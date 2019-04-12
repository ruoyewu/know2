package com.wuruoye.know.util.model.beans

/**
 * Created at 2019/4/12 16:26 by wuruoye
 * Description:
 */
data class Result<T : Any?>(
    var result: Boolean,
    var msg: String,
    var obj: T?
) {
    constructor(result: Boolean, msg: String):
            this(result, msg, null)
}