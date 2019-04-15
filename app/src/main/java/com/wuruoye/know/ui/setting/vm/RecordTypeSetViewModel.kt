package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.RecordTypeItem
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordItemDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.dao.RecordViewDao
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Created at 2019/4/13 15:50 by wuruoye
 * Description:
 */
class RecordTypeSetViewModel(
    private val recordTypeDao: RecordTypeDao,
    private val recordDao: RecordDao,
    private val recordItemDao: RecordItemDao,
    private val recordViewDao: RecordViewDao
) : ViewModel(), IRecordTypeSetVM {

    override val recordTypeList: MutableLiveData<List<RecordType>> =
            MutableLiveData()

    init {
        updateTypeList()
    }

    override fun deleteRecordType(type: RecordType) {
        GlobalScope.launch {
            deleteRecordTypeWith(type)
            updateTypeList()
        }
    }

    override fun deleteRecordType(types: Array<RecordType>) {
        GlobalScope.launch {
            for (type in types) {
                deleteRecordTypeWith(type)
            }
            updateTypeList()
        }
    }

    override fun updateTypeList() {
        GlobalScope.launch {
            recordTypeList.postValue(recordTypeDao.queryAll())
        }
    }

    private fun deleteRecordTypeWith(recordType: RecordType) {
        val recordList = recordDao.queryByType(recordType.id!!)
        for (record in recordList) {
            recordItemDao.deleteByRecord(record.id!!)
            recordDao.delete(record.id!!)
        }

        val itemList = loadItems(recordType.items)
        for (item in itemList) {
            val view = recordViewDao.query(item.type, item.id)
            if (view != null) {
                if (view is RecordLayoutView) {
                    deleteRealRecordLayoutView(view)
                } else {
                    recordViewDao.delete(RecordTypeSelect.getType(view), view.id!!)
                }
            }
        }

        recordTypeDao.delete(recordType.id!!)
    }


    private fun deleteRealRecordLayoutView(view: RecordLayoutView) {
        val itemList = loadItems(view.items)

        for (item in itemList) {
            val v = recordViewDao.query(item.type, item.id)
            if (v != null) {
                if (v is RecordLayoutView) {
                    deleteRealRecordLayoutView(v)
                } else {
                    recordViewDao.delete(RecordTypeSelect.getType(v), v.id!!)
                }
            }
        }
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
            return RecordTypeSetViewModel(recordTypeDao, recordDao, recordItemDao, recordViewDao) as T
        }
    }
}