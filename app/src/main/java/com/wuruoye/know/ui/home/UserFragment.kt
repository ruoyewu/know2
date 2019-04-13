package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.wuruoye.know.R
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
    private lateinit var llEditRecordTag: LinearLayout
    private lateinit var llBackup: LinearLayout

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        bindListener()
        initView()
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
            tvRecordTypeSize = findViewById(R.id.tv_record_size_user)
            llRecordTagSize = findViewById(R.id.ll_record_tag_size_user)
            tvRecordTagSize = findViewById(R.id.tv_record_tag_size_user)
            llUserInfo = findViewById(R.id.ll_user_info_user)
            llEdiRecordType = findViewById(R.id.ll_edit_record_type_user)
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
        llEditRecordTag.setOnClickListener(this)
        llBackup.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = UserFragment()
    }
}