package com.wuruoye.know.ui.setting.adapter

import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019-04-27 13:28 by wuruoye
 * Description:
 */
class ReviewStrategySetAdapter : BaseSelectAdapter<ReviewStrategy>() {
    override fun setContent(holder: ViewHolder, item: ReviewStrategy) {
        holder.tvTitle.text = item.title
    }
}