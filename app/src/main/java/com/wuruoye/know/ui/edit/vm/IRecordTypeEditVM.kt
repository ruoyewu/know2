package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/10 19:45 by wuruoye
 * Description:
 */
interface IRecordTypeEditVM {
    var selectItems: MutableLiveData<List<RecordTypeSelect>>
    var recordType: MediatorLiveData<RealRecordType>
    var submitResult: MutableLiveData<Boolean>

    fun setRecordTypeId(id: Long?)
    fun saveRecordType()
    fun setTitle(title: String)
    fun updateView()
    fun removeView(view: RecordView)
}