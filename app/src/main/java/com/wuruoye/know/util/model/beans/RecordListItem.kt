package com.wuruoye.know.util.model.beans

import com.wuruoye.know.util.orm.table.Record

/**
 * Created at 2019/4/12 10:11 by wuruoye
 * Description:
 */
class RecordListItem(
    var record: Record,
    var title: String,
    var tag: String,
    var content: String?,
    var imgPath: ImagePath?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordListItem

        if (record != other.record) return false
        if (title != other.title) return false
        if (tag != other.tag) return false
        if (content != other.content) return false
        if (imgPath != other.imgPath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = record.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (imgPath?.hashCode() ?: 0)
        return result
    }
}