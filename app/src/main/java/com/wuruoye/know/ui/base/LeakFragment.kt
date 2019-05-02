package com.wuruoye.know.ui.base

import androidx.fragment.app.Fragment
import leakcanary.LeakSentry

/**
 * Created at 2019-05-02 14:38 by wuruoye
 * Description:
 */
abstract class LeakFragment : Fragment() {

    override fun onDestroy() {
        super.onDestroy()
        LeakSentry.refWatcher.watch(this)
    }
}