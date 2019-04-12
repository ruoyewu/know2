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

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordImageView> = object : Parcelable.Creator<RecordImageView> {
            override fun createFromParcel(source: Parcel): RecordImageView = RecordImageView(source)
            override fun newArray(size: Int): Array<RecordImageView?> = arrayOfNulls(size)
        }
    }
}