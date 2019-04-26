package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import android.view.Gravity
import android.widget.LinearLayout
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wuruoye.know.util.model.beans.RealRecordLayoutView

/**
 * Created at 2019/4/9 20:05 by wuruoye
 * Description:
 */
@Entity(tableName = "layout_view",
    indices = [Index("createTime")])
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
                null, LinearLayout.HORIZONTAL, "", 0, Gravity.CENTER,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordLayoutView

        if (id != other.id) return false
        if (orientation != other.orientation) return false
        if (items != other.items) return false
        if (bgColor != other.bgColor) return false
        if (gravity != other.gravity) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (marginLeft != other.marginLeft) return false
        if (marginRight != other.marginRight) return false
        if (marginTop != other.marginTop) return false
        if (marginBottom != other.marginBottom) return false
        if (paddingLeft != other.paddingLeft) return false
        if (paddingRight != other.paddingRight) return false
        if (paddingTop != other.paddingTop) return false
        if (paddingBottom != other.paddingBottom) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + orientation
        result = 31 * result + items.hashCode()
        result = 31 * result + bgColor
        result = 31 * result + gravity
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + marginLeft
        result = 31 * result + marginRight
        result = 31 * result + marginTop
        result = 31 * result + marginBottom
        result = 31 * result + paddingLeft
        result = 31 * result + paddingRight
        result = 31 * result + paddingTop
        result = 31 * result + paddingBottom
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordLayoutView> = object : Parcelable.Creator<RecordLayoutView> {
            override fun createFromParcel(source: Parcel): RecordLayoutView = RecordLayoutView(source)
            override fun newArray(size: Int): Array<RecordLayoutView?> = arrayOfNulls(size)
        }
    }
}