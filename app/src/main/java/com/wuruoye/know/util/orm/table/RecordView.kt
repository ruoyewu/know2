package com.wuruoye.know.util.orm.table

import android.os.Parcelable

/**
 * Created at 2019/4/9 19:37 by wuruoye
 * Description:
 */
interface RecordView : BaseTable , Parcelable{
    override var id: Long?
    var width: Int
    var height: Int
    var marginLeft: Int
    var marginRight: Int
    var marginTop: Int
    var marginBottom: Int
    var paddingTop: Int
    var paddingBottom: Int
    var paddingLeft: Int
    var paddingRight: Int
    override var createTime: Long
    override var updateTime: Long
}