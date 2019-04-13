package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.MutableLiveData

/**
 * Created at 2019/4/13 14:16 by wuruoye
 * Description:
 */
interface IUserVM {
    var recordSize: MutableLiveData<Long>
    var recordTagSize: MutableLiveData<Long>
    var recordTypeSize: MutableLiveData<Long>
    var login: MutableLiveData<Boolean>

    fun updateInfo()
}