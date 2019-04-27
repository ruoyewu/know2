package com.wuruoye.know.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.home.adapter.scroll.BaseViewHolder
import com.wuruoye.know.util.DateUtil
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-27 11:36 by wuruoye
 * Description:
 */
class ReviewListAdapter : BaseAdapter<RecordListItem, RecyclerView.ViewHolder>(Callback()) {

    private var mOnClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_NORMAL) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_review_list, parent, false)
            )
        } else {
            TailViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tail, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            super.onBindViewHolder(holder, position)

            val item = getItem(position)
            with(holder as ViewHolder) {
                tvTitle.text = item.title
                tvContent.text = item.content
                tvDate.text = DateUtil.milli2Date(item.record.createTime)

                llContent.setOnClickListener { mOnClickListener?.onClick(item) }
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) TYPE_NORMAL
        else TYPE_TAIL
    }

    fun setOnClickListener(listener: OnClickListener) {
        mOnClickListener = listener
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        override val siv: ScrollItemView = view as ScrollItemView
        val llContent: LinearLayout = view.findViewById(R.id.ll_review_list)
        val tvTitle: TextView = view.findViewById(R.id.tv_title_review_list)
        val tvContent: TextView = view.findViewById(R.id.tv_content_review_list)
        val tvDate: TextView = view.findViewById(R.id.tv_date_review_list)
        val iv: ImageView = view.findViewById(R.id.iv_review_list)
    }

    class TailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view as TextView
    }

    class Callback : DiffUtil.ItemCallback<RecordListItem>() {
        override fun areItemsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
            return oldItem.record.id == newItem.record.id
        }

        override fun areContentsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(item: RecordListItem)
    }

    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_TAIL = 2
    }
}