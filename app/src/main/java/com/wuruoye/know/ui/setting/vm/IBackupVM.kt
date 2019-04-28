package com.wuruoye.know.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.wuruoye.know.util.model.beans.BackupInfo
import com.wuruoye.know.util.model.beans.NetResult

/**
 * Created at 2019-04-25 17:14 by wuruoye
 * Description:
 */
interface IBackupVM {
    val backupInfo: MutableLiveData<BackupInfo>
    val loadingTitle: MutableLiveData<String>
    val backupResult: MutableLiveData<NetResult>

    fun updateInfo()
    fun backup()
    fun download()
}