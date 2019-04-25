package com.wuruoye.know.ui.edit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.vm.IRecordShowVM
import com.wuruoye.know.ui.edit.vm.RecordShowViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.toast

/**
 * Created at 2019-04-24 18:26 by wuruoye
 * Description:
 */
class RecordShowActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var flContent: FrameLayout
    private lateinit var llNext: LinearLayout
    private lateinit var llCur: LinearLayout
    private lateinit var fabOk: FloatingActionButton
    private lateinit var fabError: FloatingActionButton

    private lateinit var vm: IRecordShowVM
    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_show)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordShowViewModelFactory(this))
            .get(RecordShowViewModel::class.java)

        vm.setItemList(
            intent.getParcelableArrayListExtra<RecordListItem>(RECORD_LIST)
                    as ArrayList<RecordListItem>,
            intent.getIntExtra(RECORD_POSITION, 0)
        )

        bindView()
        bindListener()
        initView()
        subscribeUI()

    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        flContent = findViewById(R.id.fl_content_record_show)
        llNext = findViewById(R.id.ll_content_1_record_show)
        llCur = findViewById(R.id.ll_content_2_record_show)
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

    private fun subscribeUI() {
        vm.defaultShow.observe(this, Observer {
            val cur = it[0]
            val next = if (it.size > 1) it[1] else null

            llCur.tag = cur.record
            ViewFactory.generateView(this, cur, llCur, isShow = true)
            if (next != null) {
                llNext.tag = next.record
                ViewFactory.generateView(this, next, llNext, isShow = true)
            } else {
                flContent.removeView(llNext)
            }
        })
        vm.recordShow.observe(this, Observer {
            llNext = it.viewGroup as LinearLayout
            flContent.addView(llNext, 0)
            llNext.tag = it.recordShow.record
            ViewFactory.generateView(this, it.recordShow, llNext, isShow = true)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.fab_ok_record_show -> {
                if (!isRunning) {
                    if (hasContent()) {
                        startAnimation(fabOk)
                        vm.rememberRecord(llCur.tag as Record, true)
                    } else {
                        toast("no content")
                    }
                }
            }
            R.id.fab_error_record_show -> {
                if (!isRunning) {
                    if (hasContent()) {
                        startAnimation(fabError)
                        vm.rememberRecord(llCur.tag as Record, false)
                    } else {
                        toast("no content")
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun startAnimation(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val x = view.x + view.width / 2
            val y = view.y + view.width / 2
            val animator = ViewAnimationUtils.createCircularReveal(llCur,
                x.toInt(), y.toInt(),
                llCur.height.toFloat(), 0F)
            animator.duration = DURATION
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    isRunning = true
                }
                override fun onAnimationEnd(animation: Animator?) {
                    removeCur()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    removeCur()
                }
            })
            animator.start()
        } else {
            removeCur()
        }
    }

    private fun removeCur() {
        isRunning = false
        llCur.removeAllViews()
        flContent.removeView(llCur)
        vm.showInViewGroup(llCur)
        llCur = llNext
    }

    private fun hasContent(): Boolean = flContent.childCount > 0

    companion object {
        val RECORD_POSITION = "record_position"
        val RECORD_LIST = "record_list"
        val DURATION = 800L
    }
}