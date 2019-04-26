package com.wuruoye.know.util

import android.util.Base64
import com.wuruoye.know.util.model.beans.AllTable
import com.wuruoye.know.util.orm.table.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created at 2019-04-26 10:01 by wuruoye
 * Description:
 */
object BackupUtil {
    private const val RECORD = "record"
    private const val RECORD_TYPE = "record_type"
    private const val RECORD_ITEM = "record_item"
    private const val RECORD_TAG = "record_tag"
    private const val REVIEW_STRATEGY = "review_strategy"
    private const val TEXT_VIEW = "text_view"
    private const val IMAGE_VIEW = "image_view"
    private const val LAYOUT_VIEW = "layout_view"

    private const val ID = "id"
    private const val CONTENT = "content"


    fun loadTables(data: String): AllTable {
        val obj = JSONObject(data)
        val recordList = obj.getJSONArray(RECORD)
        val recordTypeList = obj.getJSONArray(RECORD_TYPE)
        val recordItemList = obj.getJSONArray(RECORD_ITEM)
        val recordTagList = obj.getJSONArray(RECORD_TAG)
        val reviewStrategyList = obj.getJSONArray(REVIEW_STRATEGY)
        val textViewList = obj.getJSONArray(TEXT_VIEW)
        val imageViewList = obj.getJSONArray(IMAGE_VIEW)
        val layoutViewList = obj.getJSONArray(LAYOUT_VIEW)

        val record = loadTables(recordList, Record::class.java)
        val recordType = loadTables(recordTypeList, RecordType::class.java)
        val recordItem = loadTables(recordItemList, RecordItem::class.java)
        val recordTag = loadTables(recordTagList, RecordTag::class.java)
        val reviewStrategy = loadTables(reviewStrategyList, ReviewStrategy::class.java)
        val textView = loadTables(textViewList, RecordTextView::class.java)
        val imageView = loadTables(imageViewList, RecordImageView::class.java)
        val layoutView = loadTables(layoutViewList, RecordLayoutView::class.java)

        return AllTable(record, recordType, recordItem,
            recordTag, reviewStrategy, textView, imageView, layoutView)
    }

    fun dumpTables(data: AllTable): String {
        val obj = JSONObject()
        val record = JSONArray()
        val recordType = JSONArray()
        val recordItem = JSONArray()
        val recordTag = JSONArray()
        val reviewStrategy = JSONArray()
        val textView = JSONArray()
        val imageView = JSONArray()
        val layoutView = JSONArray()

        for (item in data.record) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            record.put(ro)
        }

        for (item in data.recordType) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            recordType.put(ro)
        }

        for (item in data.recordItem) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            recordItem.put(ro)
        }

        for (item in data.recordTag) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            recordTag.put(ro)
        }

        for (item in data.reviewStrategy) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            reviewStrategy.put(ro)
        }

        for (item in data.textView) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            textView.put(ro)
        }

        for (item in data.imageView) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            imageView.put(ro)
        }

        for (item in data.layoutView) {
            val ro = JSONObject().apply {
                put(ID, item.id)
                put(CONTENT, gsonDump(item))
            }
            layoutView.put(ro)
        }

        obj.put(RECORD, record)
        obj.put(RECORD_TYPE, recordType)
        obj.put(RECORD_TAG, recordTag)
        obj.put(RECORD_ITEM, recordItem)
        obj.put(REVIEW_STRATEGY, reviewStrategy)
        obj.put(TEXT_VIEW, textView)
        obj.put(IMAGE_VIEW, imageView)
        obj.put(LAYOUT_VIEW, layoutView)
        return obj.toString()
    }

    private fun <T> loadTables(tableList: JSONArray, clz: Class<T>): ArrayList<T> {
        val result = ArrayList<T>()
        for (i in 0 until tableList.length()) {
            result.add(GsonFactory.getInstance()
                .fromJson(
                    decodeBase64(tableList.getJSONObject(i).getString(CONTENT)),
                    clz
                )
            )
        }
        return result
    }

    private fun gsonDump(obj: Any): String {
        return encodeBase64(GsonFactory.getInstance().toJson(obj))
    }

    private fun encodeBase64(s: String): String {
        return String(Base64.encode(s.toByteArray(), Base64.NO_PADDING))
    }

    private fun decodeBase64(s: String): String {
        return String(Base64.decode(s, Base64.NO_PADDING))
    }
}