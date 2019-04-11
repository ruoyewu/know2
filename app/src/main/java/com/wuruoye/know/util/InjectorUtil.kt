package com.wuruoye.know.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.ui.home.vm.RecordViewModel
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
        val cache = AppCache.getInstance(context)
        return RecordViewModel.Factory(recordTypeDao, recordDao, cache)
    }

    fun recordTypeEditViewModelFactory(context: Context): ViewModelProvider.Factory {
        val recordTypeDao = Repository.getRecordType(context)
        val recordViewDao = Repository.getRecordView(context)
        return RecordTypeEditViewModel.Factory(recordTypeDao, recordViewDao)
    }
}