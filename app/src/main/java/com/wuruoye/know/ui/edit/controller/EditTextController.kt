package com.wuruoye.know.ui.edit.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.wuruoye.know.R
import com.wuruoye.know.util.ColorUtil
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/3/27 18:28 by wuruoye
 * Description:
 */
class EditTextController(private val mView: RecordTextView) : AbstractEditorController(),
        View.OnClickListener {
    private lateinit var svOptions: ScrollView
    private lateinit var flContent: FrameLayout

    private lateinit var llTextSize: LinearLayout
    private lateinit var tvTextSize: TextView
    private lateinit var llTextColor: LinearLayout
    private lateinit var tvTextColor: TextView
    private lateinit var llHint: LinearLayout
    private lateinit var tvHint: TextView
    private lateinit var llHintSize: LinearLayout
    private lateinit var tvHintSize: TextView
    private lateinit var llHintColor: LinearLayout
    private lateinit var tvHintColor: TextView
    private lateinit var llTextStyle: LinearLayout
    private lateinit var tvTextStyle: TextView
    private lateinit var llBgColor: LinearLayout
    private lateinit var tvBgColor: TextView
    private lateinit var llGravity: LinearLayout
    private lateinit var tvGravity: TextView
    private lateinit var llMinLine: LinearLayout
    private lateinit var tvMinLine: TextView
    private lateinit var llMaxLine: LinearLayout
    private lateinit var tvMaxLine: TextView
    private lateinit var llInputType: LinearLayout
    private lateinit var tvInputType: TextView

    override val result: RecordView
        get() = mView

    override fun attach(context: Context, fl: FrameLayout, sv: ScrollView) {
        super.attach(context, mView, fl)
        this.flContent = fl
        this.svOptions = sv

        initView()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        updateView()
        LayoutInflater.from(mContext)
                .inflate(R.layout.layout_edit_text, svOptions)

        with(svOptions) {
            llTextSize = findViewById(R.id.ll_text_size_layout_edit)
            llTextColor = findViewById(R.id.ll_text_color_layout_edit)
            llHint = findViewById(R.id.ll_hint_layout_edit)
            llHintSize = findViewById(R.id.ll_hint_size_layout_edit)
            llHintColor = findViewById(R.id.ll_hint_color_layout_edit)
            llBgColor = findViewById(R.id.ll_bg_color_layout_edit)
            llMargin = findViewById(R.id.ll_margin_layout_edit)
            llPadding = findViewById(R.id.ll_padding_layout_edit)
            llTextStyle = findViewById(R.id.ll_text_style_layout_edit)
            llGravity = findViewById(R.id.ll_gravity_layout_edit)
            llInputType = findViewById(R.id.ll_input_type_layout_edit)
            llMinLine = findViewById(R.id.ll_min_line_layout_edit)
            llMaxLine = findViewById(R.id.ll_max_line_layout_edit)
            llWidth = findViewById(R.id.ll_width_layout_edit)
            llHeight = findViewById(R.id.ll_height_layout_edit)

            tvTextSize = findViewById(R.id.tv_text_size_layout_edit)
            tvTextColor = findViewById(R.id.tv_text_color_layout_edit)
            tvHint = findViewById(R.id.tv_hint_layout_edit)
            tvHintSize = findViewById(R.id.tv_hint_size_layout_edit)
            tvHintColor = findViewById(R.id.tv_hint_color_layout_edit)
            tvMargin = findViewById(R.id.tv_margin_layout_edit)
            tvPadding = findViewById(R.id.tv_padding_layout_edit)
            tvTextStyle = findViewById(R.id.tv_text_style_layout_edit)
            tvBgColor = findViewById(R.id.tv_bg_color_layout_edit)
            tvGravity = findViewById(R.id.tv_gravity_layout_edit)
            tvInputType = findViewById(R.id.tv_input_type_layout_edit)
            tvMinLine = findViewById(R.id.tv_min_line_layout_edit)
            tvMaxLine = findViewById(R.id.tv_max_line_layout_edit)
            tvWidth = findViewById(R.id.tv_width_layout_edit)
            tvHeight = findViewById(R.id.tv_height_layout_edit)
        }

        llTextSize.setOnClickListener(this)
        llTextColor.setOnClickListener(this)
        llHint.setOnClickListener(this)
        llHintColor.setOnClickListener(this)
        llHintSize.setOnClickListener(this)
        llBgColor.setOnClickListener(this)
        llTextStyle.setOnClickListener(this)
        llGravity.setOnClickListener(this)
        llInputType.setOnClickListener(this)
        llMinLine.setOnClickListener(this)
        llMaxLine.setOnClickListener(this)

        with(mView) {
            tvTextSize.text = textSize.toString()
            tvTextColor.text = ColorUtil.color2hex(textColor)
            tvTextColor.setTextColor(textColor)
            tvHint.text = hint
            tvHintSize.text = hintSize.toString()
            tvHintColor.text = ColorUtil.color2hex(hintColor)
            tvHintColor.setTextColor(hintColor)
            tvBgColor.text = ColorUtil.color2hex(bgColor)
            tvTextStyle.text = TEXT_STYLE_NAME[TEXT_STYLE_VALUE.indexOf(textStyle)]
            tvGravity.text = GRAVITY_NAME[GRAVITY_VALUE.indexOf(gravity)]
            tvInputType.text = INPUT_TYPE_NAME[INPUT_TYPE_VALUE.indexOf(inputType)]
            tvMinLine.text = minLine.toString()
            tvMaxLine.text = maxLine.toString()
        }

        super.initView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_text_size_layout_edit -> {
                mCurType = TYPE_TEXT_SIZE
                showSelectDlg(TEXT_SIZE_MIN, TEXT_SIZE_MAX, mView.textSize)
            }
            R.id.ll_text_color_layout_edit -> {
                mCurType = TYPE_TEXT_COLOR
                showColorDlg(mView.textColor)
            }
            R.id.ll_hint_layout_edit -> {
                mCurType = TYPE_HINT
                showEditDlg("输入提示内容", INPUT_TYPE_VALUE[4])
            }
            R.id.ll_hint_size_layout_edit -> {
                mCurType = TYPE_HINT_SIZE
                showSelectDlg(TEXT_SIZE_MIN, TEXT_SIZE_MAX, mView.hintSize)
            }
            R.id.ll_hint_color_layout_edit -> {
                mCurType = TYPE_HINT_COLOR
                showColorDlg(mView.hintColor)
            }
            R.id.ll_bg_color_layout_edit -> {
                mCurType = TYPE_BG_COLOR
                showColorDlg(mView.bgColor)
            }
            R.id.ll_text_style_layout_edit -> {
                mCurType = TYPE_TEXT_STYLE
                showSelectDlg(TEXT_STYLE_NAME, TEXT_STYLE_VALUE.indexOf(mView.textStyle))
            }
            R.id.ll_gravity_layout_edit -> {
                mCurType = TYPE_GRAVITY
                showSelectDlg(GRAVITY_NAME, GRAVITY_VALUE.indexOf(mView.gravity))
            }
            R.id.ll_input_type_layout_edit -> {
                mCurType = TYPE_INPUT_TYPE
                showSelectDlg(INPUT_TYPE_NAME, INPUT_TYPE_VALUE.indexOf(mView.inputType))
            }
            R.id.ll_min_line_layout_edit -> {
                mCurType = TYPE_LINE_MIN
                showSelectDlg(TEXT_LINE_MIN, Math.min(TEXT_LINE_MAX, mView.maxLine), mView.minLine)
            }
            R.id.ll_max_line_layout_edit -> {
                mCurType = TYPE_LINE_MAX
                showSelectDlg(Math.max(TEXT_LINE_MIN, mView.minLine), TEXT_LINE_MAX, mView.maxLine)
            }
        }
    }

    override fun onEditSubmit(text: String) {
        super.onEditSubmit(text)
        when (mCurType) {
            TYPE_HINT -> {
                mView.hint = text
                tvHint.text = text

                updateView()
            }
        }
    }

    override fun onItemSelect(value: Int) {
        super.onItemSelect(value)
        when (mCurType) {
            TYPE_TEXT_SIZE -> {
                mView.textSize = value
                tvTextSize.text = value.toString()

                updateView()
            }
            TYPE_HINT_SIZE -> {
                mView.hintSize = value
                tvHintSize.text = value.toString()

                updateView()
            }
            TYPE_TEXT_STYLE -> {
                mView.textStyle = TEXT_STYLE_VALUE[value]
                tvTextStyle.text = TEXT_STYLE_NAME[value]

                updateView()
            }
            TYPE_GRAVITY -> {
                mView.gravity = GRAVITY_VALUE[value]
                tvGravity.text = GRAVITY_NAME[value]

                updateView()
            }
            TYPE_LINE_MIN -> {
                mView.minLine = value
                tvMinLine.text = value.toString()

                updateView()
            }
            TYPE_INPUT_TYPE -> {
                mView.inputType = INPUT_TYPE_VALUE[value]
                tvInputType.text = INPUT_TYPE_NAME[value]

                updateView()
            }
            TYPE_LINE_MAX -> {
                mView.maxLine = value
                tvMaxLine.text = value.toString()

                updateView()
            }
        }
    }

    override fun onColorSubmit(color: Int) {
        when (mCurType) {
            TYPE_TEXT_COLOR -> {
                mView.textColor = color
                tvTextColor.setTextColor(color)
                tvTextColor.text = ColorUtil.color2hex(color)

                updateView()
            }
            TYPE_HINT_COLOR -> {
                mView.hintColor = color
                tvHintColor.setTextColor(color)
                tvHintColor.text = ColorUtil.color2hex(color)

                updateView()
            }
            TYPE_BG_COLOR -> {
                mView.bgColor = color
                tvBgColor.setTextColor(color)
                tvBgColor.text = ColorUtil.color2hex(color)

                updateView()
            }
        }
    }
}
