package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.NetUtil
import com.wuruoye.know.util.UploadUtil
import com.wuruoye.know.util.model.beans.NetResult
import com.wuruoye.know.util.model.beans.TokenResult
import com.wuruoye.know.util.model.beans.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019-04-24 13:07 by wuruoye
 * Description:
 */
class UserInfoViewModel : ViewModel(), IUserInfoVM{
    override val updateUserInfoResult: MutableLiveData<NetResult> =
        MutableLiveData()

    override val logoutResult: MutableLiveData<NetResult> =
        MutableLiveData()

    override val loadingTitle: MutableLiveData<String> =
        MutableLiveData()

    override val userInfo: MutableLiveData<UserInfo> =
        MutableLiveData()

    override fun updateUserInfo() {
        GlobalScope.launch {
            with(userInfo.value!!) {
                // 首先上传头像文件
                if (!avatar.startsWith("http")) {
                    loadingTitle.postValue("获取上传文件token中")
                    val tokenResult = NetUtil.get(NetUtil.UPLOAD_TOKEN, mapOf())
                    if (tokenResult.successful) {
                        val token = GsonFactory.getInstance()
                            .fromJson(tokenResult.data, TokenResult::class.java)

                        loadingTitle.postValue("上传头像中")
                        val uploadResult =
                            UploadUtil.uploadFile(avatar, token.key, token.token)
                        if (uploadResult.successful) {
                            avatar = generateAvatarPath(token.key)
                        } else {
                            updateUserInfoResult.postValue(uploadResult)
                        }
                    } else {
                        updateUserInfoResult.postValue(tokenResult)
                    }
                }

                val values = mapOf(Pair("id", id),
                    Pair("name", name), Pair("sign", sign), Pair("avatar", avatar),
                    Pair("phone", phoneNumber)
                )

                loadingTitle.postValue("上传用户信息中")
                val updateResult = NetUtil.put(NetUtil.USER, values)
                updateUserInfoResult.postValue(updateResult)
            }
        }
    }

    override fun logout() {
        GlobalScope.launch {
            loadingTitle.postValue("正在注销中")
            val res = NetUtil.get(NetUtil.LOGOUT, mapOf())
            logoutResult.postValue(res)
        }
    }

    override fun setAvatar(path: String) {
        userInfo.value?.avatar = path
        userInfo.value = userInfo.value
    }

    override fun setName(name: String) {
        userInfo.value?.name = name
        userInfo.value = userInfo.value
    }

    override fun setPhone(phone: String) {
        userInfo.value?.phoneNumber = phone
        userInfo.value = userInfo.value
    }

    override fun setSign(sign: String) {
        userInfo.value?.sign = sign
        userInfo.value = userInfo.value
    }

    private fun generateAvatarPath(key: String): String {
        return "http://qiniu.wuruoye.com/$key"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserInfoViewModel() as T
        }
    }
}