package com.wuruoye.know.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.wuruoye.know.util.orm.table.BaseTable

/**
 * Created at 2019/4/12 16:30 by wuruoye
 * Description:
 */

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun BaseTable.update() {
    if (createTime < 0) {
        createTime = System.currentTimeMillis()
    } else {
        updateTime = System.currentTimeMillis()
    }
}

fun Any.log(msg: String) {
    Log.e(this.javaClass.name, msg)
}