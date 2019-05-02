package com.wuruoye.know.ui.edit.vm

import android.util.ArrayMap
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.ReviewUtil
import com.wuruoye.know.util.log
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordShow
import com.wuruoye.know.util.model.beans.RecordTypeItem
import com.wuruoye.know.util.orm.dao.*
import com.wuruoye.know.util.orm.table.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Created at 2019/4/11 18:26 by wuruoye
 * Description:
 */
class RecordEditViewModel(
    private val recordTypeDao: RecordTypeDao,
    private val recordDao: RecordDao,
    private val recordItemDao: RecordItemDao,
    private val recordViewDao: RecordViewDao,
    private val recordTagDao: RecordTagDao,
    private val reviewStrategyDao: ReviewStrategyDao
) : ViewModel(), IRecordEditVM {

    override val recordTagList: MutableLiveData<List<RecordTag>> =
        MediatorLiveData<List<RecordTag>>()

    override val recordShow: MutableLiveData<RecordShow> =
        MutableLiveData()

    override val recordType: RealRecordType
        get() = recordShow.value!!.recordType

    override val submitResult: MutableLiveData<Boolean> =
        MutableLiveData()

    override val recordTagTitle: MutableLiveData<String> =
        MutableLiveData()

    override val nextReviewTime: MutableLiveData<Long> =
        MutableLiveData()

    private var recordId: Long? = null

    private var recordTagId: Long = 0


    override fun saveRecordItems(items: ArrayList<RecordItem>) {
        GlobalScope.launch {
            val id = recordId
            val record =
                if (id == null) {
                    Record(recordType.id!!)
                } else {
                    recordDao.query(id)!!
                }
            record.tag = recordTagId
            if (record.createTime > 0) record.updateTime = System.currentTimeMillis()
            else record.createTime = System.currentTimeMillis()

            val recordId = recordDao.insert(record)
            if (recordId >= 0) {
                for (item in items) {
                    item.recordId = recordId
                    if (item.createTime < 0) item.createTime = System.currentTimeMillis()
                    else item.updateTime = System.currentTimeMillis()
                    recordItemDao.insert(item)
                }

                submitResult.postValue(true)
            } else {
                submitResult.postValue(false)
            }
        }
    }

    override fun setRecordId(id: Long) {
        GlobalScope.launch {
            val record = recordDao.query(id)
            val realRecordType = queryRealRecordType(record.type)
            val map = ArrayMap<String, RecordItem>()
            val list = recordItemDao.queryByRecord(id)
            for (item in list) {
                map["${item.type}_${item.typeId}"] = item
            }

            recordShow.postValue(RecordShow(realRecordType, map, record))

            val strategy = reviewStrategyDao.query(realRecordType.strategy)
            log("${strategy.id} + ${strategy.gapTime}")
            val nextReview = ReviewUtil.nextReviewTime(record, strategy)
            nextReviewTime.postValue(nextReview)

            setRecordTag(record.tag)
            recordId = id
        }
    }

    override fun setRecordTypeId(id: Long) {
        GlobalScope.launch {
            val realRecordType = queryRealRecordType(id)
            recordShow.postValue(RecordShow(realRecordType))
        }
    }

    override fun setRecordTag(tag: Long) {
        recordTagId = tag
        GlobalScope.launch {
            recordTagTitle.postValue(
                recordTagDao.query(tag).title
            )
        }
    }

    override fun updateRecordTagList() {
        GlobalScope.launch { recordTagList.postValue(recordTagDao.queryAll()) }
    }

    private fun queryRealRecordType(id: Long): RealRecordType {
        val type = recordTypeDao.query(id)
        val itemList = loadItems(type.items)

        val views = ArrayList<RecordView>()
        for (item in itemList) {
            val view = recordViewDao.query(item.type, item.id)
            if (view is RecordLayoutView) {
                views.add(queryRealRecordLayoutView(view))
            } else {
                views.add(view!!)
            }
        }

        return RealRecordType(type, views)
    }

    private fun queryRealRecordLayoutView(view: RecordLayoutView): RealRecordLayoutView {
        val itemList = loadItems(view.items)

        val views = ArrayList<RecordView>()
        for (item in itemList) {
            val v = recordViewDao.query(item.type, item.id)
            if (v is RecordLayoutView) {
                views.add(queryRealRecordLayoutView(v))
            } else {
                views.add(v!!)
            }
        }

        return RealRecordLayoutView(view, views)
    }

    private fun loadItems(text: String): List<RecordTypeItem> {
        val result = ArrayList<RecordTypeItem>()
        val array = JSONArray(text)
        for (i in 0 until array.length()) {
            result.add(
                GsonFactory.sInstance
                    .fromJson(array.getString(i), RecordTypeItem::class.java))
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTypeDao: RecordTypeDao,
        private val recordDao: RecordDao,
        private val recordItemDao: RecordItemDao,
        private val recordViewDao: RecordViewDao,
        private val recordTagDao: RecordTagDao,
        private val reviewStrategyDao: ReviewStrategyDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordEditViewModel(recordTypeDao, recordDao,
                recordItemDao, recordViewDao, recordTagDao, reviewStrategyDao) as T
        }
    }
}