package com.wuruoye.know.ui.home.vm

import androidx.collection.LongSparseArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.dao.*
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.RecordType
import com.wuruoye.know.util.orm.table.ReviewStrategy
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
    private val recordTagDao: RecordTagDao,
    private val reviewStrategyDao: ReviewStrategyDao
) : ViewModel(), IReviewVM{
    override val recordList: MutableLiveData<List<RecordListItem>> =
            MutableLiveData()

    private val recordTypeMap = LongSparseArray<RecordType>()
    private var reviewStrategyMap = LongSparseArray<ReviewStrategy>()

    init {
        updateRecordList()
    }

    override fun rememberRecord(recordListItem: RecordListItem, remember: Boolean) {
        GlobalScope.launch {
            val record = recordListItem.record
            if (remember) {
                record.remNum++
                record.lastReview = System.currentTimeMillis()
            } else {
                record.failNum++
                record.lastFailReview = System.currentTimeMillis()
            }
            recordDao.insert(record)
            updateRecordList()
        }
    }

    override fun updateRecordList() {
        GlobalScope.launch {
            val records = filterRecord(recordDao.queryAll())
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

    private fun filterRecord(recordList: List<Record>): ArrayList<Record> {
        val result = ArrayList<Record>()
        val current = System.currentTimeMillis()
        for (record in recordList) {
            val type =
                    if (recordTypeMap[record.type] == null) {
                        val t = recordTypeDao.query(record.type)
                        recordTypeMap.put(record.type, t)
                        t
                    } else {
                        recordTypeMap[record.type]!!
                    }
            val strategy =
                if (reviewStrategyMap[type.strategy] == null) {
                    val s = reviewStrategyDao.query(type.strategy)
                    reviewStrategyMap.put(type.strategy, s)
                    s
                } else {
                    reviewStrategyMap[type.strategy]!!
                }

            if (shouldShow(record, strategy, current)) {
                result.add(record)
            }
        }
        return result
    }

    private fun shouldShow(record: Record, strategy: ReviewStrategy, current: Long): Boolean {
        with(record) {
            if (remNum < strategy.rememberTime) {
                var n = failNum - remNum
                if (n <= 0) n = 1
                val gapTime = strategy.gapTime * n
                if (current - lastReview > gapTime || current - lastFailReview > gapTime) {
                    return true
                }
            }
        }
        return false
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordDao: RecordDao,
        private val recordTypeDao: RecordTypeDao,
        private val recordItemDao: RecordItemDao,
        private val recordTagDao: RecordTagDao,
        private val reviewStrategyDao: ReviewStrategyDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReviewViewModel(recordDao, recordTypeDao,
                recordItemDao, recordTagDao, reviewStrategyDao) as T
        }
    }
}