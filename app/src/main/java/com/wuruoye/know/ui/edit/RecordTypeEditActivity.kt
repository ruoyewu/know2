package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.vm.IRecordTypeEditVM
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.ui.home.adapter.FragmentAdapter
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.widgets.BottomAlertDialog

/**
 * Created at 2019/4/10 19:44 by wuruoye
 * Description:
 */
class RecordTypeEditActivity :
    AppCompatActivity(),
    View.OnClickListener {

    private lateinit var dlgTitle: BottomAlertDialog
    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: EditText

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var tl: TabLayout
    private lateinit var vp: ViewPager
    private lateinit var vm: IRecordTypeEditVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_type_edit)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordTypeEditViewModelFactory(applicationContext))
            .get(RecordTypeEditViewModel::class.java)

        initData()
        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()
    }

    private fun initData() {
        val id = intent.getLongExtra(RECORD_TYPE, -1)
        vm.setRecordTypeId(if (id == -1L) null else id)
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        tl = findViewById(R.id.tl_toolbar)
        vp = findViewById(R.id.vp_record_type_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
        tvTitle.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        tilTitle = LayoutInflater.from(this)
            .inflate(R.layout.dlg_edit, null) as TextInputLayout
        tilTitle.hint = "记录类型名称"
        etTitle = tilTitle.editText!!
        etTitle.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitTitle()
            }
            false
        }
        dlgTitle = BottomAlertDialog.Builder(this)
            .setContentView(tilTitle)
            .setConfirmListener(this)
            .setCancelListener(this)
            .setCancelable(false)
            .build()
    }

    private fun initView() {
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_check)

        val adapter = FragmentAdapter(supportFragmentManager,
            arrayOf(RecordTypeEditFragment.newInstance,
                RecordTypeEditShowFragment.newInstance),
            arrayOf("编辑", "预览"))
        vp.adapter = adapter
        tl.visibility = View.VISIBLE
        tl.setupWithViewPager(vp)
    }

    private fun subscribeUI() {
        vm.recordType.observe(this, Observer {
            tvTitle.text = it.title
            if (it.title.isEmpty()) {
                dlgTitle.show()
            }
        })
        vm.submitResult.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {

            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_title_toolbar -> {
                dlgTitle.show()
            }
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.iv_more_toolbar -> {
                vm.saveRecordType()
            }
            R.id.btn_confirm_dlg_bottom_alert -> {
                submitTitle()
            }
            R.id.btn_cancel_dlg_bottom_alert -> {
                finish()
            }
        }
    }

    private fun submitTitle() {
        val text = etTitle.text
        when {
            text.isEmpty() -> tilTitle.error = "名字不能为空"
            text.length > MAX_TITLE_LENGTH -> tilTitle.error = "名字长度不能超过 $MAX_TITLE_LENGTH"
            else -> {
                dlgTitle.dismiss()
                vm.setTitle(text.toString())
            }
        }
    }

    companion object {
        const val MAX_TITLE_LENGTH = 10
        const val RECORD_TYPE = "type"

        val ITEM_VIEW = arrayOf("修改控件", "删除控件")
        val ITEM_LAYOUT = arrayOf("修改控件", "删除控件", "增加子控件")
    }
}