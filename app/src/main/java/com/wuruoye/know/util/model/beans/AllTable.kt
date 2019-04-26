package com.wuruoye.know.util.model.beans

import com.wuruoye.know.util.orm.table.*

/**
 * Created at 2019-04-26 14:35 by wuruoye
 * Description:
 */
class AllTable(
    var record: List<Record>,
    var recordType: List<RecordType>,
    var recordItem: List<RecordItem>,
    var recordTag: List<RecordTag>,
    var reviewStrategy: List<ReviewStrategy>,
    var textView: List<RecordTextView>,
    var imageView: List<RecordImageView>,
    var layoutView: List<RecordLayoutView>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AllTable

        if (record != other.record) return false
        if (recordType != other.recordType) return false
        if (recordItem != other.recordItem) return false
        if (recordTag != other.recordTag) return false
        if (reviewStrategy != other.reviewStrategy) return false
        if (textView != other.textView) return false
        if (imageView != other.imageView) return false
        if (layoutView != other.layoutView) return false

        return true
    }

    override fun hashCode(): Int {
        var result = record.hashCode()
        result = 31 * result + recordType.hashCode()
        result = 31 * result + recordItem.hashCode()
        result = 31 * result + recordTag.hashCode()
        result = 31 * result + reviewStrategy.hashCode()
        result = 31 * result + textView.hashCode()
        result = 31 * result + imageView.hashCode()
        result = 31 * result + layoutView.hashCode()
        return result
    }
}