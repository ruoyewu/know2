package com.wuruoye.know.ui.edit.controller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.wuruoye.know.R
import com.wuruoye.know.util.ColorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/3/18 16:52 by wuruoye
 * Description:
 */
class TextViewController(private val mView: RecordTextView) :
        AbstractEditorController(), View.OnClickListener {
    private lateinit var flContent: FrameLayout
    private lateinit var svOptions: ScrollView

    private lateinit var mShowView: TextView
    private lateinit var llText: LinearLayout
    private lateinit var tvText: TextView
    private lateinit var llTextSize: LinearLayout
    private lateinit var tvTextSize: TextView
    private lateinit var llTextColor: LinearLayout
    private lateinit var tvTextColor: TextView
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

    override fun attach(context: Context, fl: FrameLayout, sv: ScrollView) {
        super.attach(context)
        flContent = fl
        svOptions = sv

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mShowView = ViewFactory.generateView(mContext, mView, flContent as ViewGroup) as TextView
        LayoutInflater.from(mContext)
                .inflate(R.layout.layout_text_view, svOptions)

        with(svOptions) {
            llText = findViewById(R.id.ll_text_layout_text)
            llTextSize = findViewById(R.id.ll_text_size_layout_text)
            llTextColor = findViewById(R.id.ll_text_color_layout_text)
            llBgColor = findViewById(R.id.ll_bg_color_layout_text)
            llMargin = findViewById(R.id.ll_margin_layout_text)
            llPadding = findViewById(R.id.ll_padding_layout_text)
            llTextStyle = findViewById(R.id.ll_text_style_layout_text)
            llGravity = findViewById(R.id.ll_gravity_layout_text)
            llMinLine = findViewById(R.id.ll_min_line_layout_text)
            llMaxLine = findViewById(R.id.ll_max_line_layout_text)
            llWidth = findViewById(R.id.ll_width_layout_text)
            llHeight = findViewById(R.id.ll_height_layout_text)

            tvText = findViewById(R.id.tv_text_layout_text)
            tvTextSize = findViewById(R.id.tv_text_size_layout_text)
            tvTextColor = findViewById(R.id.tv_text_color_layout_text)
            tvMargin = findViewById(R.id.tv_margin_layout_text)
            tvPadding = findViewById(R.id.tv_padding_layout_text)
            tvTextStyle = findViewById(R.id.tv_text_style_layout_text)
            tvBgColor = findViewById(R.id.tv_bg_color_layout_text)
            tvGravity = findViewById(R.id.tv_gravity_layout_text)
            tvMinLine = findViewById(R.id.tv_min_line_layout_text)
            tvMaxLine = findViewById(R.id.tv_max_line_layout_text)
            tvWidth = findViewById(R.id.tv_width_layout_text)
            tvHeight = findViewById(R.id.tv_height_layout_text)
        }

        llText.setOnClickListener(this)
        llTextSize.setOnClickListener(this)
        llTextColor.setOnClickListener(this)
        llBgColor.setOnClickListener(this)
        llTextStyle.setOnClickListener(this)
        llGravity.setOnClickListener(this)
        llMinLine.setOnClickListener(this)
        llMaxLine.setOnClickListener(this)

        with(mView) {
            tvText.text = text
            tvTextSize.text = textSize.toString()
            tvTextColor.text = ColorUtil.color2hex(textColor)
            tvTextColor.setTextColor(textColor)
            tvBgColor.text = ColorUtil.color2hex(bgColor)
            tvTextStyle.text = TEXT_STYLE_NAME[TEXT_STYLE_VALUE.indexOf(textStyle)]
            tvGravity.text = GRAVITY_NAME[GRAVITY_VALUE.indexOf(gravity)]
            tvMinLine.text = minLine.toString()
            tvMaxLine.text = maxLine.toString()
        }

        super.initView(mView, mShowView)
    }

    override val result: RecordView
        get() = mView

    override fun onEditSubmit(text: String) {
        super.onEditSubmit(text)
        when(mCurType) {
            TYPE_TEXT -> {
                mView.text = text
                mShowView.text = text
                tvText.text = text
            }
        }
    }

    override fun onItemSelect(value: Int) {
        super.onItemSelect(value)
        when(mCurType) {
            TYPE_TEXT_SIZE -> {
                mView.textSize = value
                mShowView.textSize = value.toFloat()
                tvTextSize.text = value.toString()
            }
            TYPE_TEXT_STYLE -> {
                mView.textStyle = TEXT_STYLE_VALUE[value]
                mShowView.setTypeface(mShowView.typeface, TEXT_STYLE_VALUE[value])
                tvTextStyle.text = TEXT_STYLE_NAME[value]
                if (value == 0) {
                    mShowView.typeface = Typeface.DEFAULT
                }
            }
            TYPE_GRAVITY -> {
                mView.gravity = GRAVITY_VALUE[value]
                mShowView.gravity = GRAVITY_VALUE[value]
                tvGravity.text = GRAVITY_NAME[value]
            }
            TYPE_LINE_MIN -> {
                mView.minLine = value
                mShowView.minLines = value
                tvMinLine.text = value.toString()
            }
            TYPE_LINE_MAX -> {
                mView.maxLine = value
                mShowView.maxLines = value
                tvMaxLine.text = value.toString()
            }
        }
    }

    override fun onColorSubmit(color: Int) {
        when (mCurType) {
            TYPE_TEXT_COLOR -> {
                mView.textColor = color
                mShowView.setTextColor(color)
                tvTextColor.setTextColor(color)
                tvTextColor.text = ColorUtil.color2hex(color)
            }
            TYPE_BG_COLOR -> {
                mView.bgColor = color
                mShowView.setBackgroundColor(color)
                tvBgColor.setTextColor(color)
                tvBgColor.text = ColorUtil.color2hex(color)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_text_layout_text -> {
                mCurType = TYPE_TEXT
                showEditDlg("输入文本内容", INPUT_TYPE_VALUE[4])
            }
            R.id.ll_text_size_layout_text -> {
                mCurType = TYPE_TEXT_SIZE
                showSelectDlg(TEXT_SIZE_MIN, TEXT_SIZE_MAX, mView.textSize)
            }
            R.id.ll_text_color_layout_text -> {
                mCurType = TYPE_TEXT_COLOR
                showColorDlg(mView.textColor)
            }
            R.id.ll_bg_color_layout_text -> {
                mCurType = TYPE_BG_COLOR
                showColorDlg(mView.bgColor)
            }
            R.id.ll_text_style_layout_text -> {
                mCurType = TYPE_TEXT_STYLE
                showSelectDlg(TEXT_STYLE_NAME, TEXT_STYLE_VALUE.indexOf(mView.textStyle))
            }
            R.id.ll_gravity_layout_text -> {
                mCurType = TYPE_GRAVITY
                showSelectDlg(GRAVITY_NAME, GRAVITY_VALUE.indexOf(mView.gravity))
            }
            R.id.ll_min_line_layout_text -> {
                mCurType = TYPE_LINE_MIN
                showSelectDlg(TEXT_LINE_MIN, Math.min(TEXT_LINE_MAX, mView.maxLine), mView.minLine)
            }
            R.id.ll_max_line_layout_text -> {
                mCurType = TYPE_LINE_MAX
                showSelectDlg(Math.max(TEXT_LINE_MIN, mView.minLine), TEXT_LINE_MAX, mView.maxLine)
            }
        }
    }

}
