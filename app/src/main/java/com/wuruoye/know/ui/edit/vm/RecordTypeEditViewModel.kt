package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordTypeItem
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.dao.RecordViewDao
import com.wuruoye.know.util.orm.table.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Created at 2019/4/10 19:46 by wuruoye
 * Description:
 */
class RecordTypeEditViewModel(
    private val recordTypeDao: RecordTypeDao,
    private val recordViewDao: RecordViewDao
) : ViewModel(), IRecordTypeEditVM {
    override var selectItems
            = MutableLiveData<List<RecordTypeSelect>>()
        .apply {
            value = arrayListOf(
                RecordTypeSelect(RecordTypeSelect.TYPE_TEXT, "标签"),
                RecordTypeSelect(RecordTypeSelect.TYPE_EDIT, "编辑框"),
                RecordTypeSelect(RecordTypeSelect.TYPE_IMG, "图片"),
                RecordTypeSelect(RecordTypeSelect.TYPE_LAYOUT, "容器")
            )
        }

    var recordTypeId = MutableLiveData<Long?>().apply { value = null }

    override var recordType
            = MediatorLiveData<RealRecordType>().apply {
        addSource(recordTypeId) {
            GlobalScope.launch {
                postValue(
                    if (it == null) {
                        RealRecordType("")
                    } else {
                        queryRealRecordType(it)
                    }
                )
            }
        }
    }

    override var submitResult: MutableLiveData<Boolean> = MutableLiveData()

    override fun setRecordTypeId(id: Long?) {
        recordTypeId.value = id
    }

    override fun saveRecordType() {
        GlobalScope.launch {
            val type = recordType.value
            if (type != null) {
                val items = type.items
                val itemList = ArrayList<RecordTypeItem>()
                for (item in items) {
                    itemList.add(saveRecordView(item))
                }
                val recordType = RecordType(type, GsonFactory.getInstance().toJson(itemList))
                if (recordType.createTime < 0) recordType.createTime = System.currentTimeMillis()
                else recordType.updateTime = System.currentTimeMillis()
                val id = recordTypeDao.insert(recordType)
                submitResult.postValue(id >= 0)
            }
        }
    }

    override fun setTitle(title: String) {
        val type = recordType.value
        if (type != null) {
            type.title = title
        }
        recordType.value = type
    }

    override fun updateView() {
        recordType.value = recordType.value
    }

    override fun removeView(view: RecordView) {
        GlobalScope.launch {
            if (view is RealRecordLayoutView) {
                for (v in view.items) {
                    removeView(v)
                }
            }

            recordViewDao.delete(RecordTypeSelect.getType(view), view.id!!)
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

    private fun saveRecordView(recordView: RecordView): RecordTypeItem {
        when(recordView) {
            is RecordTextView -> {
                val type = RecordTypeSelect.TYPE_TEXT
                val id = recordViewDao.insert(recordView)
                return RecordTypeItem(type, id)
            }
            is RecordImageView -> {
                val type = RecordTypeSelect.TYPE_IMG
                val id = recordViewDao.insert(recordView)
                return RecordTypeItem(type, id)
            }
            is RealRecordLayoutView -> {
                val type = RecordTypeSelect.TYPE_LAYOUT
                val items = recordView.items
                val itemList = ArrayList<RecordTypeItem>()
                for (item in items) {
                    itemList.add(saveRecordView(item))
                }
                val layoutView = RecordLayoutView(recordView,
                    GsonFactory.getInstance().toJson(itemList))
                val id = recordViewDao.insert(layoutView)
                return RecordTypeItem(type, id)
            }
            else -> throw RuntimeException()
        }
    }

    private fun loadItems(text: String): List<RecordTypeItem> {
        val result = ArrayList<RecordTypeItem>()
        val array = JSONArray(text)
        for (i in 0 until array.length()) {
            result.add(GsonFactory.getInstance()
                .fromJson(array.getString(i), RecordTypeItem::class.java))
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTypeDao: RecordTypeDao,
        private val recordViewDao: RecordViewDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordTypeEditViewModel(recordTypeDao, recordViewDao) as T
        }
    }
}