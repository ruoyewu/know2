package com.wuruoye.know.ui.setting.adapter

import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019-04-27 13:27 by wuruoye
 * Description:
 */
class RecordTypeSetAdapter : BaseSelectAdapter<RecordType>() {
    override fun setContent(holder: ViewHolder, item: RecordType) {
        holder.tvTitle.text = item.title
    }
}