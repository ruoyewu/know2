package com.wuruoye.know.ui.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.setting.vm.BackupViewModel
import com.wuruoye.know.ui.setting.vm.IBackupVM
import com.wuruoye.know.util.DateUtil
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.toast

/**
 * Created at 2019-04-25 17:11 by wuruoye
 * Description:
 */
class BackupActivity : LeakActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var tvRecordSize: TextView
    private lateinit var tvRecordTypeSize: TextView
    private lateinit var tvRecordTagSize: TextView
    private lateinit var tvReviewStrategySize: TextView
    private lateinit var tvLastBackup: TextView
    private lateinit var btnUpload: Button
    private lateinit var btnDownload: Button

    private lateinit var dlgLoading: AlertDialog
    private lateinit var tvLoading: TextView

    private lateinit var vm: IBackupVM
    private var mResult = false

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
        tvRecordSize = findViewById(R.id.tv_record_size_backup)
        tvRecordTypeSize = findViewById(R.id.tv_record_type_size_backup)
        tvRecordTagSize = findViewById(R.id.tv_record_tag_size_backup)
        tvReviewStrategySize = findViewById(R.id.tv_review_strategy_size_backup)
        tvLastBackup = findViewById(R.id.tv_last_backup_backup)
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
        vm.backupInfo.observe(this, Observer {
            dlgLoading.dismiss()
            with(it) {
                tvRecordSize.text = record.toString()
                tvRecordTypeSize.text = record_type.toString()
                tvRecordTagSize.text = record_tag.toString()
                tvReviewStrategySize.text = review_strategy.toString()
                tvLastBackup.text =
                        if (last_backup < 0) "无备份"
                        else DateUtil.milli2Date(last_backup)
            }
        })
        vm.loadingTitle.observe(this, Observer {
            tvLoading.text = it
            if (!dlgLoading.isShowing) {
                dlgLoading.show()
            }
        })
        vm.backupResult.observe(this, Observer {
            dlgLoading.dismiss()
            if (it.successful) {
                vm.updateInfo()
                mResult = true
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

    override fun onBackPressed() {
        if (mResult) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}