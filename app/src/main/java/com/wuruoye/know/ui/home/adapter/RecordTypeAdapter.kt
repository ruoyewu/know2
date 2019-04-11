package com.wuruoye.know.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/9 21:46 by wuruoye
 * Description:
 */
class RecordTypeAdapter(private val type: Int) :
    ListAdapter<RecordType, RecordTypeAdapter.ViewHolder>(RecordTypeDiffCallback()) {

    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select_type, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == TYPE_TYPE_LIMIT) {
            val item =
                    if (position == 0) {
                        RecordType("不限")
                    } else {
                        getItem(position-1)
                    }
            holder.tv.text = item.title
            holder.tv.setOnClickListener {
                onClickListener?.onClick(item)
            }
        } else {
            val item =
                    if (position == itemCount-1) {
                        RecordType("点击增加更多类型")
                    } else {
                        getItem(position)
                    }
            holder.tv.text = item.title
            holder.tv.setOnClickListener {
                onClickListener?.onClick(item)
            }
            if (item.id != null) {
                holder.tv.setOnLongClickListener {
                    onLongClickListener?.onLongClick(item)
                    true
                }
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    fun setOnLongClickListener(listener: OnLongClickListener) {
        this.onLongClickListener = listener
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    companion object {
        const val TYPE_TYPE_LIMIT = 1
        const val TYPE_ADD_UPDATE = 2
    }

    interface OnClickListener {
        fun onClick(recordType: RecordType)
    }

    interface OnLongClickListener {
        fun onLongClick(recordType: RecordType)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.tv_select_type)!!
    }

    private class RecordTypeDiffCallback : DiffUtil.ItemCallback<RecordType>() {
        override fun areItemsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return oldItem == newItem
        }
    }
}