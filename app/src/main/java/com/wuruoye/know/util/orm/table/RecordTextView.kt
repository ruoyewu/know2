package com.wuruoye.know.util.orm.table

import android.graphics.Color
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.view.Gravity
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created at 2019/4/9 20:00 by wuruoye
 * Description:
 */
@Entity(tableName = "text_view",
    indices = [Index("createTime")])
class RecordTextView(
    @PrimaryKey(autoGenerate = true)
    override var id: Long?,
    var text: String,
    var textSize: Int,
    var textColor: Int,
    var hint: String,
    var hintSize: Int,
    var hintColor: Int,
    var bgColor: Int,
    var gravity: Int,
    var textStyle: Int,
    var inputType: Int,
    var minLine: Int,
    var maxLine: Int,
    var editable: Boolean,
    override var width: Int,
    override var height: Int,
    override var marginLeft: Int,
    override var marginRight: Int,
    override var marginTop: Int,
    override var marginBottom: Int,
    override var paddingLeft: Int,
    override var paddingRight: Int,
    override var paddingTop: Int,
    override var paddingBottom: Int,
    override var createTime: Long,
    override var updateTime: Long
) : RecordView, Parcelable {
    constructor(editable: Boolean) :
            this(
                null, "", 15, Color.BLACK, "", 15, Color.GRAY,
                0, Gravity.CENTER, Typeface.NORMAL,
                InputType.TYPE_CLASS_TEXT,
                1, 1, editable, -1, -2,
                0, 0, 0, 0, 0,
                0, 0, 0, -1, -1
            )

    fun setInfo(v: RecordTextView) {
        text = v.text
        textSize = v.textSize
        textColor = v.textColor
        hint = v.hint
        hintSize = v.hintSize
        hintColor = v.hintColor
        bgColor = v.bgColor
        gravity = v.gravity
        textStyle = v.textStyle
        inputType = v.inputType
        minLine = v.minLine
        maxLine = v.maxLine
        editable = v.editable
        width = v.width
        height = v.height
        marginLeft = v.marginLeft
        marginRight = v.marginRight
        marginTop = v.marginTop
        marginBottom = v.marginBottom
        paddingLeft = v.paddingLeft
        paddingRight = v.paddingRight
        paddingTop = v.paddingTop
        paddingBottom = v.paddingBottom
        createTime = v.createTime
        updateTime = v.updateTime
    }

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        1 == source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readLong(),
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(text)
        writeInt(textSize)
        writeInt(textColor)
        writeString(hint)
        writeInt(hintSize)
        writeInt(hintColor)
        writeInt(bgColor)
        writeInt(gravity)
        writeInt(textStyle)
        writeInt(inputType)
        writeInt(minLine)
        writeInt(maxLine)
        writeInt((if (editable) 1 else 0))
        writeInt(width)
        writeInt(height)
        writeInt(marginLeft)
        writeInt(marginRight)
        writeInt(marginTop)
        writeInt(marginBottom)
        writeInt(paddingLeft)
        writeInt(paddingRight)
        writeInt(paddingTop)
        writeInt(paddingBottom)
        writeLong(createTime)
        writeLong(updateTime)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordTextView

        if (id != other.id) return false
        if (text != other.text) return false
        if (textSize != other.textSize) return false
        if (textColor != other.textColor) return false
        if (hint != other.hint) return false
        if (hintSize != other.hintSize) return false
        if (hintColor != other.hintColor) return false
        if (bgColor != other.bgColor) return false
        if (gravity != other.gravity) return false
        if (textStyle != other.textStyle) return false
        if (inputType != other.inputType) return false
        if (minLine != other.minLine) return false
        if (maxLine != other.maxLine) return false
        if (editable != other.editable) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (marginLeft != other.marginLeft) return false
        if (marginRight != other.marginRight) return false
        if (marginTop != other.marginTop) return false
        if (marginBottom != other.marginBottom) return false
        if (paddingLeft != other.paddingLeft) return false
        if (paddingRight != other.paddingRight) return false
        if (paddingTop != other.paddingTop) return false
        if (paddingBottom != other.paddingBottom) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + text.hashCode()
        result = 31 * result + textSize
        result = 31 * result + textColor
        result = 31 * result + hint.hashCode()
        result = 31 * result + hintSize
        result = 31 * result + hintColor
        result = 31 * result + bgColor
        result = 31 * result + gravity
        result = 31 * result + textStyle
        result = 31 * result + inputType
        result = 31 * result + minLine
        result = 31 * result + maxLine
        result = 31 * result + editable.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + marginLeft
        result = 31 * result + marginRight
        result = 31 * result + marginTop
        result = 31 * result + marginBottom
        result = 31 * result + paddingLeft
        result = 31 * result + paddingRight
        result = 31 * result + paddingTop
        result = 31 * result + paddingBottom
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordTextView> = object : Parcelable.Creator<RecordTextView> {
            override fun createFromParcel(source: Parcel): RecordTextView = RecordTextView(source)
            override fun newArray(size: Int): Array<RecordTextView?> = arrayOfNulls(size)
        }
    }
}