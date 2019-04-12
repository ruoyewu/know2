package com.wuruoye.know.util.model.beans

import com.wuruoye.know.util.orm.table.Record

/**
 * Created at 2019/4/12 10:11 by wuruoye
 * Description:
 */
class RecordListItem(
    var record: Record,
    var title: String,
    var content: String?,
    var imgPath: ImagePath?
)