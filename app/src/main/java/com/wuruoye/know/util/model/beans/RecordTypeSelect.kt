package com.wuruoye.know.util.model.beans

import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/10 20:00 by wuruoye
 * Description:
 */
class RecordTypeSelect(
    var id: Int,
    var title: String
) {
    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_EDIT = 2
        const val TYPE_IMG = 3
        const val TYPE_LAYOUT = 4

        fun getType(recordView: RecordView): Int {
            return when (recordView) {
                is RecordTextView -> {
                    if (recordView.editable)
                        TYPE_EDIT
                    else
                        TYPE_TEXT
                }
                is RecordLayoutView -> TYPE_LAYOUT
                is RecordImageView -> TYPE_IMG
                else -> 0
            }
        }
    }
}