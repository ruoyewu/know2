package com.wuruoye.know.ui.edit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
import androidx.core.app.ActivityCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.vm.IRecordShowVM
import com.wuruoye.know.ui.edit.vm.RecordShowViewModel
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019-04-24 18:26 by wuruoye
 * Description:
 */
class RecordShowActivity : AppCompatActivity(),
    View.OnClickListener, ViewFactory.OnClickListener{
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var flContent: FrameLayout
    private lateinit var llNext: LinearLayout
    private lateinit var llCur: LinearLayout
    private lateinit var fabOk: FloatingActionButton
    private lateinit var fabError: FloatingActionButton
    private lateinit var fabReview: FloatingActionButton
    private lateinit var tvInfo: TextView

    private lateinit var vm: IRecordShowVM
    private var isRunning: Boolean = false

    private var mStartColor: Int = 0
    private var mEndColor: Int = 0
    private lateinit var mArgbEvaluator: ArgbEvaluator
    private lateinit var mValueAnimator: ValueAnimator
    private lateinit var mOkSpring: SpringAnimation
    private lateinit var mErrorSpring: SpringAnimation
    private lateinit var mReviewSpring: SpringAnimation

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

        flContent.post { initAnimator() }
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        flContent = findViewById(R.id.fl_content_record_show)
        llNext = findViewById(R.id.ll_content_1_record_show)
        llCur = findViewById(R.id.ll_content_2_record_show)
        fabOk = findViewById(R.id.fab_ok_record_show)
        fabError = findViewById(R.id.fab_error_record_show)
        fabReview = findViewById(R.id.fab_review_record_show)
        tvInfo = findViewById(R.id.tv_info_record_show)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        fabOk.setOnClickListener(this)
        fabError.setOnClickListener(this)
        fabReview.setOnClickListener(this)
    }

    private fun initAnimator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStartColor = ActivityCompat.getColor(this, R.color.romance)
            mArgbEvaluator = ArgbEvaluator()
            mValueAnimator = ValueAnimator().apply {
                duration = DURATION
                setFloatValues(0F, 1F)
                addUpdateListener {
                    val value = it.animatedValue as Float
                    llCur.setBackgroundColor(mArgbEvaluator
                        .evaluate(value, mStartColor, mEndColor) as Int)
                }
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        llCur.setBackgroundColor(mStartColor)
                    }
                })
            }

            mOkSpring = SpringAnimation(fabOk,
                SpringAnimation.SCALE_X, 1F).apply {
                addUpdateListener { _, value, _ ->
                    fabOk.scaleX = value
                    fabOk.scaleY = value
                }
                spring.apply {
                    stiffness = SpringForce.STIFFNESS_LOW
                    dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY * 1.5F
                }
                addEndListener { _,
                                 _, _, _ ->
                    removeCur()
                }
            }
            mErrorSpring = SpringAnimation(fabError,
                SpringAnimation.SCALE_X, 1F).apply {
                addUpdateListener { _, value, _ ->
                    fabError.scaleX = value
                    fabError.scaleY = value
                }
                spring.apply {
                    stiffness = SpringForce.STIFFNESS_LOW
                    dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY * 1.5F
                }
                addEndListener { _,
                                 _, _, _ ->
                    removeCur()
                }
            }
            mReviewSpring = SpringAnimation(fabReview,
                SpringAnimation.SCALE_X, 1F).apply {
                addUpdateListener { _, value, _ ->
                    fabReview.scaleX = value
                    fabReview.scaleY = value
                }
                spring.apply {
                    stiffness = SpringForce.STIFFNESS_LOW
                    dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY * 1.5F
                }
                addEndListener { _,
                                 _, _, _ ->
                    removeCur()
                }
            }
        }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.record)
        ivBack.setImageResource(R.drawable.ic_left)
    }

    private fun subscribeUI() {
        vm.defaultShow.observe(this, Observer {
            val cur = it[0]
            val next = if (it.size > 1) it[1] else null

            llCur.tag = cur.record
            ViewFactory.generateView(this, cur, llCur, listener = this, isShow = true)
            if (next != null) {
                llNext.tag = next.record
                ViewFactory.generateView(this, next, llNext, listener = this, isShow = true)
            } else {
                flContent.removeView(llNext)
            }

            onRecordShow()
        })
        vm.recordShow.observe(this, Observer {
            llNext = it.viewGroup as LinearLayout
            llNext.visibility = View.VISIBLE
            flContent.addView(llNext, 0)
            llNext.tag = it.recordShow.record
            ViewFactory.generateView(this, it.recordShow, llNext, listener = this, isShow = true)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> onBackPressed()
            R.id.fab_ok_record_show -> {
                if (!isRunning && hasContent()) {
                    startAnimation(fabOk)
                    vm.rememberRecord(llCur.tag as Record, true)
                }
            }
            R.id.fab_error_record_show -> {
                if (!isRunning && hasContent()) {
                    startAnimation(fabError)
                    vm.rememberRecord(llCur.tag as Record, false)
                }
            }
            R.id.fab_review_record_show -> {
                if (!isRunning && hasContent()) {
                    startAnimation(fabReview)
                    vm.rememberRecord(llCur.tag as Record)
                }
            }
        }
    }

    override fun onClick(recordView: RecordView, view: View) {
        if (recordView is RecordImageView) {
            val item = view.getTag(R.id.tag_image)
            if (item != null && item is RecordItem) {
                val path = GsonFactory.sInstance
                    .fromJson(item.content, ImagePath::class.java)
                if (path.localPath.isNotEmpty() || path.remotePath.isNotEmpty()) {
                    ImgShowActivity.show(this, path)
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
            mEndColor =
                when (view) {
                    fabOk -> ActivityCompat.getColor(this, R.color.light_slate_gray)
                    fabError -> ActivityCompat.getColor(this, R.color.monsoon)
                    else -> ActivityCompat.getColor(this, R.color.platinum)
                }
            ViewAnimationUtils.createCircularReveal(llCur,
                (view.x + view.width/2).toInt(), (view.y + view.width/2).toInt(),
                llCur.height.toFloat(), (view.width/2).toFloat()
            ).apply {
                duration = DURATION
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        this@RecordShowActivity.isRunning = true
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        llCur.visibility = View.GONE
                        when (view) {
                            fabOk -> mOkSpring
                            fabError -> mErrorSpring
                            else -> mReviewSpring
                        }.setStartVelocity(3F).start()
                    }
                })
                start()
                mValueAnimator.start()
            }
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
        onRecordShow()
    }

    @SuppressLint("SetTextI18n")
    private fun onRecordShow() {
        val record = llCur.tag
        if (record != null && record is Record) {
            tvInfo.text = "复习次数: ${record.reviewNum}, " +
                    "记住次数: ${record.remNum}, 未记住次数: ${record.failNum}"
        }
    }

    private fun hasContent(): Boolean = flContent.childCount > 0

    companion object {
        const val RECORD_POSITION = "record_position"
        const val RECORD_LIST = "record_list"
        private const val DURATION = 1000L
    }
}