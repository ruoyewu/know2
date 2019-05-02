package com.wuruoye.know.ui.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.setting.vm.IUserInfoVM
import com.wuruoye.know.ui.setting.vm.UserInfoViewModel
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.base.WConfig
import com.wuruoye.know.util.base.media.IWPhoto
import com.wuruoye.know.util.base.media.WPhoto
import com.wuruoye.know.util.base.permission.WPermission
import com.wuruoye.know.util.model.beans.UserInfo
import com.wuruoye.know.util.toast
import com.wuruoye.know.widgets.BottomAlertDialog
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created at 2019-04-24 10:59 by wuruoye
 * Description:
 */
class UserInfoActivity :
    LeakActivity(),
    View.OnClickListener,
    IWPhoto.OnWPhotoListener<String> {

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var llAvatar: LinearLayout
    private lateinit var civAvatar: CircleImageView
    private lateinit var llId: LinearLayout
    private lateinit var tvId: TextView
    private lateinit var llName: LinearLayout
    private lateinit var tvName: TextView
    private lateinit var llSign: LinearLayout
    private lateinit var tvSign: TextView
    private lateinit var llPhone: LinearLayout
    private lateinit var tvPhone: TextView
    private lateinit var btnLogout: Button

    private lateinit var dlgLoading: AlertDialog
    private lateinit var tvLoading: TextView

    private lateinit var dlgImg: AlertDialog

    private lateinit var dlgEdit: BottomAlertDialog
    private lateinit var tilEdit: TextInputLayout
    private lateinit var etEdit: EditText

    private lateinit var vm: IUserInfoVM
    private lateinit var mPhoto: WPhoto
    private var mType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        vm = ViewModelProviders.of(this,
            InjectorUtil.userInfoViewModelFactory(this))
            .get(UserInfoViewModel::class.java)

        mPhoto = WPhoto(this)

        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()
        vm.userInfo.value = intent.getParcelableExtra(USER_INFO)
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        llAvatar = findViewById(R.id.ll_avatar_user_info)
        civAvatar = findViewById(R.id.civ_avatar_user_info)
        llId = findViewById(R.id.ll_id_user_info)
        tvId = findViewById(R.id.tv_id_user_info)
        llName = findViewById(R.id.ll_name_user_info)
        tvName = findViewById(R.id.tv_name_user_info)
        llSign = findViewById(R.id.ll_sign_user_info)
        tvSign = findViewById(R.id.tv_sign_user_info)
        llPhone = findViewById(R.id.ll_phone_user_info)
        tvPhone = findViewById(R.id.tv_phone_user_info)
        btnLogout = findViewById(R.id.btn_logout_user_info)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)

        llAvatar.setOnClickListener(this)
        llId.setOnClickListener(this)
        llName.setOnClickListener(this)
        llSign.setOnClickListener(this)
        llPhone.setOnClickListener(this)
        btnLogout.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        val loadingView = LayoutInflater.from(this)
            .inflate(R.layout.dlg_loading, null)
        tvLoading = loadingView.findViewById(R.id.tv_dlg_loading)
        dlgLoading = AlertDialog.Builder(this)
            .setView(loadingView)
            .setCancelable(false)
            .create()

        dlgImg = AlertDialog.Builder(this)
            .setItems(ITEM_PHOTO) { _, which ->
                when(which) {
                    0 -> {
                        mPhoto.choosePhoto(filePath(), 1, 1, 500, 500, this)
                    }
                    1 -> {
                        mPhoto.takePhoto(filePath(), 1, 1, 500, 500, this)
                    }
                }
            }
            .create()

        val editView = LayoutInflater.from(this)
            .inflate(R.layout.dlg_edit, null)
        tilEdit = editView as TextInputLayout
        etEdit = editView.editText!!
        dlgEdit = BottomAlertDialog.Builder(this)
            .setContentView(editView)
            .setCancelListener(this)
            .setConfirmListener(this)
            .build()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.user_info)
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_check)
    }

    private fun subscribeUI() {
        vm.userInfo.observe(this, Observer {
            with(it) {
                Glide.with(civAvatar).load(avatar).into(civAvatar)
                tvId.text = id
                tvName.text = name
                tvPhone.text = phoneNumber
                tvSign.text = sign
            }
        })
        vm.updateUserInfoResult.observe(this, Observer {
            dlgLoading.dismiss()
            with(it) {
                if (successful) {
                    val userInfo = GsonFactory.sInstance
                        .fromJson(data, UserInfo::class.java)
                    val intent = Intent()
                    intent.putExtra(USER_INFO, userInfo)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    toast(msg!!)
                }
            }
        })
        vm.logoutResult.observe(this, Observer {
            dlgLoading.dismiss()
            with(it) {
                if (successful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    toast(msg!!)
                }
            }
        })
        vm.loadingTitle.observe(this, Observer {
            if (!dlgLoading.isShowing) {
                dlgLoading.show()
            }
            tvLoading.text = it
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.iv_more_toolbar -> {
                vm.updateUserInfo()
            }
            R.id.btn_cancel_dlg_bottom_alert -> dlgEdit.dismiss()
            R.id.btn_confirm_dlg_bottom_alert -> {
                when(mType) {
                    TYPE_NAME -> {
                        val name = etEdit.text.toString()
                        if (name.isEmpty()) {
                            tilEdit.error = "用户名不能为空"
                        } else if (name.length > 8) {
                            tilEdit.error = "用户名长度不能大于 8"
                        }
                        else {
                            dlgEdit.dismiss()
                            vm.setName(name)
                        }
                    }
                    TYPE_SIGN -> {
                        val sign = etEdit.text.toString()
                        if (sign.isEmpty()) {
                            tilEdit.error = "签名不能为空"
                        } else if (sign.length > 50) {
                            tilEdit.error = "签名长度不能大于 50"
                        }
                        else {
                            dlgEdit.dismiss()
                            vm.setSign(sign)
                        }
                    }
                }
            }
            R.id.ll_avatar_user_info -> {
                dlgImg.show()
            }
            R.id.ll_name_user_info -> {
                mType = TYPE_NAME
                showEditDlg(tvName.text, "用户名称")
            }
            R.id.ll_sign_user_info -> {
                mType = TYPE_SIGN
                showEditDlg(tvSign.text, "用户签名")
            }
            R.id.ll_phone_user_info -> {

            }
            R.id.btn_logout_user_info -> {
                vm.logout()
            }
        }
    }

    override fun onPhotoError(error: String?) {
        toast(error!!)
    }

    override fun onPhotoResult(result: String?) {
        if (result != null) {
            vm.setAvatar(result)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        WPhoto.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        WPermission.onPermissionResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showEditDlg(default: CharSequence, hint: String) {
        tilEdit.isErrorEnabled = false
        tilEdit.hint = hint
        etEdit.setText(default)
        dlgEdit.show()
    }

    private fun filePath(): String {
        return WConfig.IMAGE_PATH + System.currentTimeMillis() + ".jpg"
    }

    companion object {
        val USER_INFO = "user_info"
        val ITEM_PHOTO = arrayOf("相册选择", "相机拍照")

        val TYPE_NAME = 1
        val TYPE_SIGN = 2
        val TYPE_PHONE = 3
    }
}