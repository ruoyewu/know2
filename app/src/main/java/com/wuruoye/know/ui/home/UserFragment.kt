package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.vm.IUserVM
import com.wuruoye.know.ui.home.vm.UserViewModel
import com.wuruoye.know.ui.setting.RecordTagSetActivity
import com.wuruoye.know.ui.setting.RecordTypeSetActivity
import com.wuruoye.know.ui.setting.ReviewStrategySetActivity
import com.wuruoye.know.ui.setting.UserLoginActivity
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_RECORD
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_TYPE
import com.wuruoye.know.util.model.RequestCode.USER_FOR_LOGIN
import com.wuruoye.know.util.model.RequestCode.USER_FOR_RECORD_TAG
import com.wuruoye.know.util.model.RequestCode.USER_FOR_RECORD_TYPE
import com.wuruoye.know.util.model.RequestCode.USER_FOR_REVIEW_STRATEGY
import com.wuruoye.know.util.toast
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created at 2019/4/9 21:17 by wuruoye
 * Description:
 */
class UserFragment : Fragment(), View.OnClickListener {
    private lateinit var cl: CoordinatorLayout
    private lateinit var abl: AppBarLayout
    private lateinit var ivBack: ImageView
    private lateinit var ivUser: CircleImageView
    private lateinit var tvName: TextView
    private lateinit var tvSign: TextView

    private lateinit var llRecordSize: LinearLayout
    private lateinit var tvRecordSize: TextView
    private lateinit var llRecordTypeSize: LinearLayout
    private lateinit var tvRecordTypeSize: TextView
    private lateinit var llRecordTagSize: LinearLayout
    private lateinit var tvRecordTagSize: TextView
    private lateinit var llUserInfo: LinearLayout
    private lateinit var llEdiRecordType: LinearLayout
    private lateinit var llEditReviewStrategy: LinearLayout
    private lateinit var llEditRecordTag: LinearLayout
    private lateinit var llBackup: LinearLayout

    private lateinit var vm: IUserVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this,
            InjectorUtil.userViewModelFactory(context!!))
            .get(UserViewModel::class.java)

        bindView(view)
        bindListener()
        initView()
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            cl = findViewById(R.id.cl_user)
            abl = findViewById(R.id.abl_user)
            ivBack = findViewById(R.id.iv_bg_user)
            ivUser = findViewById(R.id.civ_user)
            tvName = findViewById(R.id.tv_user_user)
            tvSign = findViewById(R.id.tv_sign_user)

            llRecordSize = findViewById(R.id.ll_record_size_user)
            tvRecordSize = findViewById(R.id.tv_record_size_user)
            llRecordTypeSize = findViewById(R.id.ll_record_type_size_user)
            tvRecordTypeSize = findViewById(R.id.tv_record_type_size_user)
            llRecordTagSize = findViewById(R.id.ll_record_tag_size_user)
            tvRecordTagSize = findViewById(R.id.tv_record_tag_size_user)
            llUserInfo = findViewById(R.id.ll_user_info_user)
            llEdiRecordType = findViewById(R.id.ll_edit_record_type_user)
            llEditReviewStrategy = findViewById(R.id.ll_edit_review_strategy_user)
            llEditRecordTag = findViewById(R.id.ll_edit_record_tag_user)
            llBackup = findViewById(R.id.ll_backup_user)
        }
    }

    private fun bindListener() {
        cl.addOnLayoutChangeListener { _, _, _, _,
                                       _, _, _, _, _ ->
            cl.dispatchDependentViewsChanged(abl)
        }
        ivUser.setOnClickListener(this)
        tvName.setOnClickListener(this)

        llRecordSize.setOnClickListener(this)
        llRecordTypeSize.setOnClickListener(this)
        llRecordTagSize.setOnClickListener(this)
        llUserInfo.setOnClickListener(this)
        llEdiRecordType.setOnClickListener(this)
        llEditReviewStrategy.setOnClickListener(this)
        llEditRecordTag.setOnClickListener(this)
        llBackup.setOnClickListener(this)
    }

    private fun initView() {

    }

    private fun subscribeUI() {
        vm.recordSize.observe(this, Observer {
            tvRecordSize.text = it.toString()
        })
        vm.recordTypeSize.observe(this, Observer {
            tvRecordTypeSize.text = it.toString()
        })
        vm.recordTagSize.observe(this, Observer {
            tvRecordTagSize.text = it.toString()
        })

        vm.userInfo.observe(this, Observer {
            if (it != null) {
                tvName.text = it.name
                tvSign.text = it.sign
                Glide.with(ivUser).load(it.avatar).into(ivUser)
            } else {
                tvName.text = "未登录"
                tvSign.text = "用户签名"
                ivUser.setImageResource(R.drawable.ic_avatar)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.civ_user -> {
                if (vm.login) {

                } else {
                    startActivityForResult(Intent(context, UserLoginActivity::class.java),
                        USER_FOR_LOGIN)
                }
            }
            R.id.tv_user_user -> {
                if (vm.login) {

                } else {

                }
            }
            R.id.ll_user_info_user -> {
                if (vm.login) {

                } else {
                    context?.toast("请先登录")
                }
            }
            R.id.ll_edit_record_type_user -> {
                startActivityForResult(Intent(context, RecordTypeSetActivity::class.java),
                    USER_FOR_RECORD_TYPE)
            }
            R.id.ll_edit_review_strategy_user -> {
                startActivityForResult(Intent(context, ReviewStrategySetActivity::class.java),
                    USER_FOR_REVIEW_STRATEGY)
            }
            R.id.ll_edit_record_tag_user -> {
                startActivityForResult(Intent(context, RecordTagSetActivity::class.java),
                    USER_FOR_RECORD_TAG)
            }
            R.id.ll_backup_user -> {
                if (vm.login) {

                } else {

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                USER_FOR_RECORD_TYPE -> {
                    vm.updateInfo()
                }
                USER_FOR_RECORD_TAG -> {
                    vm.updateInfo()
                }
                RECORD_FOR_RECORD -> {
                    vm.updateInfo()
                }
                RECORD_FOR_TYPE -> {
                    vm.updateInfo()
                }
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = UserFragment()
    }
}