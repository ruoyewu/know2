package com.wuruoye.know.util.orm.dao

import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019/4/10 20:18 by wuruoye
 * Description:
 */
class RecordViewDao(
    private val textViewDao: TextViewDao,
    private val layoutViewDao: LayoutViewDao,
    private val imageViewDao: ImageViewDao
) {
    fun insert(view: RecordView): Long {
        return when (view) {
            is RecordTextView -> {
                textViewDao.insert(view)
            }
            is RecordLayoutView -> {
                layoutViewDao.insert(view)
            }
            is RecordImageView -> {
                imageViewDao.insert(view)
            }
            else -> -1L
        }
    }

    fun query(type: Int, id: Long): RecordView? {
        return when(type) {
            RecordTypeSelect.TYPE_TEXT -> {
                textViewDao.query(id)
            }
            RecordTypeSelect.TYPE_LAYOUT -> {
                layoutViewDao.query(id)
            }
            RecordTypeSelect.TYPE_IMG -> {
                imageViewDao.query(id)
            }
            else -> null
        }
    }

    fun delete(type: Int, id: Long): Int {
        return when(type) {
            RecordTypeSelect.TYPE_TEXT -> {
                textViewDao.delete(id)
            }
            RecordTypeSelect.TYPE_LAYOUT -> {
                layoutViewDao.delete(id)
            }
            RecordTypeSelect.TYPE_IMG -> {
                imageViewDao.delete(id)
            }
            else -> 0
        }
    }
}