package com.wuruoye.know.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.edit.controller.*
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/11 09:24 by wuruoye
 * Description:
 */
class TypeItemEditActivity : LeakActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var flContent: FrameLayout
    private lateinit var svOptions: ScrollView

    private var mView: RecordView? = null
    private var mType = 0
    private lateinit var mController : EditorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_item_edit)

        initData()
        getController()
        bindView()
        bindListener()
        initView()
    }

    private fun initData() {
        mView = intent.getParcelableExtra(RECORD_VIEW)
        if (mView == null) {
            mType = intent.getIntExtra(RECORD_TYPE, 0)
        }
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        flContent = findViewById(R.id.fl_type_item_edit)
        svOptions = findViewById(R.id.sv_type_item_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
    }

    private fun initView() {
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_check)

        mController.attach(this, flContent, svOptions)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.iv_more_toolbar -> {
                var view = mController.result
                if (view is RealRecordLayoutView) {
                    view = RecordLayoutView(view, "")
                }
                if (view.createTime > 0) {
                    view.updateTime = System.currentTimeMillis()
                } else {
                    view.createTime = System.currentTimeMillis()
                }
                val intent = Intent()
                intent.putExtra(RECORD_VIEW, view)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun getController() {
        val view = mView
        val type = mType
        mController = if ((view is RecordTextView && view.editable) ||
                type == RecordTypeSelect.TYPE_EDIT) {
            EditTextController(
                if (view == null) {
                    RecordTextView(true)
                } else {
                    view as RecordTextView
                }
            )
        } else if ((view is RecordTextView && !view.editable) ||
                type == RecordTypeSelect.TYPE_TEXT) {
            TextViewController(
                if (view == null) {
                    RecordTextView(false)
                } else {
                    view as RecordTextView
                }
            )
        } else if (view is RecordLayoutView ||
                type == RecordTypeSelect.TYPE_LAYOUT) {
            LayoutViewController(
                if (view == null) {
                    RealRecordLayoutView(RecordLayoutView(), arrayListOf())
                } else {
                    RealRecordLayoutView(view as RecordLayoutView, arrayListOf())
                }
            )
        } else if (view is RecordImageView ||
                type == RecordTypeSelect.TYPE_IMG) {
            ImageViewController(
                if (view == null) {
                    RecordImageView()
                } else {
                    view as RecordImageView
                }
            ) as EditorController
        }
        else {
            throw RuntimeException()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mController.recycler()
    }

    companion object {
        val RECORD_TYPE = "type"
        val RECORD_VIEW = "view"
    }
}