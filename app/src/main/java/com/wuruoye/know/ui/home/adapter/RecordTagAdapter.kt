package com.wuruoye.know.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019/4/12 13:51 by wuruoye
 * Description:
 */
class RecordTagAdapter(
    private val limit: Boolean = true
) : ListAdapter<RecordTag, RecordTagAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =
            if (limit) {
                if (position == 0) RecordTag("不限")
                else getItem(position-1)
            } else {
                if (position < itemCount-1) {
                    getItem(position)
                } else {
                    RecordTag("点击增加标签")
                }
            }
        with(holder) {
            tv.text = item.title
            tv.setOnClickListener {
                onClickListener?.onClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv_select_text)
    }

    class Callback : DiffUtil.ItemCallback<RecordTag>() {
        override fun areItemsTheSame(oldItem: RecordTag, newItem: RecordTag): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordTag, newItem: RecordTag): Boolean {
            return oldItem == newItem
        }

    }

    interface OnClickListener {
        fun onClick(item: RecordTag)
    }
}