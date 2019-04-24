package com.wuruoye.know.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.ui.edit.vm.RecordEditViewModel
import com.wuruoye.know.ui.edit.vm.RecordTagEditViewModel
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.ui.edit.vm.ReviewStrategyEditViewModel
import com.wuruoye.know.ui.home.vm.RecordViewModel
import com.wuruoye.know.ui.home.vm.ReviewViewModel
import com.wuruoye.know.ui.home.vm.UserViewModel
import com.wuruoye.know.ui.setting.vm.RecordTagSetViewModel
import com.wuruoye.know.ui.setting.vm.RecordTypeSetViewModel
import com.wuruoye.know.ui.setting.vm.ReviewStrategySetViewModel
import com.wuruoye.know.ui.setting.vm.UserLoginViewModel
import com.wuruoye.know.util.model.AppCache
import com.wuruoye.know.util.orm.Repository

/**
 * Created at 2019/4/10 15:57 by wuruoye
 * Description:
 */
object InjectorUtil {
    fun recordViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTypeDao = Repository.getRecordType(context)
        val recordDao = Repository.getRecord(context)
        val recordItemDao = Repository.getRecordItem(context)
        val recordTagDao = Repository.getRecordTag(context)
        val cache = AppCache.getInstance(context)
        return RecordViewModel.Factory(recordTypeDao, recordDao, recordItemDao, recordTagDao, cache)
    }

    fun recordTypeEditViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTypeDao = Repository.getRecordType(context)
        val recordViewDao = Repository.getRecordView(context)
        return RecordTypeEditViewModel.Factory(recordTypeDao, recordViewDao)
    }

    fun recordEdiViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTypeDao = Repository.getRecordType(context)
        val recordDao = Repository.getRecord(context)
        val recordItemDao = Repository.getRecordItem(context)
        val recordViewDao = Repository.getRecordView(context)
        val recordTagDao = Repository.getRecordTag(context)
        return RecordEditViewModel.Factory(recordTypeDao, recordDao,
            recordItemDao, recordViewDao, recordTagDao)
    }

    fun recordTagEditViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTagDao = Repository.getRecordTag(context)
        return RecordTagEditViewModel.Factory(recordTagDao)
    }

    fun userViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordDao = Repository.getRecord(context)
        val recordTypeDao = Repository.getRecordType(context)
        val recordTagDao = Repository.getRecordTag(context)
        val cache = AppCache.getInstance(context)

        return UserViewModel.Factory(recordDao, recordTypeDao, recordTagDao, cache)
    }

    fun recordTypeSetViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTypeDao = Repository.getRecordType(context)
        val recordDao = Repository.getRecord(context)
        val recordItemDao = Repository.getRecordItem(context)
        val recordViewDao = Repository.getRecordView(context)
        return RecordTypeSetViewModel.Factory(recordTypeDao, recordDao, recordItemDao, recordViewDao)
    }

    fun recordTagSetViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTagDao = Repository.getRecordTag(context)
        val recordDao = Repository.getRecord(context)
        return RecordTagSetViewModel.Factory(recordTagDao, recordDao)
    }

    fun reviewViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordDao = Repository.getRecord(context)
        val recordTypeDao = Repository.getRecordType(context)
        val recordItemDao = Repository.getRecordItem(context)
        val recordTagDao = Repository.getRecordTag(context)
        val reviewStrategyDao = Repository.getReviewStrategy(context)
        return ReviewViewModel.Factory(recordDao, recordTypeDao, recordItemDao,
            recordTagDao, reviewStrategyDao)
    }

    fun reviewStrategyEditViewModel(context: Context): ViewModelProvider.Factory {
        val reviewStrategyDao = Repository.getReviewStrategy(context)
        return ReviewStrategyEditViewModel.Factory(reviewStrategyDao)
    }

    fun reviewStrategySetViewModel(context: Context): ViewModelProvider.Factory {
        val reviewStrategyDao = Repository.getReviewStrategy(context)
        val recordTypeDao = Repository.getRecordType(context)
        return ReviewStrategySetViewModel.Factory(reviewStrategyDao, recordTypeDao)
    }

    fun userLoginViewModelFactory(context: Context): ViewModelProvider.Factory {
        return UserLoginViewModel.Factory()
    }
}