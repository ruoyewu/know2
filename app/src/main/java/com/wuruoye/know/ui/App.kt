package com.wuruoye.know.ui

import android.app.Application
import com.wuruoye.know.util.base.WConfig
import com.wuruoye.know.util.model.AppCache
import com.wuruoye.know.util.orm.Repository
import com.wuruoye.know.util.orm.table.RecordTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/12 14:14 by wuruoye
 * Description:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        WConfig.init(this)

        checkRecordTag()
    }

    private fun checkRecordTag() {
        GlobalScope.launch {
            val recordTagDao = Repository.getRecordTag(this@App)
            val cache = AppCache.getInstance(this@App)
            if (!cache.initRecordTag) {
                cache.initRecordTag =
                    recordTagDao.insert(
                        RecordTag(0L, "默认",
                            "", System.currentTimeMillis(), -1)
                    ) == 0L
            }
        }
    }
}