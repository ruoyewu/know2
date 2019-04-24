package com.wuruoye.know.util.model.beans

/**
 * Created at 2019-04-23 19:30 by wuruoye
 * Description:
 */
class NetResult(
    var res: Int,
    var msg: String? = null,
    val data: String? = null
) {
    val successful: Boolean
        get() = res == 200
}