package com.wuruoye.know.util.model.beans

/**
 * Created at 2019/4/11 21:38 by wuruoye
 * Description:
 */
class ImagePath(
    var localPath: String,
    var remotePath: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImagePath

        if (localPath != other.localPath) return false
        if (remotePath != other.remotePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localPath.hashCode()
        result = 31 * result + remotePath.hashCode()
        return result
    }
}