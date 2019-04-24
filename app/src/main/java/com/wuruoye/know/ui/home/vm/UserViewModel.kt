package com.wuruoye.know.ui.home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.NetUtil
import com.wuruoye.know.util.model.AppCache
import com.wuruoye.know.util.model.beans.UserInfo
import com.wuruoye.know.util.orm.dao.RecordDao
import com.wuruoye.know.util.orm.dao.RecordTagDao
import com.wuruoye.know.util.orm.dao.RecordTypeDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created at 2019/4/13 14:25 by wuruoye
 * Description:
 */
class UserViewModel(
    private val recordDao: RecordDao,
    private val recordTypeDao: RecordTypeDao,
    private val recordTagDao: RecordTagDao,
    private val cache: AppCache
) : ViewModel(), IUserVM {
    override val userInfo: MutableLiveData<UserInfo?> =
        MutableLiveData()

    override var login: Boolean
        get() = cache.userLogin
        set(value) { cache.userLogin = value }

    override var recordSize: MutableLiveData<Long> =
            MutableLiveData()

    override var recordTagSize: MutableLiveData<Long> =
            MutableLiveData()

    override var recordTypeSize: MutableLiveData<Long> =
            MutableLiveData()

    init {
        updateInfo()

        if (login) {
            login()
        }
    }

    override fun updateInfo() {
        GlobalScope.launch {
            recordSize.postValue(recordDao.queryCount())
            recordTypeSize.postValue(recordTypeDao.queryCount())
            recordTagSize.postValue(recordTagDao.queryCount())
        }
    }

    override fun logout() {
        login = false
        cache.userId = ""
        cache.userPwd = ""
    }

    private fun login() {
        GlobalScope.launch {
            val id = cache.userId
            val pwd = cache.userPwd

            val values = mapOf(Pair("id", id),
                Pair("pwd", pwd))
            val result = NetUtil.get(NetUtil.LOGIN, values)
            if (result.successful) {
                val userInfo = GsonFactory.getInstance()
                    .fromJson(result.data!!, UserInfo::class.java)
                this@UserViewModel.userInfo.postValue(userInfo)
            } else {
                logout()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val recordDao: RecordDao,
        private val recordTypeDao: RecordTypeDao,
        private val recordTagDao: RecordTagDao,
        private val cache: AppCache
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(recordDao, recordTypeDao, recordTagDao, cache) as T
        }
    }
}