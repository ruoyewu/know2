package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.*
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.AppCache
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordItemDao
import com.wuruoye.know.util.orm.dao.RecordTagDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.orm.table.RecordType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/9 21:28 by wuruoye
 * Description:
 */
@Suppress("UNUSED_EXPRESSION")
class RecordViewModel(
    private val recordTypeDao: RecordTypeDao,
    private val recordDao: RecordDao,
    private val recordItemDao: RecordItemDao,
    private val recordTagDao: RecordTagDao,
    private val cache: AppCache
) : ViewModel(), IRecordVM{

    override var typeTimeLimit: MutableLiveData<Int> =
        MutableLiveData<Int>().apply { value = cache.typeTimeLimit }

    override var typeTypeLimit: MutableLiveData<Long> =
        MutableLiveData<Long>().apply { value = cache.typeTypeLimit }

    override var typeTagLimit: MutableLiveData<Long> =
        MutableLiveData<Long>().apply { value = cache.typeTagLimit }

    override var recordTypeList: MutableLiveData<List<RecordType>>
            = MutableLiveData<List<RecordType>>().apply {
        GlobalScope.launch { postValue(recordTypeDao.queryAll()) }
    }

    override var timeLimitList
            = MutableLiveData<List<TimeLimitItem>>().apply { value = TIME_LIMIT_ITEM }

    override var recordTagList: MutableLiveData<List<RecordTag>> =
        MutableLiveData<List<RecordTag>>().apply {
            GlobalScope.launch {
                postValue(recordTagDao.queryAll())
            }
        }

    override var recordList: MutableLiveData<List<RecordListItem>> =
        MediatorLiveData<List<RecordListItem>>().apply {
            addSource(typeTimeLimit) { updateRecord() }
            addSource(typeTypeLimit) { updateRecord() }
            addSource(typeTagLimit) { updateRecord() }
        }

    override var recordTypeTitle
            = MediatorLiveData<String>().apply {
        addSource(typeTypeLimit) {
            GlobalScope.launch {
                postValue(if (it < 0) "不限" else recordTypeDao.query(it).title)
            }
        }
    }

    override var recordTagTitle: MutableLiveData<String> =
        MediatorLiveData<String>().apply {
            addSource(typeTagLimit) {
                GlobalScope.launch {
                    postValue(if (it < 0) "不限" else recordTagDao.query(it).title)
                }
            }
        }

    override var recordLimitTitle
            = Transformations.map(typeTimeLimit) {
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

    override fun setTagLimit(limit: Long) {
        typeTagLimit.value = limit
        cache.typeTagLimit = limit
    }

    override fun updateRecord() {
        GlobalScope.launch {
            val timeLimit = getTimeLimit(typeTimeLimit.value!!)
            val typeLimit = typeTypeLimit.value!!
            val tagLimit = typeTagLimit.value!!
            val records =
                if (typeLimit < 0) {
                    if (tagLimit < 0) {
                        recordDao.queryByTime(timeLimit)
                    } else {
                        recordDao.queryByTagTime(tagLimit, timeLimit)
                    }
            } else {
                    if (tagLimit < 0) {
                        recordDao.queryByTimeType(timeLimit, typeLimit)
                    } else {
                        recordDao.queryByTypeTagTime(typeLimit, tagLimit, timeLimit)
                    }
                }
            val recordListItems = ArrayList<RecordListItem>()
            for (record in records) {
                val title = recordTypeDao.query(record.type).title
                val tag = recordTagDao.query(record.tag).title
                val content = recordItemDao.queryByType(record.id!!, RecordTypeSelect.TYPE_TEXT)?.content
                val path = recordItemDao.queryByType(record.id!!, RecordTypeSelect.TYPE_IMG)?.content
                val imgPath = if (path == null) {
                    null
                } else {
                    GsonFactory.getInstance().fromJson(path, ImagePath::class.java)
                }
                recordListItems.add(RecordListItem(record, title, tag, content, imgPath))
            }
            recordList.postValue(recordListItems)
        }
    }

    override fun removeRecord(id: Long) {
        GlobalScope.launch {
            recordDao.delete(id)
            recordItemDao.deleteByRecord(id)
            updateRecord()
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
        private val recordItemDao: RecordItemDao,
        private val recordTagDao: RecordTagDao,
        private val cache: AppCache
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordViewModel(recordTypeDao, recordDao, recordItemDao, recordTagDao, cache) as T
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