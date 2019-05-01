package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData

/**
 * Created at 2019-05-01 08:57 by wuruoye
 * Description:
 */
interface ISettingVM {
    val haptic: MutableLiveData<Boolean>

    fun setHaptic(value: Boolean, notify: Boolean = true)
}