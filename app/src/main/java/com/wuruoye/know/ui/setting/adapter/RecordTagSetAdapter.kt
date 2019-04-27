package com.wuruoye.know.ui.setting.adapter

import com.wuruoye.know.util.orm.table.RecordTag

/**
 * Created at 2019-04-27 13:26 by wuruoye
 * Description:
 */
class RecordTagSetAdapter : BaseSelectAdapter<RecordTag>() {
    override fun setContent(holder: ViewHolder, item: RecordTag) {
        holder.tvTitle.text = item.title
    }
}