package com.wuruoye.know.ui.home.adapter.scroll

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.widgets.scrollview.ScrollItemView

/**
 * Created at 2019-04-27 11:17 by wuruoye
 * Description:
 */
abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract val siv: ScrollItemView

    fun close() {
        siv.close()
    }

    fun openLeft() {
        siv.openLeft()
    }

    fun openRight() {
        siv.openRight()
    }
}