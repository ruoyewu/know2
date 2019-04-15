package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/13 15:37 by wuruoye
 * Description:
 */
interface IRecordTypeSetVM {
    val recordTypeList: MutableLiveData<List<RecordType>>

    fun updateTypeList()
    fun deleteRecordType(type: RecordType)
    fun deleteRecordType(types: Array<RecordType>)
}