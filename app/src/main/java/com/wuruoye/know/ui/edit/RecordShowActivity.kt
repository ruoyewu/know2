package com.wuruoye.know.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R

/**
 * Created at 2019-04-24 18:26 by wuruoye
 * Description:
 */
class RecordShowActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var flContent: FrameLayout
    private lateinit var llContent1: LinearLayout
    private lateinit var llContent2: LinearLayout
    private lateinit var fabOk: FloatingActionButton
    private lateinit var fabError: FloatingActionButton

    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_show)

        bindView()
        bindListener()
        initView()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        flContent = findViewById(R.id.fl_content_record_show)
        llContent1 = findViewById(R.id.ll_content_1_record_show)
        llContent2 = findViewById(R.id.ll_content_2_record_show)
        fabOk = findViewById(R.id.fab_ok_record_show)
        fabError = findViewById(R.id.fab_error_record_show)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        fabOk.setOnClickListener(this)
        fabError.setOnClickListener(this)
    }

    private fun initView() {
        tvTitle.text = getString(R.string.record)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.fab_ok_record_show -> {
                if (!isRunning) {

                }
            }
            R.id.fab_error_record_show -> {
                if (!isRunning) {

                }
            }
        }
    }
}