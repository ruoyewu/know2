package com.wuruoye.know.util.model.beans

import android.annotation.SuppressLint
import android.os.Parcel
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/10 20:56 by wuruoye
 * Description:
 */
@SuppressLint("ParcelCreator")
class RealRecordLayoutView(
    override var id: Long?,
    var orientation: Int,
    var items: ArrayList<RecordView>,
    var bgColor: Int,
    var gravity: Int,
    override var width: Int,
    override var height: Int,
    override var marginLeft: Int,
    override var marginRight: Int,
    override var marginTop: Int,
    override var marginBottom: Int,
    override var paddingLeft: Int,
    override var paddingRight: Int,
    override var paddingTop: Int,
    override var paddingBottom: Int,
    override var createTime: Long,
    override var updateTime: Long
) : RecordView {

    constructor(view: RecordLayoutView, views: ArrayList<RecordView>):
            this(view.id, view.orientation, views, view.bgColor, view.gravity,
                view.width, view.height, view.marginLeft, view.marginRight, view.marginTop,
                view.marginBottom, view.paddingLeft, view.paddingRight, view.paddingTop,
                view.paddingBottom, view.createTime, view.updateTime)

    fun setInfo(v: RecordLayoutView) {
        orientation = v.orientation
        bgColor = v.bgColor
        gravity = v.gravity
        width = v.width
        height = v.height
        marginLeft = v.marginLeft
        marginRight = v.marginRight
        marginTop = v.marginTop
        marginBottom = v.marginBottom
        paddingLeft = v.paddingLeft
        paddingRight = v.paddingRight
        paddingTop = v.paddingTop
        paddingBottom = v.paddingBottom
        createTime = v.createTime
        updateTime = v.updateTime
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
}