package com.wuruoye.know.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.model.beans.TimeLimitItem

/**
 * Created at 2019/4/10 17:03 by wuruoye
 * Description:
 */
class TimeLimitAdapter : ListAdapter<TimeLimitItem, TimeLimitAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context!!)
            .inflate(R.layout.item_select_text, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tv.text = item.title
        holder.tv.setOnClickListener {
            onClickListener?.onClick(item)
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tv: TextView = itemView.findViewById(R.id.tv_select_text)
    }

    interface OnClickListener {
        fun onClick(item: TimeLimitItem)
    }

    class Callback : DiffUtil.ItemCallback<TimeLimitItem>() {
        override fun areItemsTheSame(oldItem: TimeLimitItem, newItem: TimeLimitItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimeLimitItem, newItem: TimeLimitItem): Boolean {
            return oldItem == newItem
        }
    }
}