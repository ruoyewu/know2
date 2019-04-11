package com.wuruoye.know.ui.edit.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.wuruoye.know.R
import com.wuruoye.know.util.ColorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/6 20:46 by wuruoye
 * Description:
 */
class ImageViewController (private val mView: RecordImageView)
    : AbstractEditorController(), View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private lateinit var flContent: FrameLayout
    private lateinit var svOptions: ScrollView

    private lateinit var mShowView: ImageView
    private lateinit var llTint: LinearLayout
    private lateinit var tvTint: TextView
    private lateinit var llShape: LinearLayout
    private lateinit var tvShape: TextView
    private lateinit var sthBlur: Switch

    override fun attach(context: Context, fl: FrameLayout, sv: ScrollView) {
        super.attach(context)
        flContent = fl
        svOptions = sv

        initView()
    }

    private fun initView() {
        mShowView = ViewFactory.generateView(mContext, mView, flContent) as ImageView
        LayoutInflater.from(mContext)
                .inflate(R.layout.layout_image_view, svOptions, true)

        with(svOptions) {
            llWidth = findViewById(R.id.ll_width_layout_image)
            llHeight = findViewById(R.id.ll_height_layout_image)
            llTint = findViewById(R.id.ll_tint_layout_image)
            llShape = findViewById(R.id.ll_shape_layout_image)
            llMargin = findViewById(R.id.ll_margin_layout_image)
            llPadding = findViewById(R.id.ll_padding_layout_image)

            tvWidth = findViewById(R.id.tv_width_layout_image)
            tvHeight = findViewById(R.id.tv_height_layout_image)
            tvTint = findViewById(R.id.tv_tint_layout_image)
            tvShape = findViewById(R.id.tv_shape_layout_image)
            tvMargin = findViewById(R.id.tv_margin_layout_image)
            tvPadding = findViewById(R.id.tv_padding_layout_image)
            sthBlur = findViewById(R.id.sth_blur_layout_image)
        }

        llShape.setOnClickListener(this)
        llTint.setOnClickListener(this)
        sthBlur.setOnCheckedChangeListener(this)

        with(mView) {
            tvTint.text = ColorUtil.color2hex(mView.tint)
            tvTint.setTextColor(mView.tint)
            tvShape.text = SHAPE_NAME[shape]
            // TODO set shape
            sthBlur.isChecked = mView.blur
        }

        super.initView(mView, mShowView)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ll_shape_layout_image -> {
                mCurType = TYPE_SHAPE
                showSelectDlg(SHAPE_NAME, mView.shape)
            }
            R.id.ll_tint_layout_image -> {
                mCurType = TYPE_TINT
                showColorDlg(mView.tint)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mView.blur = isChecked
    }

    override fun onItemSelect(value: Int) {
        super.onItemSelect(value)
        when(mCurType) {
            TYPE_SHAPE -> {
                mView.shape = value
                tvShape.text = SHAPE_NAME[value]

                // TODO 设置图片形状
            }
        }
    }

    override fun onColorSubmit(color: Int) {
        super.onColorSubmit(color)
        when(mCurType) {
            TYPE_TINT -> {
                mView.tint = color

                tvTint.text = ColorUtil.color2hex(color)
                tvTint.setTextColor(color)
            }
        }
    }

    override val result: RecordView
        get() = mView
}