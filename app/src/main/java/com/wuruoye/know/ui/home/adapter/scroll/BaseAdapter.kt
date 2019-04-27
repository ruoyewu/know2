package com.wuruoye.know.ui.home.adapter.scroll

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-27 11:20 by wuruoye
 * Description:
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>
constructor(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

    private var mLastVH: BaseViewHolder? = null
    private var mActionListener: OnActionListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder as BaseViewHolder) {
            siv.closeDirectly()
            siv.setOnTouchDownListener {
                if (mLastVH != holder) {
                    mLastVH?.siv?.close()
                }
                mLastVH = holder
            }
            siv.setOnClickListener(object : ScrollItemView.OnClickListener {
                override fun onLeftClick() {
                    siv.deleteLeft()
                    onLeft(item)
                }
                override fun onRightClick() {
                    siv.deleteRight()
                    onRight(item)
                }
            })
            siv.setOnScrollListener(object : ScrollItemView.OnScrollListener() {
                override fun onPreLeft() {
                    onLeft(item)
                }
                override fun onPreRight() {
                    onRight(item)
                }
            })
        }
    }

    fun setOnActionListener(listener: OnActionListener<T>) {
        mActionListener = listener
    }

    private fun onLeft(item: T) {
        mLastVH = null
        mActionListener?.onLeft(item)
    }

    private fun onRight(item: T) {
        mLastVH = null
        mActionListener?.onRight(item)
    }

    interface OnActionListener<T> {
        fun onLeft(item: T)
        fun onRight(item: T)
    }
}
