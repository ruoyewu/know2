package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/12 11:25 by wuruoye
 * Description:
 */
@Entity(tableName = "record_tag",
    indices = [Index("title", "createTime")])
class RecordTag(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var title: String,
    var comment: String,
    override var createTime: Long,
    override var updateTime: Long
) : BaseTable, Parcelable {
    constructor(title: String) :
            this(null, title, "", -1, -1)

    constructor():
            this(null, "", "", -1, -1)

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
        writeString(comment)
        writeLong(createTime)
        writeLong(updateTime)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordTag

        if (id != other.id) return false
        if (title != other.title) return false
        if (comment != other.comment) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordTag> = object : Parcelable.Creator<RecordTag> {
            override fun createFromParcel(source: Parcel): RecordTag = RecordTag(source)
            override fun newArray(size: Int): Array<RecordTag?> = arrayOfNulls(size)
        }
    }
}