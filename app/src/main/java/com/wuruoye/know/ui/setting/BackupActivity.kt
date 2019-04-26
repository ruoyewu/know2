package com.wuruoye.know.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wuruoye.know.R
import com.wuruoye.know.ui.setting.vm.BackupViewModel
import com.wuruoye.know.ui.setting.vm.IBackupVM
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.toast

/**
 * Created at 2019-04-25 17:11 by wuruoye
 * Description:
 */
class BackupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var btnUpload: Button
    private lateinit var btnDownload: Button

    private lateinit var dlgLoading: AlertDialog
    private lateinit var tvLoading: TextView

    private lateinit var vm: IBackupVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        vm = ViewModelProviders.of(this,
            InjectorUtil.backupViewModelFactory(this))
            .get(BackupViewModel::class.java)

        bindView()
        bindListener()
        initVew()
        initDlg()
        subscribeUI()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        btnUpload = findViewById(R.id.btn_upload_backup)
        btnDownload = findViewById(R.id.btn_download_backup)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        btnUpload.setOnClickListener(this)
        btnDownload.setOnClickListener(this)
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
    }

    private fun initVew() {
        tvTitle.text = getString(R.string.backup)
        ivBack.setImageResource(R.drawable.ic_left)
    }

    private fun subscribeUI() {
        vm.loadingTitle.observe(this, Observer {
            tvLoading.text = it
            if (!dlgLoading.isShowing) {
                dlgLoading.show()
            }
        })
        vm.result.observe(this, Observer {
            dlgLoading.dismiss()
            if (it.successful) {

            } else {
                toast(it.msg!!)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.btn_upload_backup -> vm.backup()
            R.id.btn_download_backup -> vm.download()
        }
    }

}