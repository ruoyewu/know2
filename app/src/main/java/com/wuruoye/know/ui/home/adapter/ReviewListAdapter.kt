package com.wuruoye.know.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.DateUtil
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-19 22:52 by wuruoye
 * Description:
 */
class ReviewListAdapter : ListAdapter<RecordListItem, ReviewListAdapter.ViewHolder>(Callback()){
    private var mLastVH: ViewHolder? = null
    private var mListener: OnActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            // init content
            tvTitle.text = item.title
            tvContent.text = item.content
            tvDate.text = DateUtil.milli2Date(item.record.createTime)

            siv.closeDirectly()
            siv.setOnClickListener {
                when(it.id) {
                    R.id.ll_view_remember -> siv.deleteLeft()
                    R.id.ll_view_not_remember -> siv.deleteRight()
                    R.id.ll_review_list -> mListener?.onClick(item)
                }
            }
            siv.setOnScrollListener(object : ScrollItemView.OnScrollListener {
                override fun onLeft() {
                    mLastVH = null
                    mListener?.onRemember(item)
                }
                override fun onRight() {
                    mLastVH = null
                    mListener?.onNotRemember(item)
                }
            })
        }
    }

    fun setOnActionListener(listener: OnActionListener) {
        mListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val siv: ScrollItemView = view.findViewById(R.id.siv_review_list)
        val tvTitle: TextView = view.findViewById(R.id.tv_title_review_list)
        val tvContent: TextView = view.findViewById(R.id.tv_content_review_list)
        val tvDate: TextView = view.findViewById(R.id.tv_date_review_list)
        val iv: ImageView = view.findViewById(R.id.iv_review_list)

        init {
            siv.setOnTouchDownListener {
                if (mLastVH != this) {
                    mLastVH?.siv?.close()
                }
                mLastVH = this
            }
        }
    }

    class Callback : DiffUtil.ItemCallback<RecordListItem>() {
        override fun areItemsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
            return oldItem.record.id == newItem.record.id
        }

        override fun areContentsTheSame(oldItem: RecordListItem, newItem: RecordListItem): Boolean {
            return oldItem == newItem
        }
    }

    interface OnActionListener {
        fun onClick(item: RecordListItem)
        fun onRemember(item: RecordListItem)
        fun onNotRemember(item: RecordListItem)
    }
}
