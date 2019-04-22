package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.adapter.RecordTypeSelectAdapter
import com.wuruoye.know.ui.edit.vm.IRecordTypeEditVM
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory
import com.wuruoye.know.util.model.RequestCode
import com.wuruoye.know.util.model.beans.RealRecordLayoutView
import com.wuruoye.know.util.model.beans.RecordTypeSelect
import com.wuruoye.know.util.orm.table.RecordImageView
import com.wuruoye.know.util.orm.table.RecordLayoutView
import com.wuruoye.know.util.orm.table.RecordTextView
import com.wuruoye.know.util.orm.table.RecordView

/**
 * Created at 2019-04-22 15:02 by wuruoye
 * Description:
 */
class RecordTypeEditFragment :
    Fragment(),
    View.OnClickListener,
    RecordTypeSelectAdapter.OnClickListener,
    ViewFactory.OnLongClickListener {

    private lateinit var dlgSelectItem: BottomSheetDialog
    private lateinit var rvSelectItem: RecyclerView

    private lateinit var mParent: ViewGroup
    private lateinit var mParentViews: ArrayList<RecordView>
    private lateinit var mUpdateView: RecordView

    private lateinit var llContent: LinearLayout
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var vm: IRecordTypeEditVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_type_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(activity!!,
            InjectorUtil.recordTypeEditViewModelFactory(context!!))
            .get(RecordTypeEditViewModel::class.java)

        bindView(view)
        bindListener()
        initDlg()
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            llContent = findViewById(R.id.ll_record_type_edit)
            fabAdd = findViewById(R.id.fab_record_type_edit)
        }
    }

    private fun bindListener() {
        fabAdd.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        val selectAdapter = RecordTypeSelectAdapter()
        selectAdapter.setOnClickListener(this)
        rvSelectItem = LayoutInflater.from(context!!)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvSelectItem.layoutManager = LinearLayoutManager(context!!)
        rvSelectItem.adapter = selectAdapter
        dlgSelectItem = BottomSheetDialog(context!!)
        dlgSelectItem.setContentView(rvSelectItem)
    }

    private fun subscribeUI() {
        vm.selectItems.observe(this, Observer {
            (rvSelectItem.adapter as RecordTypeSelectAdapter).submitList(it)
        })
        vm.recordType.observe(this, Observer {
            llContent.removeAllViews()
            for (v in it.items) {
                ViewFactory.generateEditView(context!!,
                    v, llContent, it.items, true, this)
            }
        })
    }

    override fun onLongClick(recordView: RecordView, view: View,
                             parentView: ArrayList<RecordView>, parent: ViewGroup) {
        AlertDialog.Builder(context!!)
            .setItems(if (recordView is RealRecordLayoutView) RecordTypeEditActivity.ITEM_LAYOUT else RecordTypeEditActivity.ITEM_VIEW) {
                    _, which ->
                when (which) {
                    0 -> {  // update
                        mParent = parent
                        mParentViews = parentView
                        mUpdateView = recordView
                        val intent = Intent(context, TypeItemEditActivity::class.java)
                        intent.putExtra(TypeItemEditActivity.RECORD_VIEW,
                            if (recordView is RealRecordLayoutView) {
                                RecordLayoutView(recordView, "")
                            } else {
                                recordView
                            })
                        startActivityForResult(intent, RequestCode.RECORD_TYPE_EDIT_FOR_UPDATE)
                    }
                    1 -> {  // remove
                        parentView.remove(recordView)
                        parent.removeView(view)
                        vm.removeView(recordView)
                    }
                    2 -> {  // add
                        mParent = view as ViewGroup
                        mParentViews = (recordView as RealRecordLayoutView).items
                        dlgSelectItem.show()
                    }
                }
            }
            .show()
    }

    override fun onClick(item: RecordTypeSelect) {
        dlgSelectItem.dismiss()
        val intent = Intent(context, TypeItemEditActivity::class.java)
        intent.putExtra(TypeItemEditActivity.RECORD_TYPE, item.id)
        startActivityForResult(intent, RequestCode.RECORD_TYPE_EDIT_FOR_ADD)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_record_type_edit -> {
                mParentViews = vm.recordType.value!!.items
                mParent = llContent
                dlgSelectItem.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val view = data!!.getParcelableExtra<RecordView>(TypeItemEditActivity.RECORD_VIEW)
            when (requestCode) {
                RequestCode.RECORD_TYPE_EDIT_FOR_ADD -> {
                    mParentViews.add(if (view is RecordLayoutView) {
                        RealRecordLayoutView(view, arrayListOf())
                    } else view)
                }
                RequestCode.RECORD_TYPE_EDIT_FOR_UPDATE -> {
                    when (view) {
                        is RecordLayoutView -> {
                            (mUpdateView as RealRecordLayoutView).setInfo(view)
                        }
                        is RecordTextView -> {
                            (mUpdateView as RecordTextView).setInfo(view)
                        }
                        is RecordImageView -> {
                            (mUpdateView as RecordImageView).setInfo(view)
                        }
                    }
                }
            }
            vm.updateView()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = RecordTypeEditFragment()
    }
}