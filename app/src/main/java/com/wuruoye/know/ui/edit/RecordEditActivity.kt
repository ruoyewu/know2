package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.vm.IRecordEditVM
import com.wuruoye.know.ui.edit.vm.RecordEditViewModel
import com.wuruoye.know.ui.home.adapter.RecordTagAdapter
import com.wuruoye.know.util.GsonFactory
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.base.WConfig
import com.wuruoye.know.util.base.media.IWPhoto
import com.wuruoye.know.util.base.media.WPhoto
import com.wuruoye.know.util.base.permission.WPermission
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.*
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created at 2019/4/11 18:40 by wuruoye
 * Description:
 */
class RecordEditActivity :
    AppCompatActivity(),
    View.OnClickListener,
    ViewFactory.OnClickListener,
    IWPhoto.OnWPhotoListener<String>,
    RecordTagAdapter.OnClickListener {

    private lateinit var dlgRecordTag: BottomSheetDialog
    private lateinit var rvRecordTag: RecyclerView

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var llContent: LinearLayout
    private lateinit var llTag: LinearLayout
    private lateinit var tvTag: TextView

    private lateinit var mPhotoGet: WPhoto
    private lateinit var mView: ImageView
    private lateinit var mRecordView: RecordView
    private lateinit var vm: IRecordEditVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_edit)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordEdiViewModelFactory(this))
            .get(RecordEditViewModel::class.java)
        mPhotoGet = WPhoto(this)

        vm.setRecordTypeId(intent!!.getLongExtra(RECORD_TYPE, -1))

        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        llContent = findViewById(R.id.ll_record_edit)
        llTag = findViewById(R.id.ll_tag_record_edit)
        tvTag = findViewById(R.id.tv_tag_record_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
        llTag.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        val tagAdapter = RecordTagAdapter(false)
        tagAdapter.setOnClickListener(this)
        rvRecordTag = LayoutInflater.from(this)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvRecordTag.layoutManager = LinearLayoutManager(this)
        rvRecordTag.adapter = tagAdapter
        dlgRecordTag = BottomSheetDialog(this)
        dlgRecordTag.setContentView(rvRecordTag)
        dlgRecordTag.setTitle("选择标签：")
    }

    private fun initView() {
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_check)
    }

    private fun subscribeUI() {
        vm.recordType.observe(this, Observer {
            tvTitle.text = it.title

            for (v in it.items) {
                ViewFactory.generateView(this, v, llContent, true, this)
            }

            val recordId = intent!!.getLongExtra(RECORD, -1)
            if (recordId >= 0) {
                vm.setRecordId(recordId)
            }
        })
        vm.recordTagList.observe(this, Observer {
            (rvRecordTag.adapter as RecordTagAdapter).submitList(it)
        })
        vm.recordTagTitle.observe(this, Observer {
            tvTag.text = it
        })
        vm.recordData.observe(this, Observer {
            loadRecord(it)
        })
        vm.submitResult.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    override fun onClick(recordView: RecordView, view: View) {
        if (recordView is RecordImageView) {
            mView = view as ImageView
            mRecordView = recordView
            AlertDialog.Builder(this)
                .setItems(ITEM_PHOTO) { _, which ->
                    when(which) {
                        0 -> mPhotoGet.choosePhoto(this)
                        1 -> mPhotoGet.takePhoto(generateImgPath(), this)
                        2 -> mPhotoGet.choosePhoto(generateImgPath(), 1, 1,
                            500, 500, this)
                        3 -> mPhotoGet.takePhoto(generateImgPath(), 1, 1,
                            500, 500, this)
                    }
                }
                .show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.iv_more_toolbar -> {
                saveRecord()
            }
            R.id.ll_tag_record_edit -> {
                dlgRecordTag.show()
            }
        }
    }

    override fun onPhotoResult(result: String) {
        var item = mView.getTag(R.id.tag_image)
        if (item == null) {
            item = RecordItem(-1L, mRecordView.id!!, RecordTypeSelect.getType(mRecordView))
        }
        val path = GsonFactory.getInstance()
            .fromJson((item as RecordItem).content, ImagePath::class.java)
            ?: ImagePath("", "")
        path.localPath = result
        path.remotePath = ""
        item.content = GsonFactory.getInstance().toJson(path)
        mView.setTag(R.id.tag_text, item)
        loadImage(path, mView, generateOption(mRecordView as RecordImageView, mView))
    }

    override fun onPhotoError(error: String?) {

    }

    override fun onClick(item: RecordTag) {
        dlgRecordTag.dismiss()
        if (item.id != null) {
            vm.setRecordTag(item.id!!)
        } else {
            val intent = Intent(this, RecordTagEditActivity::class.java)
            startActivityForResult(intent, FOR_TAG_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        WPhoto.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                FOR_TAG_RESULT -> {
                    val tag = data!!.getParcelableExtra<RecordTag>(RecordTagEditActivity.RECORD_TAG)
                    vm.setRecordTag(tag.id!!)
                    vm.updateRecordTagList()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        WPermission.onPermissionResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadRecord(map: ArrayMap<String, RecordItem>) {
        val views = vm.recordType.value!!.items
        val parent = llContent

        loadRecordRecursive(map, views, parent)
    }

    private fun loadRecordRecursive(map: ArrayMap<String, RecordItem>,
                                    views: ArrayList<RecordView>,
                                    parent: ViewGroup) {
        for (i in 0 until views.size) {
            val v = views[i]
            val child = parent.getChildAt(i)

            if (v is RealRecordLayoutView) {
                loadRecordRecursive(map, v.items, child as ViewGroup)
            } else if (v is RecordTextView && v.editable) {
                val item = map[generateKey(v)]
                if (item != null) {
                    (child as TextInputLayout).editText!!.setText(item.content)
                    child.setTag(R.id.tag_text, item)
                }
            } else if (v is RecordImageView) {
                val item = map[generateKey(v)]
                if (item != null) {
                    val path = GsonFactory.getInstance()
                        .fromJson(item.content, ImagePath::class.java)
                    loadImage(path, child as ImageView, generateOption(v, child))
                    child.setTag(R.id.tag_image, item)
                }
            }
        }
    }

    private fun saveRecord() {
        val itemList = ArrayList<RecordItem>()
        val views = vm.recordType.value!!.items
        val parent = llContent

        saveRecordRecursive(itemList, views, parent)

        vm.saveRecordItems(itemList)
    }

    private fun saveRecordRecursive(itemList: ArrayList<RecordItem>,
                                    views: ArrayList<RecordView>,
                                    parent: ViewGroup) {
        for (i in 0 until views.size) {
            val v = views[i]
            val child = parent.getChildAt(i)

            if (v is RealRecordLayoutView) {
                saveRecordRecursive(itemList, v.items, child as ViewGroup)
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

    private fun generateKey(view: RecordView): String {
        val type = RecordTypeSelect.getType(view)
        return "${type}_${view.id}"
    }

    private fun loadImage(imagePath: ImagePath, iv: ImageView, options: BaseRequestOptions<*>) {
        Glide.with(iv)
            .load(imagePath.localPath)
            .apply(options)
            .error(
                Glide.with(iv)
                    .load(imagePath.remotePath)
            )
            .into(iv)
    }

    private fun generateOption(view: RecordImageView, iv: ImageView): BaseRequestOptions<*> {
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

    private fun generateImgPath(): String {
        return WConfig.IMAGE_PATH + System.currentTimeMillis()
    }

    companion object {
        const val RECORD_TYPE = "type"
        const val RECORD = "record"
        const val FOR_TAG_RESULT = 1

        val ITEM_PHOTO = arrayOf("相册选择", "相机拍照", "相册选择&剪裁", "相机拍照&剪裁")
    }
}