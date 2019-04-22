package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.model.beans.Result
import com.wuruoye.know.util.orm.dao.ReviewStrategyDao
import com.wuruoye.know.util.orm.table.ReviewStrategy
import com.wuruoye.know.util.update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019-04-21 18:20 by wuruoye
 * Description:
 */
class ReviewStrategyEditViewModel(
    private val reviewStrategyDao: ReviewStrategyDao
) : ViewModel(), IReviewStrategyEditVM {
    override val reviewStrategy: MutableLiveData<ReviewStrategy> =
            MutableLiveData()

    override val submitResult: MutableLiveData<Result<ReviewStrategy>> =
            MutableLiveData()

    private lateinit var strategy: ReviewStrategy

    override val gapTime: Long
        get() = strategy.gapTime

    override val remTime: Int
        get() = strategy.rememberTime

    override fun saveReviewStrategy(title: String) {
        GlobalScope.launch {
            if (strategy.id == null && reviewStrategyDao.queryByTitle(title) != null) {
                submitResult.postValue(Result(false, "已存在同名策略"))
            } else {
                strategy.title = title
                strategy.update()
                val id = reviewStrategyDao.insert(strategy)
                strategy.id = id
                submitResult.postValue(Result(true, "", strategy))
            }
        }
    }

    override fun setReviewStrategyId(id: Long) {
        GlobalScope.launch {
            strategy = if (id < 0) ReviewStrategy()
                        else reviewStrategyDao.query(id)
            reviewStrategy.postValue(strategy)
        }
    }

    override fun setGapTime(time: Long) {
        strategy.gapTime = time
        reviewStrategy.value = strategy
    }

    override fun setRemTime(time: Int) {
        strategy.rememberTime = time
        reviewStrategy.value = strategy
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val reviewStrategyDao: ReviewStrategyDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReviewStrategyEditViewModel(reviewStrategyDao) as T
        }
    }
}