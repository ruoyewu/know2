package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019/4/15 17:17 by wuruoye
 * Description:
 */
interface IRecordTagSetVM {
    val recordTagList: MutableLiveData<List<RecordTag>>
    val recordTagSignal: MutableLiveData<Boolean>

    fun updateTagList()
    fun deleteRecordTag(tag: RecordTag)
    fun deleteRecordTag(tags: Array<RecordTag>)
}