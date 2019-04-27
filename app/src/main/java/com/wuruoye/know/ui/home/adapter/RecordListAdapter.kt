package com.wuruoye.know.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.home.adapter.scroll.BaseViewHolder
import com.wuruoye.know.util.DateUtil
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019/4/12 10:08 by wuruoye
 * Description:
 */
class RecordListAdapter : BaseAdapter<RecordListItem, RecordListAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_list, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        with(holder) {
            tvTitle.text = "${item.title}-${item.tag}"
            tvContent.text = item.content
            tvDate.text = DateUtil.milli2Date(item.record.createTime)
            if (item.imgPath == null
                || (item.imgPath!!.localPath.isEmpty() && item.imgPath!!.remotePath.isEmpty())) {
                iv.visibility = View.GONE
            } else {
                Glide.with(iv)
                    .load(item.imgPath!!.localPath)
                    .error(
                        Glide.with(iv)
                            .load(item.imgPath!!.remotePath)
                            .addListener(object: RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    iv.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                            })
                    )
                    .into(iv)
            }

            llContent.setOnClickListener {
                onClickListener?.onClick(item)
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    class ViewHolder(itemView: View): BaseViewHolder(itemView) {
        override val siv: ScrollItemView = itemView as ScrollItemView
        val llContent: LinearLayout = itemView.findViewById(R.id.ll_record_list)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_record_list)
        val tvContent: TextView = itemView.findViewById(R.id.tv_content_record_list)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date_record_list)
        val iv: ImageView = itemView.findViewById(R.id.iv_record_list)
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
}