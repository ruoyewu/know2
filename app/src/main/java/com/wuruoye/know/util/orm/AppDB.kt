package com.wuruoye.know.util.orm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wuruoye.know.util.orm.dao.*
import com.wuruoye.know.util.orm.table.*

/**
 * Created at 2019/4/9 19:41 by wuruoye
 * Description:
 */
@Database(entities = [
    RecordImageView::class,
    RecordTextView::class,
    RecordLayoutView::class,
    RecordType::class,
    Record::class,
    RecordItem::class,
    RecordTag::class], version = 1)
abstract class AppDB : RoomDatabase(){
    abstract fun imageView(): ImageViewDao
    abstract fun textView(): TextViewDao
    abstract fun layoutView(): LayoutViewDao
    abstract fun recordType(): RecordTypeDao
    abstract fun record(): RecordDao
    abstract fun recordItem(): RecordItemDao
    abstract fun recordTag(): RecordTagDao

    companion object {
        private const val DATABASE_NAME = "com.wuruoye.know.database"
        @Volatile private var sInstance: AppDB? = null

        fun getInstance(context: Context): AppDB {
            if (sInstance == null) {
                synchronized(AppDB::class) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(context.applicationContext,
                                AppDB::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return sInstance!!
        }
    }
}