package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import android.view.Gravity
import android.widget.LinearLayout
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wuruoye.know.util.model.beans.RealRecordLayoutView

/**
 * Created at 2019/4/9 20:05 by wuruoye
 * Description:
 */
@Entity(tableName = "layout_view")
class RecordLayoutView(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var orientation: Int,
    var items: String,
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
) : RecordView, Parcelable {
    constructor() :
            this(
                null, LinearLayout.HORIZONTAL, "", 0, Gravity.CENTER_VERTICAL,
                -1, -2, 0, 0, 0, 0,
                16, 16, 10, 10,
                -1, -1
            )

    constructor(v: RealRecordLayoutView, items: String):
            this(v.id, v.orientation, items, v.bgColor, v.gravity, v.width, v.height, v.marginLeft,
                v.marginRight, v.marginTop, v.marginBottom, v.paddingLeft, v.paddingRight,
                v.paddingTop, v.paddingBottom, v.createTime, v.updateTime)

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readInt(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeInt(orientation)
        writeString(items)
        writeInt(bgColor)
        writeInt(gravity)
        writeInt(width)
        writeInt(height)
        writeInt(marginLeft)
        writeInt(marginRight)
        writeInt(marginTop)
        writeInt(marginBottom)
        writeInt(paddingLeft)
        writeInt(paddingRight)
        writeInt(paddingTop)
        writeInt(paddingBottom)
        writeLong(createTime)
        writeLong(updateTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordLayoutView> = object : Parcelable.Creator<RecordLayoutView> {
            override fun createFromParcel(source: Parcel): RecordLayoutView = RecordLayoutView(source)
            override fun newArray(size: Int): Array<RecordLayoutView?> = arrayOfNulls(size)
        }
    }
}