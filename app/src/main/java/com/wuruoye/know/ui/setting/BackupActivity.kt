package com.wuruoye.know.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wuruoye.know.R
import com.wuruoye.know.ui.setting.vm.IBackupVM

/**
 * Created at 2019-04-25 17:11 by wuruoye
 * Description:
 */
class BackupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView

    private lateinit var vm: IBackupVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
    }

    private fun initVew() {

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
        }
    }

}