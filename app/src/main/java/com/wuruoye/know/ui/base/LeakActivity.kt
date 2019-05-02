package com.wuruoye.know.ui.base

import androidx.appcompat.app.AppCompatActivity
import leakcanary.LeakSentry

/**
 * Created at 2019-05-02 14:38 by wuruoye
 * Description:
 */
abstract class LeakActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        LeakSentry.refWatcher.watch(this)
    }
}