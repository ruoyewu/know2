package com.wuruoye.know.ui.edit.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.wuruoye.know.R
import com.wuruoye.know.util.ColorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/5 22:09 by wuruoye
 * Description:
 */
class LayoutViewController(private val mView: RealRecordLayoutView) :
        AbstractEditorController(), View.OnClickListener {
    private lateinit var flContent: FrameLayout
    private lateinit var svOptions: ScrollView

    private lateinit var mShowView: LinearLayout
    private lateinit var llOrientation: LinearLayout
    private lateinit var tvOrientation: TextView
    private lateinit var llBgColor: LinearLayout
    private lateinit var tvBgColor: TextView
    private lateinit var llGravity: LinearLayout
    private lateinit var tvGravity: TextView

    override fun attach(context: Context, fl: FrameLayout, sv: ScrollView) {
        super.attach(context)
        flContent = fl
        svOptions = sv

        initView()
    }

    private fun initView() {
//        mShowView = ViewFactory.generateView(mContext, mView, flContent) as LinearLayout
        mShowView = ViewFactory.generateView(mContext, mView, flContent) as LinearLayout
        LayoutInflater.from(mContext)
                .inflate(R.layout.layout_layout_view, svOptions)

        with(svOptions) {
            llWidth = findViewById(R.id.ll_width_layout_layout)
            llHeight = findViewById(R.id.ll_height_layout_layout)
            llOrientation = findViewById(R.id.ll_orientation_layout_layout)
            llBgColor = findViewById(R.id.ll_bg_color_layout_layout)
            llMargin = findViewById(R.id.ll_margin_layout_layout)
            llPadding = findViewById(R.id.ll_padding_layout_layout)
            llGravity = findViewById(R.id.ll_gravity_layout_layout)

            tvWidth = findViewById(R.id.tv_width_layout_layout)
            tvHeight = findViewById(R.id.tv_height_layout_layout)
            tvOrientation = findViewById(R.id.tv_orientation_layout_layout)
            tvBgColor = findViewById(R.id.tv_bg_color_layout_layout)
            tvMargin = findViewById(R.id.tv_margin_layout_layout)
            tvPadding = findViewById(R.id.tv_padding_layout_layout)
            tvGravity = findViewById(R.id.tv_gravity_layout_layout)
        }

        llOrientation.setOnClickListener(this)
        llBgColor.setOnClickListener(this)
        llGravity.setOnClickListener(this)

        with(mView) {
            tvOrientation.text = ORIENTATION_NAME[ORIENTATION_VALUE.indexOf(orientation)]
            tvBgColor.text = ColorUtil.color2hex(bgColor)
            tvGravity.text = GRAVITY_NAME[if (GRAVITY_VALUE.indexOf(gravity) < 0) 0
                                            else GRAVITY_VALUE.indexOf(gravity)]
        }

        super.initView(mView, mShowView)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_orientation_layout_layout -> {
                mCurType = TYPE_ORIENTATION
                showSelectDlg(ORIENTATION_NAME, ORIENTATION_VALUE.indexOf(mView.orientation))
            }
            R.id.ll_bg_color_layout_layout -> {
                mCurType = TYPE_BG_COLOR
                showColorDlg(mView.bgColor)
            }
            R.id.ll_gravity_layout_layout -> {
                mCurType = TYPE_GRAVITY
                showSelectDlg(GRAVITY_NAME, GRAVITY_VALUE.indexOf(mView.gravity))
            }
        }
    }

    override fun onItemSelect(value: Int) {
        super.onItemSelect(value)
        when (mCurType) {
            TYPE_ORIENTATION -> {
                mView.orientation = ORIENTATION_VALUE[value]
                tvOrientation.text = ORIENTATION_NAME[value]

                mShowView.orientation = ORIENTATION_VALUE[value]
            }
            TYPE_GRAVITY -> {
                mView.gravity = GRAVITY_VALUE[value]
                tvGravity.text = GRAVITY_NAME[value]

                mShowView.gravity = GRAVITY_VALUE[value]
            }
        }
    }

    override fun onColorSubmit(color: Int) {
        when (mCurType) {
            TYPE_BG_COLOR -> {
                mView.bgColor = color
                tvBgColor.text = ColorUtil.color2hex(color)

                mShowView.setBackgroundColor(color)
            }
        }
    }

    override val result: RecordView
        get() = mView
}