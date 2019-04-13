package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:16 by wuruoye
 * Description:
 */
@Entity(tableName = "record",
    indices = [Index("createTime", "type", "tag")])
class Record(
        @PrimaryKey(autoGenerate = true)
        override var id: Long?,
        var type: Long,
        var reviewNum: Int,
        var failNum: Int,
        var lastReview: Long,
        var tag: Long,
        override var createTime: Long,
        override var updateTime: Long
) : BaseTable {
    constructor(type: Long):
            this(null, type, 0, 0,
                -1, -1, -1, -1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Record

        if (id != other.id) return false
        if (type != other.type) return false
        if (reviewNum != other.reviewNum) return false
        if (failNum != other.failNum) return false
        if (lastReview != other.lastReview) return false
        if (tag != other.tag) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + reviewNum
        result = 31 * result + failNum
        result = 31 * result + lastReview.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }


}