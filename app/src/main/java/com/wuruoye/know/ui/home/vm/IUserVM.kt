package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.UserInfo

/**
 * Created at 2019/4/13 14:16 by wuruoye
 * Description:
 */
interface IUserVM {
    val recordSize: MutableLiveData<Long>
    val recordTagSize: MutableLiveData<Long>
    val recordTypeSize: MutableLiveData<Long>

    val userInfo: MutableLiveData<UserInfo?>
    val login: Boolean

    fun updateInfo()
}