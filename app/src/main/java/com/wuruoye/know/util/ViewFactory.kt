package com.wuruoye.know.util

import android.content.Context
import android.graphics.Bitmap
import android.text.method.ScrollingMovementMethod
import android.util.ArrayMap
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RecordShow
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created at 2019-04-25 08:47 by wuruoye
 * Description:
 */
object ViewFactory {
    interface OnLongClickListener {
        fun onClick(recordView: RecordView, view: View,
                    parentView: ArrayList<RecordView>, parent: ViewGroup)
    }

    interface OnClickListener {
        fun onClick(recordView: RecordView, view: View)
    }



    fun generateView(context: Context,
                     recordShow: RecordShow,
                     parent: ViewGroup,
                     attach: Boolean = true,
                     listener: OnClickListener? = null,
                     isShow: Boolean = false
    ) {
        val realType = recordShow.recordType
        val map = recordShow.recordData

        for (v in realType.items) {
            generateView(context, v, parent, attach, listener, map, isShow)
        }
    }

    fun generateView(context: Context,
                     recordView: RecordView,
                     parent: ViewGroup,
                     attach: Boolean = true,
                     listener: OnClickListener? = null,
                     map: ArrayMap<String, RecordItem>? = null,
                     isShow: Boolean = false
    ): View? {
        return when(recordView) {
            is RecordTextView ->
                generateTextView(context, recordView, parent, attach, map, isShow)
            is RecordImageView ->
                generateImageView(context, recordView, parent, attach, listener, map, isShow)
            is RealRecordLayoutView ->
                generateLayoutView(context, recordView, parent, attach, listener, map, isShow)
            else -> null
        }
    }

    private fun generateTextView(
        context: Context,
        textView: RecordTextView,
        parent: ViewGroup,
        attach: Boolean,
        map: ArrayMap<String, RecordItem>? = null,
        isShow: Boolean = false
    ): View {
        with(textView) {
            if (editable) {
                val viewLayout = LayoutInflater.from(context)
                    .inflate(R.layout.view_edit, parent, false) as TextInputLayout

                viewLayout.hint = hint
                // TODO set hint size and color using reflect
                viewLayout.setBackgroundColor(bgColor)

                viewLayout.setPadding(DensityUtil.dp2px(context, paddingLeft.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingTop.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingRight.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingBottom.toFloat()).toInt())
                val params = ViewGroup.MarginLayoutParams(viewLayout.layoutParams)
                params.setMargins(DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginTop.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginBottom.toFloat()).toInt())
                params.width = lengthToPx(context, width)
                params.height = lengthToPx(context, height)
                viewLayout.layoutParams = params

                val view = viewLayout.findViewById<EditText>(R.id.et_view_edit)
                view.setTextColor(textColor)
                view.textSize = textSize.toFloat()
                view.gravity = gravity
                view.setTypeface(view.typeface, textStyle)
                view.inputType = inputType
                view.minLines = minLine
                view.maxLines = maxLine
                view.movementMethod = ScrollingMovementMethod.getInstance()

                if (attach) {
                    parent.addView(viewLayout)
                }
                if (map != null) {
                    val item = map[getKey(textView)]
                    if (item != null) {
                        viewLayout.setTag(R.id.tag_text, item)
                        view.setText(item.content)
                        view.setSelection(item.content.length)
                    }
                }
                if (isShow) {
                    view.isEnabled = false
                }
                return viewLayout
            } else {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.view_text, parent, false) as TextView
                view.text = text
                view.setTextColor(textColor)
                view.textSize = textSize.toFloat()
                view.setBackgroundColor(bgColor)

                view.setPadding(
                    DensityUtil.dp2px(context, paddingLeft.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingTop.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingRight.toFloat()).toInt(),
                    DensityUtil.dp2px(context, paddingBottom.toFloat()).toInt()
                )
                val params = ViewGroup.MarginLayoutParams(view.layoutParams)
                params.setMargins(
                    DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginTop.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginBottom.toFloat()).toInt()
                )
                params.width = lengthToPx(context, width)
                params.height = lengthToPx(context, height)
                view.layoutParams = params
                view.gravity = gravity
                view.setTypeface(view.typeface, textStyle)
                view.minLines = minLine
                view.maxLines = maxLine
                view.movementMethod = ScrollingMovementMethod.getInstance()

