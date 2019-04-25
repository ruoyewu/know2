package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.RealRecordType
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordView
import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019/4/10 19:45 by wuruoye
 * Description:
 */
interface IRecordTypeEditVM {
    val selectItems: MutableLiveData<List<RecordTypeSelect>>
    val reviewStrategyList: MutableLiveData<List<ReviewStrategy>>

    val recordType: MediatorLiveData<RealRecordType>
    val reviewStrategyTitle: MutableLiveData<String>
    val submitResult: MutableLiveData<Boolean>

    fun setRecordTypeId(id: Long?)
    fun saveRecordType()
    fun setTitle(title: String)
    fun setReviewStrategy(id: Long)
    fun updateView()
    fun updateReviewStrategy()
    fun removeView(view: RecordView)
}