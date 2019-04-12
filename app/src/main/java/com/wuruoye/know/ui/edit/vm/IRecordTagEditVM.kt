package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.Result
import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019/4/12 16:09 by wuruoye
 * Description:
 */
interface IRecordTagEditVM {
    var submitResult: MutableLiveData<Result<RecordTag>>

    fun saveRecordTag(recordTag: RecordTag?, title: String, comment: String)
}