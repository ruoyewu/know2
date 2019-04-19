package com.wuruoye.know.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.model.beans.Result
import com.wuruoye.know.util.orm.dao.RecordTagDao
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/12 16:32 by wuruoye
 * Description:
 */
class RecordTagEditViewModel(
    private val recordTagDao: RecordTagDao
) : ViewModel(), IRecordTagEditVM {
    override var submitResult: MutableLiveData<Result<RecordTag>> = MutableLiveData()

    override fun saveRecordTag(recordTag: RecordTag?, title: String, comment: String) {
        GlobalScope.launch {
            if (recordTag == null && recordTagDao.queryByTitle(title) != null) {
                submitResult.postValue(Result(false, "已有同名标签"))
            } else {
                val tag = recordTag ?: RecordTag()
                tag.title = title
                tag.comment = comment
                tag.update()
                val id = recordTagDao.insert(tag)
                if (id < 0) {
                    submitResult.postValue(Result(false, "添加失败"))
                } else {
                    submitResult.postValue(Result(true, "ok", tag.also { tag.id = id }))
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordTagDao: RecordTagDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordTagEditViewModel(recordTagDao) as T
        }
    }
}