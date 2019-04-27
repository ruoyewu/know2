package com.wuruoye.know.ui.setting.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.home.adapter.scroll.BaseViewHolder
import com.wuruoye.know.util.DensityUtil
import com.wuruoye.know.util.orm.table.BaseTable
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-27 12:14 by wuruoye
 * Description:
 */
abstract class BaseSelectAdapter<T : BaseTable> :
    BaseAdapter<T, BaseSelectAdapter.ViewHolder>(Callback<T>()) {
    private var mOnClickListener: OnClickListener<T>? = null
    private var mSelectable: Boolean = false
    private var mVHs: ArrayList<ViewHolder> = ArrayList()
    private var mSelectSet: HashSet<T> = HashSet()

    abstract fun setContent(holder: ViewHolder, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_type_set, parent, false)
        )
        mVHs.add(vh)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        with(holder) {
            setContent(holder, item)
            tvTitle.setOnClickListener { mOnClickListener?.onClick(item) }

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

    fun setOnClickListener(listener: OnClickListener<T>) {
        mOnClickListener = listener
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

    fun getSelectSet(): HashSet<T> {
        return mSelectSet
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        override val siv: ScrollItemView = view.findViewById(R.id.siv_record_type_set)
        val tvTitle: TextView = view.findViewById(R.id.tv_title_record_type_set)
        val flSelect: FrameLayout = view.findViewById(R.id.fl_select_record_type_set)
        val rbSelect: RadioButton = view.findViewById(R.id.rb_select_record_type_set)
    }

    class Callback<T : BaseTable> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface OnClickListener<T> {
        fun onClick(item: T)
    }
}