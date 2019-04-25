package com.wuruoye.know.util.model.beans

import android.os.Parcel
import android.os.Parcelable

/**
 * Created at 2019/4/11 21:38 by wuruoye
 * Description:
 */
class ImagePath(
    var localPath: String,
    var remotePath: String
) : Parcelable {
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

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(localPath)
        writeString(remotePath)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImagePath> = object : Parcelable.Creator<ImagePath> {
            override fun createFromParcel(source: Parcel): ImagePath = ImagePath(source)
            override fun newArray(size: Int): Array<ImagePath?> = arrayOfNulls(size)
        }
    }
}