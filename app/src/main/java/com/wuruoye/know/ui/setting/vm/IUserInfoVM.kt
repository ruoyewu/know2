package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.NetResult
import com.wuruoye.know.util.model.beans.UserInfo

/**
 * Created at 2019-04-24 11:20 by wuruoye
 * Description:
 */
interface IUserInfoVM {
    val userInfo: MutableLiveData<UserInfo>
    val updateUserInfoResult: MutableLiveData<NetResult>
    val logoutResult: MutableLiveData<NetResult>
    val loadingTitle: MutableLiveData<String>

    fun updateUserInfo()
    fun logout()

    fun setAvatar(path: String)
    fun setName(name: String)
    fun setSign(sign: String)
    fun setPhone(phone: String)
}