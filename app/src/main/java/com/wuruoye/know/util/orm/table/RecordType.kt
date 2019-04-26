package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wuruoye.know.util.model.beans.RealRecordType

/**
 * Created at 2019/4/9 20:10 by wuruoye
 * Description:
 */
@Entity(tableName = "record_type",
    indices = [Index("createTime")])
class RecordType(
        @PrimaryKey(autoGenerate = true)
        override var id: Long?,
        var title: String,
        var items: String,
        var strategy: Long,
        override var createTime: Long,
        override var updateTime: Long
) : BaseTable {
    constructor():
            this("")

    constructor(title: String): this(null, title, "[]", 0, -1, -1)

    constructor(type: RealRecordType, items: String):
            this(type.id, type.title, items, type.strategy, type.createTime, type.updateTime)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordType

        if (id != other.id) return false
        if (title != other.title) return false
        if (items != other.items) return false
        if (strategy != other.strategy) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + items.hashCode()
        result = 31 * result + strategy.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }
}