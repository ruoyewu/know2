package com.wuruoye.know.ui.edit.controller

import android.content.Context
import android.widget.FrameLayout
import android.widget.ScrollView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/3/18 16:42 by wuruoye
 * Description:
 */
interface EditorController {
    val result: RecordView
    fun attach(context: Context, fl: FrameLayout, sv: ScrollView)
    fun recycler()
}
