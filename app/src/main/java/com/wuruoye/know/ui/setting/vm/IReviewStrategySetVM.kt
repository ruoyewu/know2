package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019-04-21 19:02 by wuruoye
 * Description:
 */
interface IReviewStrategySetVM {
    val reviewStrategyList: MutableLiveData<List<ReviewStrategy>>
    val reviewStrategySignal: MutableLiveData<Boolean>

    fun updateStrategyList()
    fun deleteReviewStrategy(strategy: ReviewStrategy)
    fun deleteReviewStrategy(strategies: Array<ReviewStrategy>)
}