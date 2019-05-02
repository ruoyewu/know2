package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakFragment
import com.wuruoye.know.ui.edit.RecordEditActivity
import com.wuruoye.know.ui.edit.RecordTypeEditActivity
import com.wuruoye.know.ui.home.adapter.RecordListAdapter
import com.wuruoye.know.ui.home.adapter.RecordTagAdapter
import com.wuruoye.know.ui.home.adapter.RecordTypeAdapter
import com.wuruoye.know.ui.home.adapter.TimeLimitAdapter
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.home.vm.IRecordVM
import com.wuruoye.know.ui.home.vm.RecordViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_RECORD
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_TYPE
import com.wuruoye.know.util.model.RequestCode.USER_FOR_BACKUP
import com.wuruoye.know.util.model.RequestCode.USER_FOR_RECORD_TAG
import com.wuruoye.know.util.model.RequestCode.USER_FOR_RECORD_TYPE
import com.wuruoye.know.util.model.beans.RecordListItem
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.table.RecordTag
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/9 21:16 by wuruoye
 * Description:
 */
class RecordFragment : LeakFragment(),
    View.OnClickListener,
    RecordTypeAdapter.OnClickListener,
    RecordTypeAdapter.OnLongClickListener,
    TimeLimitAdapter.OnClickListener,
    RecordListAdapter.OnClickListener,
    RecordTagAdapter.OnClickListener, BaseAdapter.OnActionListener<RecordListItem> {

    private lateinit var dlgSelectType: BottomSheetDialog
    private lateinit var rvSelect: RecyclerView

    private lateinit var dlgLimitType: BottomSheetDialog
    private lateinit var rvTypeLimit: RecyclerView

    private lateinit var dlgLimitTime: BottomSheetDialog
    private lateinit var rvTimeLimit: RecyclerView

    private lateinit var dlgLimitTag: BottomSheetDialog
    private lateinit var rvTagLimit: RecyclerView

    private lateinit var tvTitle: TextView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvTimeLimit: TextView
    private lateinit var tvTagLimit: TextView
    private lateinit var tvTypeLimit: TextView
    private lateinit var rvRecord: RecyclerView

    private lateinit var vm: IRecordVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this,
            InjectorUtil.recordViewModelFactory(context!!.applicationContext))
            .get(RecordViewModel::class.java)

        bindView(view)
        bindListener()
        initDlg()
        initView()
        subscribeUI()
    }

    private fun bindView(view: View) {
        tvTitle = view.findViewById(R.id.tv_title_toolbar)
        fabAdd = view.findViewById(R.id.fab_record)
        tvTimeLimit = view.findViewById(R.id.tv_time_limit_record)
        tvTagLimit = view.findViewById(R.id.tv_type_limit_tag)
        tvTypeLimit = view.findViewById(R.id.tv_type_limit_record)
        rvRecord = view.findViewById(R.id.rv_record)
    }

    private fun bindListener() {
        fabAdd.setOnClickListener(this)
        tvTimeLimit.setOnClickListener(this)
        tvTagLimit.setOnClickListener(this)
        tvTypeLimit.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun initDlg() {
        rvSelect = LayoutInflater.from(context)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvSelect.layoutManager = LinearLayoutManager(context)
        val selectAdapter = RecordTypeAdapter(RecordTypeAdapter.TYPE_ADD_UPDATE)
        selectAdapter.setOnClickListener(this)
        selectAdapter.setOnLongClickListener(this)
        rvSelect.adapter = selectAdapter
        dlgSelectType = BottomSheetDialog(context!!)
        dlgSelectType.setContentView(rvSelect)
        dlgSelectType.setTitle("选择记录类型：")


        rvTypeLimit = LayoutInflater.from(context)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvTypeLimit.layoutManager = LinearLayoutManager(context)
        val typeLimitAdapter = RecordTypeAdapter(RecordTypeAdapter.TYPE_TYPE_LIMIT)
        typeLimitAdapter.setOnClickListener(object : RecordTypeAdapter.OnClickListener {
            override fun onClick(recordType: RecordType) {
                dlgLimitType.dismiss()
                vm.setTypeLimit(recordType.id ?: -1)
            }
        })
        rvTypeLimit.adapter = typeLimitAdapter
        dlgLimitType = BottomSheetDialog(context!!)
        dlgLimitType.setContentView(rvTypeLimit)
        dlgLimitType.setTitle("选择记录类型：")

        rvTagLimit = LayoutInflater.from(context)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvTagLimit.layoutManager = LinearLayoutManager(context)
        val tagLimitAdapter = RecordTagAdapter()
        tagLimitAdapter.setOnClickListener(this)
        rvTagLimit.adapter = tagLimitAdapter
        dlgLimitTag = BottomSheetDialog(context!!)
        dlgLimitTag.setContentView(rvTagLimit)
        dlgLimitTag.setTitle("选择记录标签：")

        rvTimeLimit = LayoutInflater.from(context)
            .inflate(R.layout.dlg_record_type, null) as RecyclerView
        rvTimeLimit.layoutManager = LinearLayoutManager(context)
        val timeLimitAdapter = TimeLimitAdapter()
        timeLimitAdapter.setOnClickListener(this)
        rvTimeLimit.adapter = timeLimitAdapter
        dlgLimitTime = BottomSheetDialog(context!!)
        dlgLimitTime.setContentView(rvTimeLimit)
        dlgLimitTime.setTitle("选择时间：")
    }

    private fun initView() {
        tvTitle.text = getString(R.string.record)

        val adapter = RecordListAdapter()
        adapter.setOnClickListener(this)
        adapter.setOnActionListener(this)
        rvRecord.layoutManager = LinearLayoutManager(context)
        rvRecord.adapter = adapter
    }

    private fun subscribeUI() {
        vm.recordTypeList.observe(this, Observer {
            (rvSelect.adapter as RecordTypeAdapter).submitList(it)

            (rvTypeLimit.adapter as RecordTypeAdapter).submitList(it)
        })
        vm.timeLimitList.observe(this, Observer {
            (rvTimeLimit.adapter as TimeLimitAdapter).submitList(it)
        })
        vm.recordTagList.observe(this, Observer {
            (rvTagLimit.adapter as RecordTagAdapter).submitList(it)
        })
        vm.recordList.observe(this, Observer {
            (rvRecord.adapter as RecordListAdapter).submitList(it)
        })
        vm.recordTypeTitle.observe(this, Observer {
            tvTypeLimit.text = it
        })
        vm.recordTagTitle.observe(this, Observer {
            tvTagLimit.text = it
        })
        vm.recordLimitTitle.observe(this, Observer {
            tvTimeLimit.text = it
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_record -> {
                startAddRecord(vm.typeTypeLimit.value!!, vm.typeTagLimit.value!!)
            }
            R.id.tv_time_limit_record -> {
                dlgLimitTime.show()
            }
            R.id.tv_type_limit_record -> {
                dlgLimitType.show()
            }
            R.id.tv_type_limit_tag -> {
                dlgLimitTag.show()
            }
        }
    }

    override fun onClick(recordType: RecordType) {
        dlgSelectType.dismiss()
        if (recordType.id == null) {
            onLongClick(recordType)
        } else {
            startAddRecord(recordType.id!!, vm.typeTagLimit.value!!)
        }
    }

    override fun onLeft(item: RecordListItem) {}

    override fun onRight(item: RecordListItem) {
        vm.removeRecord(item.record.id!!)
    }

    override fun onLongClick(recordType: RecordType) {
        dlgSelectType.dismiss()

        val intent = Intent(context, RecordTypeEditActivity::class.java)
        intent.putExtra(RecordTypeEditActivity.RECORD_TYPE, recordType.id)
        startActivityForResult(intent, RECORD_FOR_TYPE)
    }

    override fun onClick(item: TimeLimitItem) {
        dlgLimitTime.dismiss()
        vm.setTimeLimit(item.id)
    }

    override fun onClick(item: RecordListItem) {
        val intent = Intent(context, RecordEditActivity::class.java)
        intent.putExtra(RecordEditActivity.RECORD, item.record.id)
        startActivityForResult(intent, RECORD_FOR_RECORD)
    }

    override fun onClick(item: RecordTag) {
        dlgLimitTag.dismiss()
        vm.setTagLimit(item.id ?: -1)
    }

    private fun startAddRecord(type: Long, tag: Long) {
        if (type < 0) {
            dlgSelectType.show()
        } else {
            val intent = Intent(context, RecordEditActivity::class.java)
            intent.putExtra(RecordEditActivity.RECORD_TYPE, type)
            if (tag >= 0) intent.putExtra(RecordEditActivity.RECORD_TAG, tag)
            startActivityForResult(intent, RECORD_FOR_RECORD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                RECORD_FOR_TYPE -> {
                    vm.updateRecord()
                    vm.updateRecordType()
                }
                RECORD_FOR_RECORD -> {
                    vm.updateRecord()
                }
                USER_FOR_RECORD_TYPE -> {
                    vm.updateRecord()
                    vm.updateRecordType()
                }
                USER_FOR_RECORD_TAG -> {
                    vm.updateRecordTag()
                    vm.updateRecord()
                }
                USER_FOR_BACKUP -> {
                    vm.updateRecord()
                    vm.updateRecordTag()
                    vm.updateRecordType()
                }
            }
        }
    }
}