package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.*
import com.wuruoye.know.util.model.AppCache
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.RecordType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/9 21:28 by wuruoye
 * Description:
 */
class RecordViewModel(
    private val recordTypeDao: RecordTypeDao,
    private val recordDao: RecordDao,
    private val cache: AppCache
) : ViewModel(), IRecordVM{

    override var typeTimeLimit
            = MutableLiveData<Int>().apply { value = cache.typeTimeLimit }

    override var typeTypeLimit
            = MutableLiveData<Long>().apply { value = cache.typeTypeLimit }

    override var recordTypeList: MutableLiveData<List<RecordType>>
            = MutableLiveData<List<RecordType>>().apply {
        GlobalScope.launch {
            postValue(recordTypeDao.queryAll())
        }
    }

    override var recordList: MutableLiveData<List<Record>> =
        MediatorLiveData<List<Record>>().apply {
        addSource(typeTimeLimit) {
            updateRecordWithLimit()
        }
        addSource(typeTypeLimit) {
            updateRecordWithLimit()
        }
    }

    override var timeLimitList
            = MutableLiveData<List<TimeLimitItem>>().apply {
        value = TIME_LIMIT_ITEM
    }

    override var recordTypeTitle
            = MediatorLiveData<String>().apply {
        addSource(typeTypeLimit) {
            GlobalScope.launch {
                val type = recordTypeDao.query(it)
                postValue(type?.title ?: "不限")
            }
        }
    }

    override var recordTypeTime = Transformations.map(typeTimeLimit) {
        TIME_LIMIT_TITLE[it]
    } as LiveData<String>

    override fun updateRecordType() {
        GlobalScope.launch {
            recordTypeList.postValue(
                recordTypeDao.queryAll()
            )
        }
    }

    override fun setTimeLimit(limit: Int) {
        typeTimeLimit.value = limit
        cache.typeTimeLimit = limit
    }

    override fun setTypeLimit(limit: Long) {
        typeTypeLimit.value = limit
        cache.typeTypeLimit = limit
    }

    private fun updateRecordWithLimit() {
        GlobalScope.launch {
            val timeLimit = typeTimeLimit.value!!
            val typeLimit = typeTypeLimit.value!!
            recordList.postValue(
                if (typeLimit < 0) {
                    recordDao.queryByTime(getTimeLimit(timeLimit))
                } else {
                    recordDao.queryByTimeType(getTimeLimit(timeLimit), typeLimit)
                }
            )
        }
    }

    private fun getTimeLimit(limitType: Int): Long {
        val oneDay = 86400000L
        val time = when (limitType) {
            1 -> oneDay
            2 -> oneDay * 2
            3 -> oneDay * 3
            4 -> oneDay * 7
            5 -> oneDay * 30
            6 -> oneDay * 365
            else -> Long.MAX_VALUE
        }
        return System.currentTimeMillis() - time
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTypeDao: RecordTypeDao,
        private val recordDao: RecordDao,
        private val cache: AppCache
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordViewModel(recordTypeDao, recordDao, cache) as T
        }
    }

    companion object {
        val TIME_LIMIT_TITLE
                = arrayOf("不限", "一天前", "两天前", "三天前", "一周前", "一月前", "一年前")
        val TIME_LIMIT_ITEM
                = arrayListOf<TimeLimitItem>().apply {
            for (i in 0 until TIME_LIMIT_TITLE.size) {
                add(TimeLimitItem(i, TIME_LIMIT_TITLE[i]))
            }
        }
    }
}