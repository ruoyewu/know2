package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created at 2019-04-20 14:36 by wuruoye
 * Description:
 */
@Entity(tableName = "review_strategy")
class ReviewStrategy(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var title: String,
    var rememberTime: Int,
    var gapTime: Long,
    override var createTime: Long,
    override var updateTime: Long
) : BaseTable, Parcelable {
    constructor() :
            this(null, "", 3, 86400000, -1, -1)

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readInt(),
        source.readLong(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
        writeInt(rememberTime)
        writeLong(gapTime)
        writeLong(createTime)
        writeLong(updateTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReviewStrategy> = object : Parcelable.Creator<ReviewStrategy> {
            override fun createFromParcel(source: Parcel): ReviewStrategy = ReviewStrategy(source)
            override fun newArray(size: Int): Array<ReviewStrategy?> = arrayOfNulls(size)
        }
    }
}