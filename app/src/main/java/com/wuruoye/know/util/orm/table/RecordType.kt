package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wuruoye.know.util.model.beans.RealRecordType

/**
 * Created at 2019/4/9 20:10 by wuruoye
 * Description:
 */
@Entity(tableName = "record_type")
class RecordType(
        @PrimaryKey(autoGenerate = true)
        override var id: Long?,
        var title: String,
        var items: String,
        override var createTime: Long,
        override var updateTime: Long
) : BaseTable {
    constructor():
            this("")

    constructor(title: String): this(null, title, "[]", -1, -1)

    constructor(type: RealRecordType, items: String):
            this(type.id, type.title, items, type.createTime, type.updateTime)
}