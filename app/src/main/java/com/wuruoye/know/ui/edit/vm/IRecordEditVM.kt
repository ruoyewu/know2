package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordShow
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019/4/11 18:25 by wuruoye
 * Description:
 */
interface IRecordEditVM {
    val recordTagList: MutableLiveData<List<RecordTag>>

    val recordShow: MutableLiveData<RecordShow>
    val recordType: RealRecordType
    val recordTagTitle: MutableLiveData<String>
    val submitResult: MutableLiveData<Boolean>
    val nextReviewTime: MutableLiveData<Long>

    fun setRecordTypeId(id: Long)
    fun setRecordId(id: Long)
    fun setRecordTag(tag: Long)
    fun updateRecordTagList()
    fun saveRecordItems(items: ArrayList<RecordItem>)
}