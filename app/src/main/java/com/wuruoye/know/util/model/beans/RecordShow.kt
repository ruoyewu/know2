package com.wuruoye.know.util.model.beans

import android.util.ArrayMap
import com.wuruoye.know.util.orm.table.RecordItem

/**
 * Created at 2019-04-24 23:02 by wuruoye
 * Description:
 */
class RecordShow(
    var recordType: RealRecordType,
    var recordData: ArrayMap<String, RecordItem>? = null
)