package com.wuruoye.know.util.orm

import android.content.Context
import com.wuruoye.know.util.orm.dao.*

/**
 * Created at 2019/4/10 13:28 by wuruoye
 * Description:
 */
object Repository {
    @Volatile private var imageViewDao: ImageViewDao? = null
    @Volatile private var layoutViewDao: LayoutViewDao? = null
    @Volatile private var recordDao: RecordDao? = null
    @Volatile private var recordItemDao: RecordItemDao? = null
    @Volatile private var recordTypeDao: RecordTypeDao? = null
    @Volatile private var textViewDao: TextViewDao? = null
    @Volatile private var recordViewDao: RecordViewDao? = null
    @Volatile private var recordTagDao: RecordTagDao? = null
    @Volatile private var reviewStrategyDao: ReviewStrategyDao? = null
    @Volatile private var insertDao: InsertDao ? = null

    private fun getImageView(context: Context): ImageViewDao {
        if (imageViewDao == null) {
            synchronized(this) {
                if (imageViewDao == null) {
                    imageViewDao = AppDB.getInstance(context).imageView()
                }
            }
        }
        return imageViewDao!!
    }

    private fun getLayoutView(context: Context): LayoutViewDao {
        if (layoutViewDao == null) {
            synchronized(this) {
                if (layoutViewDao == null) {
                    layoutViewDao = AppDB.getInstance(context).layoutView()
                }
            }
        }
        return layoutViewDao!!
    }

    fun getRecord(context: Context): RecordDao {
        if (recordDao == null) {
            synchronized(this) {
                if (recordDao == null) {
                    recordDao = AppDB.getInstance(context).record()
                }
            }
        }
        return recordDao!!
    }

    fun getRecordItem(context: Context): RecordItemDao {
        if (recordItemDao == null) {
            synchronized(this) {
                if (recordItemDao == null) {
                    recordItemDao = AppDB.getInstance(context).recordItem()
                }
            }
        }
        return recordItemDao!!
    }

    fun getRecordType(context: Context): RecordTypeDao {
        if (recordTypeDao == null) {
            synchronized(this) {
                if (recordTypeDao == null) {
                    recordTypeDao = AppDB.getInstance(context).recordType()
                }
            }
        }
        return recordTypeDao!!
    }

    private fun getTextView(context: Context): TextViewDao {
        if (textViewDao == null) {
            synchronized(this) {
                if (textViewDao == null) {
                    textViewDao = AppDB.getInstance(context).textView()
                }
            }
        }
        return textViewDao!!
    }

    fun getRecordView(context: Context): RecordViewDao {
        if (recordViewDao == null) {
            synchronized(this) {
                if (recordViewDao == null) {
                    recordViewDao = RecordViewDao(
                        getTextView(context),
                        getLayoutView(context),
                        getImageView(context))
                }
            }
        }
        return recordViewDao!!
    }

    fun getRecordTag(context: Context): RecordTagDao {
        if (recordTagDao == null) {
            synchronized(this) {
                if (recordTagDao == null) {
                    recordTagDao = AppDB.getInstance(context).recordTag()
                }
            }
        }
        return recordTagDao!!
    }

    fun getReviewStrategy(context: Context): ReviewStrategyDao {
        if (reviewStrategyDao == null) {
            synchronized(this) {
                if (reviewStrategyDao == null) {
                    reviewStrategyDao = AppDB.getInstance(context).reviewStrategy()
                }
            }
        }
        return reviewStrategyDao!!
    }

    fun getInsertDao(context: Context): InsertDao {
        if (insertDao == null) {
            synchronized(this) {
                if (insertDao == null) {
                    insertDao = InsertDao.getInstance(context)
                }
            }
        }
        return insertDao!!
    }
}