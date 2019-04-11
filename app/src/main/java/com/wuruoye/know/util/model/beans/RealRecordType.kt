package com.wuruoye.know.util.model.beans

import com.wuruoye.know.util.orm.table.BaseTable
import com.wuruoye.know.util.orm.table.RecordType
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/10 20:56 by wuruoye
 * Description:
 */
class RealRecordType(
    override var id: Long?,
    var title: String,
    var items: ArrayList<RecordView>,
    override var createTime: Long,
    override var updateTime: Long
) : BaseTable {
    constructor(title: String): this(null, title, arrayListOf(), -1, -1)

    constructor(type: RecordType, views: ArrayList<RecordView>):
            this(type.id, type.title, views, type.createTime, type.updateTime)
}