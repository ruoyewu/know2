package com.wuruoye.know.ui.edit.vm

import android.util.ArrayMap
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.model.beans.*
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
 * Created at 2019-04-25 11:38 by wuruoye
 * Description:
 */
class RecordShowViewModel(
    private val recordDao: RecordDao,
    private val recordTypeDao: RecordTypeDao,
    private val recordItemDao: RecordItemDao,
    private val recordViewDao: RecordViewDao
) : ViewModel(), IRecordShowVM {

    override val recordShow: MutableLiveData<RecordShowWithView> =
        MutableLiveData()

    override val defaultShow: MutableLiveData<List<RecordShow>> =
        MutableLiveData()

    private lateinit var itemList: ArrayList<RecordListItem>
    private var current: Int = 0

    override fun setItemList(itemList: ArrayList<RecordListItem>, cur: Int) {
        this.itemList = itemList

        current = when {
            cur < 0 -> 0
            cur >= itemList.size -> itemList.size-1
            else -> cur
        }

        GlobalScope.launch {
            val cu = getNext()!!
            val next = getNext()

            val list = ArrayList<RecordShow>()
            list.add(cu)
            if (next != null) list.add(next)

            defaultShow.postValue(list)
        }
    }

    override fun showInViewGroup(viewGroup: ViewGroup) {
        GlobalScope.launch {
            if (itemList.isNotEmpty()) {
                val item = if (current < itemList.size) {
                    itemList.removeAt(current)
                } else {
                    itemList.removeAt(itemList.size-1)
                }

                recordShow.postValue(
                    RecordShowWithView(
                        queryRecordShow(item.record),
                        viewGroup
                    )
                )
            }
        }
    }

    override fun rememberRecord(record: Record, remember: Boolean?) {
        GlobalScope.launch {
            when {
                remember == null -> {
                    record.reviewNum++
                    record.lastReview
                }
                remember -> {
                    record.remNum++
                    record.lastReview = System.currentTimeMillis()
                }
                else -> {
                    record.failNum++
                    record.lastFailReview = System.currentTimeMillis()
                }
            }

            recordDao.insert(record)
        }
    }

    private fun getNext(): RecordShow? {
        return if (itemList.isNotEmpty()) {
            val item = if (current < itemList.size) {
                itemList.removeAt(current)
            } else {
                itemList.removeAt(itemList.size-1)
            }

            queryRecordShow(item.record)
        } else {
            null
        }
    }

    private fun queryRecordShow(record: Record): RecordShow {
        val recordType = queryRealRecordType(record.type)
        val map = ArrayMap<String, RecordItem>()
        val list = recordItemDao.queryByRecord(record.id!!)
        for (item in list) {
            map["${item.type}_${item.typeId}"] = item
        }

        return RecordShow(recordType, map, record)
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
        private val recordDao: RecordDao,
        private val recordTypeDao: RecordTypeDao,
        private val recordItemDao: RecordItemDao,
        private val recordViewDao: RecordViewDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordShowViewModel(recordDao, recordTypeDao, recordItemDao, recordViewDao) as T
        }
    }
}