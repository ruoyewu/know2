package com.wuruoye.know.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.wuruoye.know.util.toast

/**
 * Created at 2019-04-22 22:43 by wuruoye
 * Description:
 */
class UserRegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var tilId: TextInputLayout
    private lateinit var etId: EditText
    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilPwd: TextInputLayout
    private lateinit var etPwd: EditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var etPhone: EditText
    private lateinit var tilVerifyCode: TextInputLayout
    private lateinit var etVerifyCode: EditText
    private lateinit var btnVerifyCode: Button
    private lateinit var btnRegister: Button

    private lateinit var dlgLoading: AlertDialog
    private lateinit var tvLoading: TextView

    private lateinit var vm: IUserLoginVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this,
            InjectorUtil.userLoginViewModelFactory(context!!))
            .get(UserLoginViewModel::class.java)


        bindView(view)
        bindListener()
        initDlg()
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            tilId = findViewById(R.id.til_id_user_register)
            etId = findViewById(R.id.et_id_user_register)
            tilName = findViewById(R.id.til_name_user_register)
            etName = findViewById(R.id.et_name_user_register)
            tilPwd = findViewById(R.id.til_pwd_user_register)
            etPwd = findViewById(R.id.et_pwd_user_register)
            tilPhone = findViewById(R.id.til_phone_user_register)
            etPhone = findViewById(R.id.et_phone_user_register)
            tilVerifyCode = findViewById(R.id.til_verify_code_user_register)
            etVerifyCode = findViewById(R.id.et_verity_code_user_register)
            btnVerifyCode = findViewById(R.id.btn_verify_code_user_register)
            btnRegister = findViewById(R.id.btn_register_user_register)
        }
    }

    private fun bindListener() {
        btnVerifyCode.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        val loadingView = LayoutInflater.from(context)
            .inflate(R.layout.dlg_loading, null)
        tvLoading = loadingView.findViewById(R.id.tv_dlg_loading)
        dlgLoading = AlertDialog.Builder(context!!)
            .setView(loadingView)
            .create()
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeUI() {
        vm.verifyCodeResult.observe(this, Observer {
            with(it) {
                if (successful) {

                } else {
                    context?.toast(msg!!)
                }
            }
        })
        vm.registerResult.observe(this, Observer {
            with(it) {
                if (successful) {
                    val userInfo = GsonFactory.getInstance()
                        .fromJson(data, UserInfo::class.java)
                    (activity as UserLoginActivity).setResult(userInfo)
                } else {
                    context?.toast(msg!!)
                }
            }
        })
        vm.leftSecond.observe(this, Observer {
            when (it) {
                0 -> {
                    btnVerifyCode.isClickable = true
                    btnVerifyCode.text = getString(R.string.send_verify_code)
                }
                else -> {
                    btnVerifyCode.isClickable = false
                    btnVerifyCode.text = "已发送 $it"
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_verify_code_user_register -> {
                showLoading("正在发送验证码")
                val phone = etPhone.text.toString()
                vm.verifyCode(phone)
            }
            R.id.btn_register_user_register -> {
                tilId.isErrorEnabled = false
                tilPwd.isErrorEnabled = false
                tilName.isErrorEnabled = false
                tilPhone.isErrorEnabled = false
                tilVerifyCode.isErrorEnabled = false

                val id = etId.text.toString()
                val pwd = etPwd.text.toString()
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val code = etVerifyCode.text.toString()
                when {
                    id.isEmpty() -> tilId.error = "账号不能为空"
                    id.length < 6 -> tilId.error = "账号长度不能小于 6"
                    id.length > 32 -> tilId.error = "账号长度不能大于 32"
                    pwd.isEmpty() -> tilPwd.error = "密码不能为空"
                    pwd.length < 6 -> tilPwd.error = "密码长度不能小于 6"
                    pwd.length > 32 -> tilPwd.error = "密码长度不能大于 32"
                    name.isEmpty() -> tilName.error = "名称不能为空"
                    name.length > 8 -> tilName.error = "名称长度不能大于 8"
                    phone.isEmpty() -> tilPhone.error = "手机号不能为空"
                    code.isEmpty() -> tilVerifyCode.error = "验证码不能为空"
                    else -> {
                        showLoading("正在注册中")
                        vm.register(id, name, pwd, phone, code)
                    }
                }
            }
        }
    }

    private fun showLoading(text: String) {
        tvLoading.text = text
        dlgLoading.show()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = UserRegisterFragment()
    }
}