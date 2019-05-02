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
    var gapTime: Int,
    override var createTime: Long,
    override var updateTime: Long
) : BaseTable, Parcelable {
    constructor() :
            this(null, "", 3, 5, -1, -1)

    constructor(title: String) :
            this(null, title, 0, 0, -1, -1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReviewStrategy

        if (id != other.id) return false
        if (title != other.title) return false
        if (rememberTime != other.rememberTime) return false
        if (gapTime != other.gapTime) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + rememberTime
        result = 31 * result + gapTime.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
        writeInt(rememberTime)
        writeInt(gapTime)
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