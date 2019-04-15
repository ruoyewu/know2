package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordTagDao
import com.wuruoye.know.util.orm.table.RecordTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/15 17:23 by wuruoye
 * Description:
 */
class RecordTagSetViewModel(
    private val recordTagDao: RecordTagDao,
    private val recordDao: RecordDao
) : ViewModel(), IRecordTagSetVM {
    override val recordTagList: MutableLiveData<List<RecordTag>> =
        MutableLiveData()

    override val recordTagSignal: MutableLiveData<Boolean> =
        MutableLiveData()

    init {
        updateTagList()
    }

    override fun updateTagList() {
        GlobalScope.launch {
            recordTagList.postValue(recordTagDao.queryAll())
        }
    }

    override fun deleteRecordTag(tag: RecordTag) {
        GlobalScope.launch {
            deleteRecordTagWith(tag)
            updateTagList()
        }
    }

    override fun deleteRecordTag(tags: Array<RecordTag>) {
        GlobalScope.launch {
            for (tag in tags) {
                deleteRecordTagWith(tag)
            }

            updateTagList()
        }
    }

    private fun deleteRecordTagWith(tag: RecordTag) {
        if (tag.id == 0L) {
            recordTagSignal.postValue(true)
            return
        }

        val recordList = recordDao.queryByTag(tag.id!!)
        for (record in recordList) {
            record.tag = 0
            recordDao.insert(record)
        }
        recordTagDao.delete(tag.id!!)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTagDao: RecordTagDao,
        private val recordDao: RecordDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordTagSetViewModel(recordTagDao, recordDao) as T
        }
    }
}