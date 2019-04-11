package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/10 14:58 by wuruoye
 * Description:
 */
interface IRecordVM {
    var recordTypeList: MutableLiveData<List<RecordType>>
    var recordList: MutableLiveData<List<Record>>
    var timeLimitList: MutableLiveData<List<TimeLimitItem>>

    var recordTypeTitle: MediatorLiveData<String>
    val recordTypeTime: LiveData<String>
    var typeTimeLimit: MutableLiveData<Int>
    var typeTypeLimit: MutableLiveData<Long>

    fun updateRecordType()

    fun setTimeLimit(limit: Int)
    fun setTypeLimit(limit: Long)
}