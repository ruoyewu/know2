package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.edit.vm.IReviewStrategyEditVM
import com.wuruoye.know.ui.edit.vm.ReviewStrategyEditViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.widgets.BottomAlertDialog

/**
 * Created at 2019-04-20 15:49 by wuruoye
 * Description:
 */
class ReviewStrategyEditActivity : LeakActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var tilTitle: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var llRemTime: LinearLayout
    private lateinit var tvRemTime: TextView
    private lateinit var llGapTime: LinearLayout
    private lateinit var tvGapTime: TextView
    private lateinit var btnSubmit: Button

    private lateinit var dlgEdit: Dialog
    private lateinit var tilEdit: TextInputLayout
    private lateinit var etEdit: EditText

    private lateinit var dlgGap: Dialog
    private lateinit var npGap: NumberPicker

    private lateinit var vm: IReviewStrategyEditVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_strategy_edit)

        vm = ViewModelProviders.of(this,
            InjectorUtil.reviewStrategyEditViewModel(this))
            .get(ReviewStrategyEditViewModel::class.java)

        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        tilTitle = findViewById(R.id.til_title_review_strategy_edit)
        etTitle = findViewById(R.id.et_title_review_strategy_edit)
        llRemTime = findViewById(R.id.ll_remember_time_review_strategy_edit)
        tvRemTime = findViewById(R.id.tv_remember_time_review_strategy_edit)
        llGapTime = findViewById(R.id.ll_gap_time_review_strategy_edit)
        tvGapTime = findViewById(R.id.tv_gap_time_review_strategy_edit)
        btnSubmit = findViewById(R.id.btn_review_strategy_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        llRemTime.setOnClickListener(this)
        llGapTime.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        tilEdit = LayoutInflater.from(this)
            .inflate(R.layout.dlg_edit, null) as TextInputLayout
        etEdit = tilEdit.editText!!
        tilEdit.hint = "输入记忆成功次数（ 3～20 ）"
        etEdit.inputType = InputType.TYPE_CLASS_NUMBER
        dlgEdit = BottomAlertDialog.Builder(this)
            .setContentView(tilEdit)
            .setCancelListener(this)
            .setConfirmListener(this)
            .build()

        npGap = LayoutInflater.from(this)
            .inflate(R.layout.dlg_number_picker, null) as NumberPicker
        changePickerDivider(npGap)
        npGap.minValue = 0
        npGap.maxValue = GAP_NAME.size-1
        npGap.displayedValues = GAP_NAME
        dlgGap = BottomAlertDialog.Builder(this)
            .setContentView(npGap)
            .setConfirmListener(this, Gravity.TOP)
            .build()
    }

    private fun initView() {
        vm.setReviewStrategyId(intent.getLongExtra(REVIEW_STRATEGY, -1))

        tvTitle.text = "编辑复习策略"
        ivBack.setImageResource(R.drawable.ic_left)
    }

    private fun subscribeUI() {
        vm.reviewStrategy.observe(this, Observer {
            if (etTitle.text!!.isEmpty()) {
                etTitle.setText(it.title)
            }
            tvRemTime.text = it.rememberTime.toString()
            tvGapTime.text = GAP_NAME[it.gapTime - START_GAP_VALUE]
        })

        vm.submitResult.observe(this, Observer {
            if (it.result) {
                val intent = Intent()
                intent.putExtra(REVIEW_STRATEGY, it.obj)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                tilTitle.error = it.msg
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.btn_cancel_dlg_bottom_alert -> {
                dlgEdit.dismiss()
            }
            R.id.btn_confirm_dlg_bottom_alert -> {
                try {
                    val time = etEdit.text.toString().toInt()
                    if (time < 3 || time > 20) {
                        tilEdit.error = "输入次数不规范"
                    } else {
                        vm.setRemTime(time)
                        tilEdit.isErrorEnabled = false
                        etEdit.text.clear()
                        dlgEdit.dismiss()
                    }
                } catch (e: Exception) {
                    tilEdit.error = "输入有误"
                }
            }
            R.id.btn_confirm_top_dlg_bottom_alert -> {
                dlgGap.dismiss()
                val gapTime = GAP_VALUE[npGap.value]
                vm.setGapTime(gapTime)
            }
            R.id.ll_remember_time_review_strategy_edit -> {
                etEdit.setText(vm.remTime.toString())
                etEdit.setSelection(etEdit.text.length-1)
                dlgEdit.show()
            }
            R.id.ll_gap_time_review_strategy_edit -> {
                npGap.value = GAP_VALUE.indexOf(vm.gapTime)
                dlgGap.show()
            }
            R.id.btn_review_strategy_edit -> {
                val title = etTitle.text.toString()
                when {
                    title.isEmpty() -> tilTitle.error = "名称不能为空"
                    title.length > 10 -> tilTitle.error = "名称长度不能大于 10"
                    else -> vm.saveReviewStrategy(title)
                }
            }
        }
    }

    private fun changePickerDivider(picker: NumberPicker) {
        try {
            val fields = picker.javaClass.declaredFields
            for (f in fields) {
                if (f.name == "mSelectionDivider") {
                    f.isAccessible = true
                    val drawable = ColorDrawable(
                        ActivityCompat
                            .getColor(this, R.color.transparent))
                    f.set(picker, drawable)
                    break
                }
            }
        } catch (e: Exception) {}
    }

    companion object {
        const val REVIEW_STRATEGY = "review_strategy"

        val START_GAP_VALUE = 3
        val GAP_NAME = arrayOf("短", "较短", "中", "较长", "长")
        val GAP_VALUE = arrayOf(3, 4, 5, 6, 7)

    }
}