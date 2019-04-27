package com.wuruoye.know.ui.edit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019-04-25 14:59 by wuruoye
 * Description:
 */
class ReviewStrategyAdapter :
    ListAdapter<ReviewStrategy, ReviewStrategyAdapter.ViewHolder>(Callback()){

    private var mListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_select_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = if (position < itemCount-1) getItem(position)
                            else ReviewStrategy("点击增加复习策略")
        with(holder) {
            tv.text = item.title

            tv.setOnClickListener { mListener?.onClick(item) }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv_select_text)
    }

    class Callback : DiffUtil.ItemCallback<ReviewStrategy>() {
        override fun areItemsTheSame(oldItem: ReviewStrategy, newItem: ReviewStrategy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReviewStrategy, newItem: ReviewStrategy): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(item: ReviewStrategy)
    }
}