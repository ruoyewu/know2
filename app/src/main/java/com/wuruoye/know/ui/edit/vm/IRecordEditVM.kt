package com.wuruoye.know.ui.edit.vm

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019/4/11 18:25 by wuruoye
 * Description:
 */
interface IRecordEditVM {
    var recordTagList: MutableLiveData<List<RecordTag>>

    var recordType: MutableLiveData<RealRecordType>
    var recordData: MutableLiveData<ArrayMap<String, RecordItem>>
    var recordTagTitle: MutableLiveData<String>
    var submitResult: MutableLiveData<Boolean>

    fun setRecordTypeId(id: Long)
    fun setRecordId(id: Long)
    fun setRecordTag(tag: Long)
    fun updateRecordTagList()
    fun saveRecordItems(items: ArrayList<RecordItem>)
}