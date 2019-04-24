package com.wuruoye.know.util.model.beans

import android.os.Parcel
import android.os.Parcelable

/**
 * Created at 2019-04-22 18:37 by wuruoye
 * Description:
 */
class UserInfo(
    var id: String,
    var name: String,
    var avatar: String,
    var sign: String,
    var phoneNumber: String,
    var createTime: Long
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(avatar)
        writeString(sign)
        writeString(phoneNumber)
        writeLong(createTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserInfo> = object : Parcelable.Creator<UserInfo> {
            override fun createFromParcel(source: Parcel): UserInfo = UserInfo(source)
            override fun newArray(size: Int): Array<UserInfo?> = arrayOfNulls(size)
        }
    }
}