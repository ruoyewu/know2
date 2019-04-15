package com.wuruoye.know.ui.setting.adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.DensityUtil
import com.wuruoye.know.util.orm.table.RecordType
import com.wuruoye.know.widgets.EventHorizontalScrollView

/**
 * Created at 2019/4/13 15:00 by wuruoye
 * Description:
 */
class RecordTypeSetAdapter :
    ListAdapter<RecordType, RecordTypeSetAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null
    private var mLastMoveVH: ViewHolder? = null
    private var mSelectable: Boolean = false
    private var mVHs: ArrayList<ViewHolder> = ArrayList()
    private var mSelectSet: HashSet<RecordType> = HashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_type_set, parent, false)
        )
        mVHs.add(vh)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            // 基础设置
            tvTitle.text = item.title
            hsv.tag = item
            hsv.scrollTo(0, 0)
            tvTitle.setOnClickListener {
                onClickListener?.onClick(item)
            }
            llDel.setOnClickListener {
                delete()
            }
            rb.setOnClickListener {
                val contain = mSelectSet.contains(item)
                rb.isChecked = !contain
                if (contain) {
                    mSelectSet.remove(item)
                } else {
                    mSelectSet.add(item)
                }
            }

            // 设置是否可以选择，用于多选情况
            if (mSelectable) {
                val lp = flSelect.layoutParams
                lp.width = DensityUtil.dp2px(flSelect.context, 50F).toInt()
                flSelect.layoutParams = lp

                tvTitle.isClickable = false
                hsv.isScrollEnable = false
            } else {
                val lp = flSelect.layoutParams
                lp.width = 0
                flSelect.layoutParams = lp

                tvTitle.isClickable = true
                hsv.isScrollEnable = true
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    /**
     * 设置是否处于选择状态
     */
    fun setSelectable(selectable: Boolean) {
        if (mSelectable != selectable) {
            mSelectable = selectable
//            mSelectSet.clear()
            if (selectable) {
                openSelect()
            } else {
                closeSelect()
            }
        }
    }

    // 获取选择的所有项
    fun getSelectSet(): HashSet<RecordType> {
        return mSelectSet
    }

    private val animator = ValueAnimator().apply {
        duration = 300L
        addUpdateListener {
            val value = it.animatedValue as Int
            for (vh in mVHs) {
                val lp = vh.flSelect.layoutParams
                lp.width = value
                vh.flSelect.layoutParams = lp
            }
        }
    }

    private fun openSelect() {
        if (mVHs.isNotEmpty()) {
            val width = DensityUtil.dp2px(mVHs[0].flSelect.context, 50F)
            animator.setIntValues(0, width.toInt())
            animator.start()

            for (vh in mVHs) {
                with(vh) {
                    close()
                    tvTitle.isClickable = false
                    hsv.isScrollEnable = false
                }
            }
        }
    }

    private fun closeSelect() {
        if (mVHs.isNotEmpty()) {
            animator.setIntValues(mVHs[0].flSelect.width, 0)
            animator.start()

            for (vh in mVHs) {
                with(vh) {
                    tvTitle.isClickable = true
                    hsv.isScrollEnable = true
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hsv: EventHorizontalScrollView = view.findViewById(R.id.hsv_record_type_set)
        val tvTitle : TextView = view.findViewById(R.id.tv_record_type_set)
        val llDel: LinearLayout = view.findViewById(R.id.ll_del_record_type_set)
        val flSelect: FrameLayout = view.findViewById(R.id.fl_select_record_type_set)
        val rb : AppCompatRadioButton = view.findViewById(R.id.rb_record_type_set)
        val v: View = view.findViewById(R.id.v_record_type_set)

        private var maxMove: Int = 0
        private var deleteMove: Int = 0
        private var maxWidth: Int = 0
        private var isOnePass: Boolean = false
        private var moveAdapter = ViewMoveAdapter(llDel)

        init {
            // 初始化各个控件位置、大小
            view.post {
                val width = hsv.width

                var lp = tvTitle.layoutParams
                lp.width = width
                tvTitle.layoutParams = lp
                lp = v.layoutParams
                lp.width = width
                v.layoutParams = lp

                llDel.post { llDel.x = (width).toFloat() }

                maxWidth = width
                deleteMove = width / 2
                maxMove = llDel.width
            }

            // 设置 HSV（HorizontalScrollView）事件监听
            hsv.setOnEventListener { event ->
                if (event.action == MotionEvent.ACTION_UP ||
                    event.action == MotionEvent.ACTION_CANCEL) {
                    val move = hsv.scrollX
                    when {
                        move > deleteMove -> delete()
                        move > maxMove/2 -> open()
                        else -> close()
                    }
                } else if (event.action == MotionEvent.ACTION_DOWN) {
                    isOnePass = false
                    if (mLastMoveVH != null && mLastMoveVH != this) {
                        mLastMoveVH!!.close()
                    }
                }
            }

            hsv.setOnScrollChangedListener { oldX, _, newX, _ ->
                if (!isOnePass) {
                    if (newX < maxMove) {
                        val end = (maxWidth - newX).toFloat()
                        moveAdapter.moveTo(end)
                    } else if (oldX < deleteMove && newX >= deleteMove) {
                        val end = deleteMove.toFloat()
                        moveAdapter.moveTo(end)
                        llDel.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    } else if (newX < deleteMove && oldX >= deleteMove) {
                        val end = (maxWidth - llDel.width).toFloat()
                        moveAdapter.moveTo(end)
                        llDel.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    } else if (newX > deleteMove) {
                        val end = (maxWidth - newX).toFloat()
                        moveAdapter.moveTo(end)
                    }
                } else {
                    val end = (maxWidth - newX).toFloat()
                    moveAdapter.moveTo(end)
                }

                if (newX == maxWidth) {
                    val item = hsv.tag
                    if (item != null) {
                        onClickListener?.onDelClick(item as RecordType)
                    }
                }
            }
        }

        // 关闭右侧
        fun close() {
            hsv.post { hsv.smoothScrollTo(0, 0) }
        }

        // 打开右侧
        fun open() {
            hsv.post { hsv.smoothScrollTo(maxMove, 0) }
            mLastMoveVH = this
        }

        // 执行删除动画
        fun delete() {
            hsv.post { hsv.smoothScrollTo(maxWidth, 0) }
            isOnePass = true
        }
    }

    class Callback : DiffUtil.ItemCallback<RecordType>() {
        override fun areItemsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return false
        }
    }

    interface OnClickListener {
        fun onClick(item: RecordType)
        fun onDelClick(item: RecordType)
    }
}