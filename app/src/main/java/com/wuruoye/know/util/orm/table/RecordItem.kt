package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:27 by wuruoye
 * Description:
 */
@Entity(tableName = "record_item",
    indices = [Index("createTime", "recordId", "type", "typeId")])
class RecordItem(
        @PrimaryKey(autoGenerate = true)
        override var id: Long?,
        var recordId: Long,
        var type: Int,
        var typeId: Long,
        var content: String,
        override var createTime: Long,
        override var updateTime: Long
) : BaseTable {
    constructor():
            this(null, -1, -1, -1, "", -1, -1)

    constructor(recordId: Long, typeId: Long, type: Int):
            this(null, recordId, type, typeId, "", -1, -1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordItem

        if (id != other.id) return false
        if (recordId != other.recordId) return false
        if (type != other.type) return false
        if (typeId != other.typeId) return false
        if (content != other.content) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + recordId.hashCode()
        result = 31 * result + type
        result = 31 * result + typeId.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }
}