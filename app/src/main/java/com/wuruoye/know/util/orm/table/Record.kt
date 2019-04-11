package com.wuruoye.know.util.orm.table

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:16 by wuruoye
 * Description:
 */
@Entity(tableName = "record")
class Record(
        @PrimaryKey(autoGenerate = true)
        override var id: Long?,
        var type: Long,
        var reviewNum: Int,
        var failNum: Int,
        var lastReview: Long,
        override var createTime: Long,
        override var updateTime: Long
) : BaseTable