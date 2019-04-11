package com.wuruoye.know.util.orm.table

/**
 * Created at 2019/4/9 20:11 by wuruoye
 * Description:
 */
interface BaseTable {
    var id: Long?
    var createTime: Long
    var updateTime: Long
}