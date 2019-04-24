package com.wuruoye.know.ui.setting.vm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.NetUtil
import com.wuruoye.know.util.log
import com.wuruoye.know.util.model.beans.NetResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * Created at 2019-04-23 20:13 by wuruoye
 * Description:
 */
class UserLoginViewModel : ViewModel(), IUserLoginVM {
    override val loginResult: MutableLiveData<NetResult> =
            MutableLiveData()

    override val registerResult: MutableLiveData<NetResult> =
            MutableLiveData()

    override val verifyCodeResult: MutableLiveData<NetResult> =
            MutableLiveData()

    override val leftSecond: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {

        }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun login(id: String, pwd: String) {
        GlobalScope.launch {
            val values = mapOf(Pair("id", id),
                Pair("pwd", pwd))
            loginResult.postValue(NetUtil.get(NetUtil.LOGIN, values))
        }
    }

    override fun register(id: String, name: String, pwd: String, phone: String, code: String) {
        GlobalScope.launch {
            val values = mapOf(Pair("id", id),
                Pair("name", name), Pair("pwd", pwd),
                Pair("phone", phone), Pair("code", code)
            )

            registerResult.postValue(NetUtil.post(NetUtil.USER, values))
        }
    }

    override fun verifyCode(phone: String) {
        GlobalScope.launch {
            val check = checkPhone(phone)
            if (check) {
                val values = mapOf(Pair("phone", phone))


//                val result = NetUtil.get(NetUtil.VERIFY_CODE, values)
                val result = NetResult(200, "ok")
                if (result.successful) {
                    leftSecond.postValue(60)
                    count()
                }
                verifyCodeResult.postValue(result)
            } else {
                verifyCodeResult.postValue(NetResult(400, "手机号输入错误"))
            }
        }
    }

    private fun count() {
        mHandler.postDelayed({
            log("count down ${leftSecond.value}")
            log("has observer ${leftSecond.hasActiveObservers()}, ${leftSecond.hasObservers()}")
            val left = leftSecond.value
            if (left is Int && left > 0) {
                leftSecond.value = left - 1
                count()
            }
        }, 1000)
    }

    private fun checkPhone(phone: String): Boolean {
        return if (phone.length != 11) {
            false
        } else {
            val regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$"
            val p = Pattern.compile(regex)
            val m = p.matcher(phone)
            m.matches()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserLoginViewModel() as T
        }
    }

    companion object {
        var sInstance: UserLoginViewModel? = null
    }
}