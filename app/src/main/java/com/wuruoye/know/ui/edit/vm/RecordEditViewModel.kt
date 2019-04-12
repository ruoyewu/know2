package com.wuruoye.know.ui.edit.vm

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordTypeItem
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordItemDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.dao.RecordViewDao
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordView
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
    private val recordViewDao: RecordViewDao
) : ViewModel(), IRecordEditVM {
    override var recordData: MutableLiveData<ArrayMap<String, RecordItem>> =
            MutableLiveData()

    override var recordType: MutableLiveData<RealRecordType> = MutableLiveData()

    override var submitResult: MutableLiveData<Boolean> = MutableLiveData()

    private var recordId: Long? = null


    override fun saveRecordItems(items: ArrayList<RecordItem>) {
        GlobalScope.launch {
            val id = recordId
            val record =
                if (id == null) {
                    Record(recordType.value!!.id!!)
                } else {
                    recordDao.query(id)!!
                }
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
            val list = recordItemDao.queryByRecord(id)
            val map = ArrayMap<String, RecordItem>()
            for (item in list) {
                map["${item.type}_${item.typeId}"] = item
            }

            recordData.postValue(map)
            recordId = id
        }
    }

    override fun setRecordTypeId(id: Long) {
        GlobalScope.launch {
            recordType.postValue(
                queryRealRecordType(id)
            )
        }
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
                GsonFactory.getInstance()
                    .fromJson(array.getString(i), RecordTypeItem::class.java))
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTypeDao: RecordTypeDao,
        private val recordDao: RecordDao,
        private val recordItemDao: RecordItemDao,
        private val recordViewDao: RecordViewDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordEditViewModel(recordTypeDao, recordDao, recordItemDao, recordViewDao) as T
        }
    }
}