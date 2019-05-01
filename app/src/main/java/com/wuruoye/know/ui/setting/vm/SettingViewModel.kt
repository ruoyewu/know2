package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.log
import com.wuruoye.know.util.model.AppCache

/**
 * Created at 2019-05-01 08:58 by wuruoye
 * Description:
 */
class SettingViewModel(
    private val cache: AppCache
) : ViewModel(), ISettingVM {
    override val haptic: MutableLiveData<Boolean> =
        MutableLiveData()

    init {
        haptic.value = cache.haptic
    }

    override fun setHaptic(value: Boolean, notify: Boolean) {
        log(value)
        cache.haptic = value
        if (notify) {
            haptic.value = value
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val cache: AppCache
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingViewModel(cache) as T
        }
    }
}