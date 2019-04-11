package com.wuruoye.know.widgets

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.wuruoye.know.R

/**
 * Created at 2019/3/18 23:02 by wuruoye
 * Description:
 */
open class BottomDialog(context: Context, view: View) : Dialog(context, R.style.BottomDlgTheme) {
    init {
        setContentView(view)
        val window = window
        if (window != null) {
            window.decorView.setPadding(0, 0, 0, 0)
            val lp = window.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
            window.setGravity(Gravity.BOTTOM)
            window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
        }
    }
}
