package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.adapter.RecordTypeSelectAdapter
import com.wuruoye.know.ui.edit.vm.IRecordTypeEditVM
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView
import com.wuruoye.know.widgets.BottomAlertDialog

/**
 * Created at 2019/4/10 19:44 by wuruoye
 * Description:
 */
class RecordTypeEditActivity :
    AppCompatActivity(),
    View.OnClickListener,
    ViewFactory.OnLongClickListener,
    RecordTypeSelectAdapter.OnClickListener {

    private lateinit var dlgTitle: BottomAlertDialog
    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: EditText

    private lateinit var dlgSelectItem: BottomSheetDialog
    private lateinit var rvSelectItem: RecyclerView

    private lateinit var mParent: ViewGroup
    private lateinit var mParentViews: ArrayList<RecordView>
    private lateinit var mUpdateView: RecordView

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var llContent: LinearLayout
    private lateinit var fabAdd: FloatingActionButton
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
        llContent = findViewById(R.id.ll_record_type_edit)
        fabAdd = findViewById(R.id.fab_record_type_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
        fabAdd.setOnClickListener(this)
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

        val selectAdapter = RecordTypeSelectAdapter()
        selectAdapter.setOnClickListener(this)
        rvSelectItem = LayoutInflater.from(this)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvSelectItem.layoutManager = LinearLayoutManager(this)
        rvSelectItem.adapter = selectAdapter
        dlgSelectItem = BottomSheetDialog(this)
        dlgSelectItem.setContentView(rvSelectItem)
    }

    private fun initView() {
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_check)
    }

    private fun subscribeUI() {
        vm.selectItems.observe(this, Observer {
            (rvSelectItem.adapter as RecordTypeSelectAdapter).submitList(it)
        })
        vm.recordType.observe(this, Observer {
            llContent.removeAllViews()
            for (v in it.items) {
                ViewFactory.generateEditView(applicationContext,
                    v, llContent, it.items, true, this)
            }

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

    override fun onLongClick(recordView: RecordView,
                             view: View,
                             parentView: ArrayList<RecordView>,
                             parent: ViewGroup) {
        AlertDialog.Builder(this)
            .setItems(if (recordView is RealRecordLayoutView) ITEM_LAYOUT else ITEM_VIEW) {
                _, which ->
                        when (which) {
                            0 -> {  // update
                                mParent = parent
                                mParentViews = parentView
                                mUpdateView = recordView
                                val intent = Intent(this, TypeItemEditActivity::class.java)
                                intent.putExtra(TypeItemEditActivity.RECORD_VIEW,
                                    if (recordView is RealRecordLayoutView) {
                                        RecordLayoutView(recordView, "")
                                    } else {
                                        recordView
                                    })
                                startActivityForResult(intent, FOR_UPDATE_RESULT)
                            }
                            1 -> {  // remove
                                parentView.remove(recordView)
                                parent.removeView(view)
                                vm.removeView(recordView)
                            }
                            2 -> {  // add
                                mParent = view as ViewGroup
                                mParentViews = (recordView as RealRecordLayoutView).items
                                dlgSelectItem.show()
                            }
                        }
            }
            .show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_record_type_edit -> {
                mParentViews = vm.recordType.value!!.items
                mParent = llContent
                dlgSelectItem.show()
            }
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

    override fun onClick(item: RecordTypeSelect) {
        dlgSelectItem.dismiss()
        val intent = Intent(this, TypeItemEditActivity::class.java)
        intent.putExtra(TypeItemEditActivity.RECORD_TYPE, item.id)
        startActivityForResult(intent, FOR_ADD_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val view = data!!.getParcelableExtra<RecordView>(TypeItemEditActivity.RECORD_VIEW)
            when (requestCode) {
                FOR_ADD_RESULT -> {
                    mParentViews.add(if (view is RecordLayoutView) {
                        RealRecordLayoutView(view, arrayListOf())
                    } else view)
                }
                FOR_UPDATE_RESULT -> {
                    when (view) {
                        is RecordLayoutView -> {
                            (mUpdateView as RealRecordLayoutView).setInfo(view)
                        }
                        is RecordTextView -> {
                            (mUpdateView as RecordTextView).setInfo(view)
                        }
                        is RecordImageView -> {
                            (mUpdateView as RecordImageView).setInfo(view)
                        }
                    }
                }
            }
            vm.updateView()
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
        val FOR_ADD_RESULT = 1
        val FOR_UPDATE_RESULT = 2

        val ITEM_VIEW = arrayOf("修改控件", "删除控件")
        val ITEM_LAYOUT = arrayOf("修改控件", "删除控件", "增加子控件")
    }
}