package com.wuruoye.know.util.orm.table

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:16 by wuruoye
 * Description:
 */
@Entity(
    tableName = "record",
    indices = [Index("createTime", "type", "tag")]
)
class Record(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var type: Long,
    var reviewNum: Int,
    var remNum: Int,
    var failNum: Int,
    var lastReview: Long,
    var lastRemReview: Long,
    var lastFailReview: Long,
    var tag: Long,
    override var createTime: Long,
    override var updateTime: Long
) : BaseTable, Parcelable {
    constructor(type: Long) :
            this(
                null, type, 0, 0, 0,
                -1, -1, -1, 0, -1, -1
            )

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readLong(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeLong(type)
        writeInt(reviewNum)
        writeInt(remNum)
        writeInt(failNum)
        writeLong(lastReview)
        writeLong(lastRemReview)
        writeLong(lastFailReview)
        writeLong(tag)
        writeLong(createTime)
        writeLong(updateTime)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Record

        if (id != other.id) return false
        if (type != other.type) return false
        if (reviewNum != other.reviewNum) return false
        if (remNum != other.remNum) return false
        if (failNum != other.failNum) return false
        if (lastReview != other.lastReview) return false
        if (lastRemReview != other.lastRemReview) return false
        if (lastFailReview != other.lastFailReview) return false
        if (tag != other.tag) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + reviewNum
        result = 31 * result + remNum
        result = 31 * result + failNum
        result = 31 * result + lastReview.hashCode()
        result = 31 * result + lastRemReview.hashCode()
        result = 31 * result + lastFailReview.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Record> = object : Parcelable.Creator<Record> {
            override fun createFromParcel(source: Parcel): Record = Record(source)
            override fun newArray(size: Int): Array<Record?> = arrayOfNulls(size)
        }
    }
}