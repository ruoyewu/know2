package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/10 14:58 by wuruoye
 * Description:
 */
interface IRecordVM {
    var recordList: MutableLiveData<List<RecordListItem>>
    var timeLimitList: MutableLiveData<List<TimeLimitItem>>
    var recordTypeList: MutableLiveData<List<RecordType>>
    var recordTagList: MutableLiveData<List<RecordTag>>

    var recordTypeTitle: MediatorLiveData<String>
    var recordLimitTitle: LiveData<String>
    var recordTagTitle: MutableLiveData<String>

    fun updateRecordType()
    fun updateRecord()
    fun removeRecord(id: Long)

    fun setTimeLimit(limit: Int)
    fun setTypeLimit(limit: Long)
    fun setTagLimit(limit: Long)
}