package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordItemDao
import com.wuruoye.know.util.orm.dao.RecordTagDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019-04-19 23:30 by wuruoye
 * Description:
 */
class ReviewViewModel(
    private val recordDao: RecordDao,
    private val recordTypeDao: RecordTypeDao,
    private val recordItemDao: RecordItemDao,
    private val recordTagDao: RecordTagDao
) : ViewModel(), IReviewVM{
    override val recordList: MutableLiveData<List<RecordListItem>> =
            MutableLiveData()

    init {
        updateRecordList()
    }

    override fun updateRecordList() {
        GlobalScope.launch {
            val records = recordDao.queryAll()
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

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordDao: RecordDao,
        private val recordTypeDao: RecordTypeDao,
        private val recordItemDao: RecordItemDao,
        private val recordTagDao: RecordTagDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReviewViewModel(recordDao, recordTypeDao, recordItemDao, recordTagDao) as T
        }
    }
}