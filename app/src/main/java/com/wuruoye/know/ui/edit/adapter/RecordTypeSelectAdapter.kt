package com.wuruoye.know.ui.edit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.model.beans.RecordTypeSelect

/**
 * Created at 2019/4/11 08:46 by wuruoye
 * Description:
 */
class RecordTypeSelectAdapter :
    ListAdapter<RecordTypeSelect, RecordTypeSelectAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context!!)
            .inflate(R.layout.item_select_type, parent, false))
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
        internal val tv: TextView = itemView.findViewById(R.id.tv_select_type)
    }

    class Callback : DiffUtil.ItemCallback<RecordTypeSelect>() {
        override fun areItemsTheSame(oldItem: RecordTypeSelect, newItem: RecordTypeSelect): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordTypeSelect, newItem: RecordTypeSelect): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(item: RecordTypeSelect)
    }
}