package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.Result
import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019-04-20 15:57 by wuruoye
 * Description:
 */
interface IReviewStrategyEditVM {
    val reviewStrategy: MutableLiveData<ReviewStrategy>
    val submitResult: MutableLiveData<Result<ReviewStrategy>>
    val remTime: Int
    val gapTime: Long

    fun saveReviewStrategy(title: String)
    fun setReviewStrategyId(id: Long)
    fun setRemTime(time: Int)
    fun setGapTime(time: Long)
}