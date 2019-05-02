package com.wuruoye.know.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.edit.vm.IRecordTagEditVM
import com.wuruoye.know.ui.edit.vm.RecordTagEditViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.toast

/**
 * Created at 2019/4/12 16:08 by wuruoye
 * Description:
 */
class RecordTagEditActivity : LeakActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var tilComment: TextInputLayout
    private lateinit var etComment: TextInputEditText
    private lateinit var btnSubmit: Button

    private lateinit var vm: IRecordTagEditVM
    private var mTag: RecordTag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_tag_edit)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordTagEditViewModelFactory(applicationContext))
            .get(RecordTagEditViewModel::class.java)

        initData()
        bindView()
        bindListener()
        initView()
        subscribeUI()
    }

    private fun initData() {
        mTag = intent.getParcelableExtra(RECORD_TAG)
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        tilTitle = findViewById(R.id.til_title_record_tag_edit)
        tilComment = findViewById(R.id.til_comment_record_tag_edit)
        etTitle = findViewById(R.id.et_title_record_tag_edit)
        etComment = findViewById(R.id.et_comment_record_tag_edit)
        btnSubmit = findViewById(R.id.btn_record_tag_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    private fun initView() {
        tvTitle.text = getString(R.string.tag_edit)
        ivBack.setImageResource(R.drawable.ic_left)

        if (mTag != null) {
            etTitle.setText(mTag!!.title)
            etComment.setText(mTag!!.comment)
        }
    }

    private fun subscribeUI() {
        vm.submitResult.observe(this, Observer {
            if (it.result) {
                val intent = Intent()
                intent.putExtra(RECORD_TAG, it.obj)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else toast(it.msg)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.btn_record_tag_edit -> {
                saveTag()
            }
        }
    }

    private fun saveTag() {
        val title = etTitle.text.toString()
        val comment = etComment.text.toString()
        if (title.isEmpty()) {
            tilTitle.error = "标签名不能为空"
        } else {
            vm.saveRecordTag(mTag, title, comment)
        }
    }

    companion object {
        val RECORD_TAG = "record_tag"
    }
}