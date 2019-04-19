package com.wuruoye.know.ui.setting.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.DensityUtil
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-19 22:17 by wuruoye
 * Description:
 */
class RecordTagSetAdapter :
    ListAdapter<RecordTag, RecordTagSetAdapter.ViewHolder>(Callback()){

    private var onClickListener: OnClickListener? = null
    private var mLastMoveVH: ViewHolder? = null
    private var mSelectable: Boolean = false
    private var mVHs: ArrayList<ViewHolder> = ArrayList()
    private var mSelectSet: HashSet<RecordTag> = HashSet()

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
            tvTitle.text = item.title

            siv.closeDirectly()
            siv.setOnClickListener {
                when(it.id) {
                    R.id.tv_title_record_type_set -> onClickListener?.onClick(item)
                    R.id.ll_view_del -> siv.deleteRight()
                }
            }
            siv.setOnScrollListener(object : ScrollItemView.OnScrollListener {
                override fun onLeft() {

                }
                override fun onRight() {
                    if (mLastMoveVH == holder) {
                        mLastMoveVH = null
                    }
                    onClickListener?.onDelClick(item)
                }
            })

            rbSelect.setOnClickListener {
                val contain = mSelectSet.contains(item)
                rbSelect.isChecked = !contain
                if (contain) {
                    mSelectSet.remove(item)
                } else {
                    mSelectSet.add(item)
                }
            }

            if (mSelectable) {
                val lp = flSelect.layoutParams
                lp.width = DensityUtil.dp2px(flSelect.context, 50F).toInt()
                flSelect.layoutParams = lp

                tvTitle.isClickable = false
                siv.isScrollable = false
            } else {
                val lp = flSelect.layoutParams
                lp.width = 0
                flSelect.layoutParams = lp

                tvTitle.isClickable = true
                siv.isScrollable = true
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    fun setSelectable(selectable: Boolean) {
        if (mSelectable != selectable) {
            mSelectable = selectable
            if (selectable) {
                openSelect()
            } else {
                closeSelect()
            }
        }
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
                    siv.isScrollable = false
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
                    siv.isScrollable = true
                }
            }
        }
    }

    fun getSelectSet(): HashSet<RecordTag> {
        return mSelectSet
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val siv: ScrollItemView = view.findViewById(R.id.siv_record_type_set)
        val tvTitle: TextView = view.findViewById(R.id.tv_title_record_type_set)
        val flSelect: FrameLayout = view.findViewById(R.id.fl_select_record_type_set)
        val rbSelect: RadioButton = view.findViewById(R.id.rb_select_record_type_set)

        init {
            siv.setOnTouchDownListener {
                if (mLastMoveVH != this) {
                    mLastMoveVH?.siv?.close()
                }
                mLastMoveVH = this
            }
        }

        fun close() {
            siv.close()
        }

        fun open() {
            siv.openRight()
        }

        fun delete() {
            siv.deleteRight()
        }
    }

    class Callback : DiffUtil.ItemCallback<RecordTag>() {
        override fun areItemsTheSame(oldItem: RecordTag, newItem: RecordTag): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordTag, newItem: RecordTag): Boolean {
            return false
        }
    }

    interface OnClickListener {
        fun onClick(item: RecordTag)
        fun onDelClick(item: RecordTag)
    }
}