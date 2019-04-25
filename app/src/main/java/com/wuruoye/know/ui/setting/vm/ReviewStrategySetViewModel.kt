package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import com.wuruoye.know.util.orm.dao.ReviewStrategyDao
import com.wuruoye.know.util.orm.table.ReviewStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019-04-21 19:03 by wuruoye
 * Description:
 */
class ReviewStrategySetViewModel(
    private val reviewStrategyDao: ReviewStrategyDao,
    private val recordTypeDao: RecordTypeDao
) : ViewModel(), IReviewStrategySetVM {
    override val reviewStrategyList: MutableLiveData<List<ReviewStrategy>> =
            MutableLiveData()

    override val reviewStrategySignal: MutableLiveData<Boolean> =
        MutableLiveData()

    init {
        updateStrategyList()
    }

    override fun deleteReviewStrategy(strategy: ReviewStrategy) {
        GlobalScope.launch {
            deleteStrategyWith(strategy)
            updateStrategyList()
        }
    }

    override fun deleteReviewStrategy(strategies: Array<ReviewStrategy>) {
        GlobalScope.launch {
            for (strategy in strategies) {
                deleteStrategyWith(strategy)
            }
            updateStrategyList()
        }
    }

    override fun updateStrategyList() {
        GlobalScope.launch {
            reviewStrategyList.postValue(reviewStrategyDao.queryAll())
        }
    }

    private fun deleteStrategyWith(strategy: ReviewStrategy) {
        if (strategy.id == 0L) {
            reviewStrategySignal.postValue(true)
            return
        }

        val recordTypeList = recordTypeDao.queryByStrategy(strategy.id!!)
        for (recordType in recordTypeList) {
            recordType.strategy = 0
            recordTypeDao.insert(recordType)
        }

        reviewStrategyDao.delete(strategy.id!!)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val reviewStrategyDao: ReviewStrategyDao,
        private val recordTypeDao: RecordTypeDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReviewStrategySetViewModel(reviewStrategyDao, recordTypeDao) as T
        }
    }
}