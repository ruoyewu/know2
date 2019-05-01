package com.wuruoye.know.ui.edit.vm

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.RecordShow
import com.wuruoye.know.util.model.beans.RecordShowWithView
import com.wuruoye.know.util.orm.table.Record

/**
 * Created at 2019-04-24 23:01 by wuruoye
 * Description:
 */
interface IRecordShowVM {
    val defaultShow: MutableLiveData<List<RecordShow>>
    val recordShow: MutableLiveData<RecordShowWithView>

    fun setItemList(itemList: ArrayList<RecordListItem>, cur: Int)
    fun showInViewGroup(viewGroup: ViewGroup)
    fun rememberRecord(record: Record, remember: Boolean? = null)
}