                if (attach) {
                    parent.addView(view)
                }
                return view
            }
        }
    }

    private fun generateImageView(
        context: Context,
        imageView: RecordImageView,
        parent: ViewGroup,
        attach: Boolean,
        listener: OnClickListener? = null,
        map: ArrayMap<String, RecordItem>? = null,
        isShow: Boolean = false
    ): View {
        with(imageView) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.view_img, parent, false) as ImageView
            view.setPadding(DensityUtil.dp2px(context, paddingLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingTop.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingBottom.toFloat()).toInt())

            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = lengthToPx(context, width)
            lp.height = lengthToPx(context, height)
            lp.setMargins(DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginTop.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginBottom.toFloat()).toInt())
            view.layoutParams = lp

            if (attach) {
                parent.addView(view)
            }

            if (listener != null) {
                view.setOnClickListener {
                    listener.onClick(imageView, view)
                }
            }

            if (map != null) {
                val item = map[getKey(imageView)]
                if (item != null) {
                    val path = GsonFactory.getInstance()
                        .fromJson(item.content, ImagePath::class.java)
                    loadImg(path, view, generateOption(imageView, view))
                    view.setTag(R.id.tag_image, item)
                }
            }

            if (isShow && map == null) {
                Glide.with(context)
                    .load(R.drawable.ic_demo)
                    .apply(generateOption(imageView, view))
                    .into(view)
            }

            return view
        }
    }

    private fun generateLayoutView(
        context: Context,
        layoutView: RealRecordLayoutView,
        parent: ViewGroup,
        attach: Boolean,
        listener: OnClickListener? = null,
        map: ArrayMap<String, RecordItem>? = null,
        isShow: Boolean = false
    ): View {
        with(layoutView) {
            val view = LayoutInflater.from(context).inflate(R.layout.view_layout,
                parent, false) as LinearLayout
            view.orientation = orientation
            view.gravity = gravity
            view.setBackgroundColor(bgColor)
            view.setPadding(DensityUtil.dp2px(context, paddingLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingTop.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingBottom.toFloat()).toInt())
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = lengthToPx(context, width)
            lp.height = lengthToPx(context, height)
            lp.setMargins(DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginTop.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginBottom.toFloat()).toInt())
            view.layoutParams = lp

            for (v in items) {
                generateView(context, v, view, true, listener, map, isShow)
            }

            if (attach) {
                parent.addView(view)
            }
            return view
        }
    }




    fun loadRecordView(views: ArrayList<RecordView>,
                       parent: ViewGroup
    ): ArrayList<RecordItem> {
        val result = ArrayList<RecordItem>()
        loadRecordViewRecursive(result, views, parent)
        return result
    }

    private fun loadRecordViewRecursive(itemList: ArrayList<RecordItem>,
                                        views: ArrayList<RecordView>,
                                        parent: ViewGroup
    ) {
        for (i in 0 until views.size) {
            val v = views[i]
            val child = parent.getChildAt(i)

            if (v is RealRecordLayoutView) {
                loadRecordViewRecursive(itemList, v.items, child as ViewGroup)
            } else if (v is RecordTextView && v.editable) {
                var item = child.getTag(R.id.tag_text)
                if (item == null) {
                    item = RecordItem(-1, v.id!!, RecordTypeSelect.TYPE_TEXT)
                }
                item = item as RecordItem
                item.content = (child as TextInputLayout).editText!!.text.toString()
                itemList.add(item)
            } else if (v is RecordImageView) {
                val item = child.getTag(R.id.tag_image)
                if (item != null) {
                    itemList.add(item as RecordItem)
                }
            }
        }
    }





    fun generateEditView(context: Context,
                         recordView: RecordView,
                         parent: ViewGroup,
                         parentView: ArrayList<RecordView>,
                         attach: Boolean,
                         listener: OnLongClickListener? = null
    ): View? {
        if (recordView is RealRecordLayoutView) {
            return generateEditLayoutView(context, recordView, parent, parentView, attach, listener)
        } else {
            with(recordView) {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.view_text, parent, false) as TextView

                view.setBackgroundColor(ActivityCompat.getColor(context, R.color.transparent_platinum))
                view.setPadding(DensityUtil.dp2px(context, (paddingLeft+5).toFloat()).toInt(),
                    DensityUtil.dp2px(context, (paddingTop+5).toFloat()).toInt(),
                    DensityUtil.dp2px(context, (paddingRight+5).toFloat()).toInt(),
                    DensityUtil.dp2px(context, (paddingBottom+5).toFloat()).toInt())
                view.text = getLabel(recordView)
                view.gravity = Gravity.CENTER

                val lp = view.layoutParams as ViewGroup.MarginLayoutParams
                lp.width = lengthToPx(context, width)
                lp.height = lengthToPx(context, height)
                lp.setMargins(DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginTop.toFloat()+10).toInt(),
                    DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                    DensityUtil.dp2px(context, marginBottom.toFloat()+10).toInt())
                view.layoutParams = lp

                if (attach) {
                    parent.addView(view)
                }

                if (listener != null) {
                    view.setOnClickListener {
                        listener.onClick(recordView, view, parentView, parent)
                    }
                }

                return view
            }
        }
    }

    private fun generateEditLayoutView(context: Context,
                                       layoutView: RealRecordLayoutView,
                                       parent: ViewGroup,
                                       parentView: ArrayList<RecordView>,
                                       attach: Boolean,
                                       listener: OnLongClickListener? = null
    ): View {
        with(layoutView) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.view_layout, parent, false) as LinearLayout
            view.setBackgroundColor(ActivityCompat.getColor(context, R.color.transparent_platinum))
            view.setPadding(DensityUtil.dp2px(context, paddingLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, (paddingTop+10).toFloat()).toInt(),
                DensityUtil.dp2px(context, paddingRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, (paddingBottom+10).toFloat()).toInt())
            view.gravity = gravity
            view.orientation = orientation

            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.width = lengthToPx(context, width)
            lp.height = lengthToPx(context, height)
            lp.setMargins(DensityUtil.dp2px(context, marginLeft.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginTop.toFloat()+10).toInt(),
                DensityUtil.dp2px(context, marginRight.toFloat()).toInt(),
                DensityUtil.dp2px(context, marginBottom.toFloat()+10).toInt())
            view.layoutParams = lp

            for (v in items) {
                generateEditView(context, v, view, layoutView.items, true, listener)
            }

            if (attach) {
                parent.addView(view)
            }

            if (listener != null) {
                view.setOnClickListener {
                    listener.onClick(layoutView, view, parentView, parent)
                }
            }

            return view
        }
    }


    private fun getKey(view: RecordView): String {
        val type = RecordTypeSelect.getType(view)
        return "${type}_${view.id}"
    }

    private fun lengthToPx(context: Context, length: Int): Int {
        return if (length < 0) length
        else DensityUtil.dp2px(context, length.toFloat()).toInt()
    }

    private fun getLabel(view: RecordView): String {
        return when (view) {
            is RecordImageView -> "图片"
            is RecordTextView -> if (view.editable) "编辑框" else "标签"
            else -> "未知"
        }
    }

    fun loadImg(imagePath: ImagePath, iv: ImageView, options: BaseRequestOptions<*>) {
        Glide.with(iv)
            .load(imagePath.localPath)
            .apply(options)
            .error(Glide.with(iv)
                .load(imagePath.remotePath))
            .into(iv)
    }

    fun generateOption(view: RecordImageView, iv: ImageView): BaseRequestOptions<*> {
        val default = RoundedCornersTransformation(0, 0)
        return RequestOptions.bitmapTransform(
            MultiTransformation<Bitmap>(
                if (view.blur) BlurTransformation(25) else default,
                if (view.tint != 0) ColorFilterTransformation(view.tint) else default,
                when (view.shape) {
                    0 -> CenterCrop()
                    1 -> MultiTransformation<Bitmap>(
                        CenterCrop(),
                        RoundedCornersTransformation(25, 0)
                    )
                    2 -> CircleCrop()
                    else -> default
                }
            )
        )
    }
}