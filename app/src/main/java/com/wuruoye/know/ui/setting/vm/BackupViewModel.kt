package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.BackupUtil
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.NetUtil
import com.wuruoye.know.util.model.beans.AllTable
import com.wuruoye.know.util.model.beans.BackupInfo
import com.wuruoye.know.util.model.beans.NetResult
import com.wuruoye.know.util.orm.dao.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created at 2019-04-26 15:15 by wuruoye
 * Description:
 */
@Suppress("UNCHECKED_CAST")
class BackupViewModel(
    private val recordDao: RecordDao,
    private val recordTypeDao: RecordTypeDao,
    private val recordItemDao: RecordItemDao,
    private val recordTagDao: RecordTagDao,
    private val reviewStrategyDao: ReviewStrategyDao,
    private val textViewDao: TextViewDao,
    private val imageViewDao: ImageViewDao,
    private val layoutViewDao: LayoutViewDao,
    private val insertDao: InsertDao
) : ViewModel(), IBackupVM {
    override val backupInfo: MutableLiveData<BackupInfo> =
        MutableLiveData()

    override val loadingTitle: MutableLiveData<String> =
        MutableLiveData()

    override val result: MutableLiveData<NetResult> =
        MutableLiveData()

    init {
        updateInfo()
    }

    override fun updateInfo() {
        GlobalScope.launch {
            val result = NetUtil.get(NetUtil.BACKUP_INFO, mapOf())
            if (result.successful) {
                val info = GsonFactory.getInstance()
                    .fromJson(result.data!!, BackupInfo::class.java)
                backupInfo.postValue(info)
            } else {
                this@BackupViewModel.result.postValue(result)
            }
        }
    }

    override fun backup() {
        GlobalScope.launch {
            loadingTitle.postValue("正在读取数据中")
            delay(1000)
            val record = recordDao.queryAll()
            val recordType = recordTypeDao.queryAll()
            val recordItem = recordItemDao.queryAll()
            val recordTag = recordTagDao.queryAll()
            val reviewStrategy = reviewStrategyDao.queryAll()
            val textView = textViewDao.queryAll()
            val imageView = imageViewDao.queryAll()
            val layoutView = layoutViewDao.queryAll()

            val allTable = AllTable(record, recordType, recordItem, recordTag,
                reviewStrategy, textView, imageView, layoutView)
            val result = BackupUtil.dumpTables(allTable)

            loadingTitle.postValue("正在上传数据中")
            delay(1000)
            val res = NetUtil.post(NetUtil.BACKUP, mapOf(Pair("data", result)))
            this@BackupViewModel.result.postValue(res)
        }
    }

    override fun download() {
        GlobalScope.launch {
            loadingTitle.postValue("正在下载数据中")
            delay(1000)
            val result = NetUtil.get(NetUtil.BACKUP, mapOf())
            if (result.successful) {
                val table = BackupUtil.loadTables(result.data!!)

                loadingTitle.postValue("正在存储数据中")
                delay(1000)
                insertDao.beginTransaction()
                try {
                    table.record.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertRecord(it)
                        }
                    }
                    table.recordType.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertRecordType(it)
                        }
                    }
                    table.recordItem.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertRecordItem(it)
                        }
                    }
                    table.recordTag.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertRecordTag(it)
                        }
                    }
                    table.reviewStrategy.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertReviewStrategy(it)
                        }
                    }
                    table.textView.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertTextView(it)
                        }
                    }
                    table.imageView.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertImageView(it)
                        }
                    }
                    table.layoutView.forEach {
                        val exit = recordDao.query(it.id!!)
                        if (exit == null || it.updateTime > exit.updateTime) {
                            insertDao.insertLayoutView(it)
                        }
                    }

                    insertDao.setTransactionSuccessful()
                    this@BackupViewModel.result.postValue(NetResult(200, "ok"))
                } catch (e : Exception) {
                    this@BackupViewModel.result.postValue(NetResult(400, "error in insertion"))
                } finally {
                    insertDao.endTransaction()
                }
            } else {
                this@BackupViewModel.result.postValue(result)
            }
        }
    }

    class Factory(
        private val recordDao: RecordDao,
        private val recordTypeDao: RecordTypeDao,
        private val recordItemDao: RecordItemDao,
        private val recordTagDao: RecordTagDao,
        private val reviewStrategyDao: ReviewStrategyDao,
        private val textViewDao: TextViewDao,
        private val imageViewDao: ImageViewDao,
        private val layoutViewDao: LayoutViewDao,
        private val insertDao: InsertDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BackupViewModel(recordDao, recordTypeDao, recordItemDao, recordTagDao,
                reviewStrategyDao, textViewDao, imageViewDao, layoutViewDao, insertDao) as T
        }
    }
}