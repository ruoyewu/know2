package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import com.wuruoye.know.util.model.RequestCode.RECORD_EDIT_FOR_TAG
import com.wuruoye.know.util.model.beans.ImagePath
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordItem
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.orm.table.RecordView

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

        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()

        val recordId = intent.getLongExtra(RECORD, -1)
        if (recordId < 0) {
            vm.setRecordTypeId(intent.getLongExtra(RECORD_TYPE, -1))
        } else {
            vm.setRecordId(recordId)
        }

        val tag = intent.getLongExtra(RECORD_TAG, -1)
        if (tag >= 0) vm.setRecordTag(tag)

        vm.updateRecordTagList()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        llContent = findViewById(R.id.ll_record_edit)
        tvTag = findViewById(R.id.tv_tag_record_edit)
    }

    private fun bindListener() {
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
        tvTag.setOnClickListener(this)
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
        vm.recordShow.observe(this, Observer {
            tvTitle.text = it.recordType.title

            ViewFactory.generateView(this, it, llContent, listener = this)
        })
        vm.recordTagList.observe(this, Observer {
            (rvRecordTag.adapter as RecordTagAdapter).submitList(it)
        })
        vm.recordTagTitle.observe(this, Observer {
            tvTag.text = it
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
                val result =
                    ViewFactory.loadRecordView(vm.recordType.items, llContent)
                vm.saveRecordItems(result)
            }
            R.id.tv_tag_record_edit -> {
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
        mView.setTag(R.id.tag_image, item)
        ViewFactory.loadImg(path, mView, ViewFactory.generateOption(mRecordView as RecordImageView, mView))
    }

    override fun onPhotoError(error: String?) {

    }

    override fun onClick(item: RecordTag) {
        dlgRecordTag.dismiss()
        if (item.id != null) {
            vm.setRecordTag(item.id!!)
        } else {
            val intent = Intent(this, RecordTagEditActivity::class.java)
            startActivityForResult(intent, RECORD_EDIT_FOR_TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        WPhoto.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                RECORD_EDIT_FOR_TAG -> {
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

    private fun generateImgPath(): String {
        return WConfig.IMAGE_PATH + System.currentTimeMillis()
    }

    companion object {
        const val RECORD_TYPE = "type"
        const val RECORD = "record"
        const val RECORD_TAG = "record_tag"

        val ITEM_PHOTO = arrayOf("相册选择", "相机拍照", "相册选择&剪裁", "相机拍照&剪裁")
    }
}