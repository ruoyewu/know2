package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 19:28 by wuruoye
 * Description:
 */
@Entity(tableName = "image_view",
    indices = [Index("createTime")])
class RecordImageView(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var shape: Int,
    var blur: Boolean,
    var tint: Int,
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
                null, 0, false, 0, -1, -2, 0, 0,
                0, 0, 0, 0,
                0, 0, -1, -1
            )

    fun setInfo(v: RecordImageView) {
        shape = v.shape
        blur = v.blur
        tint = v.tint

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

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readInt(),
        1 == source.readInt(),
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
        writeInt(shape)
        writeInt((if (blur) 1 else 0))
        writeInt(tint)
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

        other as RecordImageView

        if (id != other.id) return false
        if (shape != other.shape) return false
        if (blur != other.blur) return false
        if (tint != other.tint) return false
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
        result = 31 * result + shape
        result = 31 * result + blur.hashCode()
        result = 31 * result + tint
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
        val CREATOR: Parcelable.Creator<RecordImageView> = object : Parcelable.Creator<RecordImageView> {
            override fun createFromParcel(source: Parcel): RecordImageView = RecordImageView(source)
            override fun newArray(size: Int): Array<RecordImageView?> = arrayOfNulls(size)
        }
    }
}