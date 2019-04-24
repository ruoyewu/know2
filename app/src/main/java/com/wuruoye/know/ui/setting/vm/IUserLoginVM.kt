package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.NetResult

/**
 * Created at 2019-04-23 20:08 by wuruoye
 * Description:
 */
interface IUserLoginVM {
    val loginResult: MutableLiveData<NetResult>
    val verifyCodeResult: MutableLiveData<NetResult>
    val registerResult: MutableLiveData<NetResult>
    val leftSecond: MutableLiveData<Int>

    fun login(id: String, pwd: String)
    fun verifyCode(phone: String)
    fun register(id: String, name: String, pwd: String, phone: String, code: String)
}