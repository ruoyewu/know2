package com.wuruoye.know.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.FragmentAdapter
import com.wuruoye.know.util.model.beans.UserInfo

/**
 * Created at 2019-04-22 22:21 by wuruoye
 * Description:
 */
class UserLoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var tl: TabLayout
    private lateinit var vp: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindView()
        bindListener()
        initView()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        tl = findViewById(R.id.tl_toolbar)
        vp = findViewById(R.id.vp_login)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
    }

    private fun initView() {
        tvTitle.text = getString(R.string.user)
        ivBack.setImageResource(R.drawable.ic_left)

        val adapter = FragmentAdapter(supportFragmentManager,
            arrayOf(UserLoginFragment.newInstance,
                UserRegisterFragment()),
            arrayOf(getString(R.string.login), getString(R.string.register)))
        vp.adapter = adapter

        tl.visibility = View.VISIBLE
        tl.setupWithViewPager(vp)
    }

    fun setResult(userInfo: UserInfo) {
        val intent = Intent()
        intent.putExtra(USER_INFO, userInfo)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
        }
    }

    companion object {
        val USER_INFO = "user_info"
    }
}