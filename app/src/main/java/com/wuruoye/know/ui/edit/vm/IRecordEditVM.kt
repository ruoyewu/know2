package com.wuruoye.know.ui.edit.vm

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.orm.table.RecordItem

/**
 * Created at 2019/4/11 18:25 by wuruoye
 * Description:
 */
interface IRecordEditVM {
    var recordType: MutableLiveData<RealRecordType>
    var recordData: MutableLiveData<ArrayMap<String, RecordItem>>
    var submitResult: MutableLiveData<Boolean>

    fun setRecordTypeId(id: Long)
    fun setRecordId(id: Long)
    fun saveRecordItems(items: ArrayList<RecordItem>)
}