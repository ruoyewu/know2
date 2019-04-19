package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RecordListItem

/**
 * Created at 2019-04-19 23:27 by wuruoye
 * Description:
 */
interface IReviewVM {
    val recordList: MutableLiveData<List<RecordListItem>>

    fun updateRecordList()
}