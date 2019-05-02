package com.wuruoye.know.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.setting.vm.ISettingVM
import com.wuruoye.know.ui.setting.vm.SettingViewModel
import com.wuruoye.know.util.InjectorUtil

/**
 * Created at 2019-05-01 08:56 by wuruoye
 * Description:
 */
class SettingActivity :
    LeakActivity(),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var llHaptic: LinearLayout
    private lateinit var sthHaptic: Switch

    private lateinit var vm: ISettingVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        vm = ViewModelProviders.of(this,
            InjectorUtil.settingViewModelFactory(this))
            .get(SettingViewModel::class.java)

        bindView()
        bindListener()
        initView()
        subscribeUI()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        llHaptic = findViewById(R.id.ll_haptic_setting)
        sthHaptic = findViewById(R.id.sth_haptic_setting)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        llHaptic.setOnClickListener(this)

        sthHaptic.setOnCheckedChangeListener(this)
    }

    private fun initView() {
        tvTitle.text = getString(R.string.more_setting)
        ivBack.setImageResource(R.drawable.ic_left)
    }

    private fun subscribeUI() {
        vm.haptic.observe(this, Observer {
            sthHaptic.isChecked = it
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.ll_haptic_setting -> {
                sthHaptic.isChecked = !sthHaptic.isChecked
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        vm.setHaptic(isChecked, false)
    }
}