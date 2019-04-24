package com.wuruoye.know.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.setting.vm.IUserLoginVM
import com.wuruoye.know.ui.setting.vm.UserLoginViewModel
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.beans.UserInfo

/**
 * Created at 2019-04-22 22:42 by wuruoye
 * Description:
 */
class UserLoginFragment : Fragment(), View.OnClickListener {
    private lateinit var tilId: TextInputLayout
    private lateinit var etId: EditText
    private lateinit var tilPwd: TextInputLayout
    private lateinit var etPwd: EditText
    private lateinit var btnLogin: Button

    private lateinit var vm: IUserLoginVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this,
            InjectorUtil.userLoginViewModelFactory(context!!))
            .get(UserLoginViewModel::class.java)

        bindView(view)
        bindListener()
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            tilId = findViewById(R.id.til_id_user_login)
            etId = findViewById(R.id.et_id_user_login)
            tilPwd = findViewById(R.id.til_pwd_user_login)
            etPwd = findViewById(R.id.et_pwd_user_login)
            btnLogin = findViewById(R.id.btn_login_user_login)
        }
    }

    private fun bindListener() {
        btnLogin.setOnClickListener(this)
    }

    private fun subscribeUI() {
        vm.loginResult.observe(this, Observer {
            with(it) {
                if (successful) {
                    val userInfo = GsonFactory.getInstance()
                        .fromJson(data, UserInfo::class.java)
                    (activity as UserLoginActivity).setResult(userInfo)
                } else {
                    tilId.error = msg
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_login_user_login -> {
                tilId.isErrorEnabled = false
                tilPwd.isErrorEnabled = false

                val id = etId.text.toString()
                val pwd = etPwd.text.toString()
                when {
                    id.isEmpty() -> tilId.error = "账号不能为空"
                    pwd.isEmpty() -> tilPwd.error = "密码不能为空"
                    else -> vm.login(id, pwd)
                }
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = UserLoginFragment()
    }
}