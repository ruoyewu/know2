package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:27 by wuruoye
 * Description:
 */
@Entity(tableName = "record_item")
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
}