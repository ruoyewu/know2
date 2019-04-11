package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.RecordTypeEditActivity
import com.wuruoye.know.ui.home.adapter.RecordTypeAdapter
import com.wuruoye.know.ui.home.adapter.TimeLimitAdapter
import com.wuruoye.know.ui.home.vm.IRecordVM
import com.wuruoye.know.ui.home.vm.RecordViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.beans.TimeLimitItem
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/9 21:16 by wuruoye
 * Description:
 */
class RecordFragment : Fragment(),
    View.OnClickListener,
    RecordTypeAdapter.OnClickListener,
    RecordTypeAdapter.OnLongClickListener, TimeLimitAdapter.OnClickListener {

    private lateinit var dlgSelectType: BottomSheetDialog
    private lateinit var rvSelect: RecyclerView

    private lateinit var dlgLimitType: BottomSheetDialog
    private lateinit var rvTypeLimit: RecyclerView

    private lateinit var dlgLimitTime: BottomSheetDialog
    private lateinit var rvTimeLimit: RecyclerView

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvTimeLimit: TextView
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
        subscribeUI()
    }

    private fun bindView(view: View) {
        fabAdd = view.findViewById(R.id.fab_record)
        tvTimeLimit = view.findViewById(R.id.tv_time_limit_record)
        tvTypeLimit = view.findViewById(R.id.tv_type_limit_record)
        rvRecord = view.findViewById(R.id.rv_record)
    }

    private fun bindListener() {
        fabAdd.setOnClickListener(this)
        tvTimeLimit.setOnClickListener(this)
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
                dlgLimitTime.dismiss()
                vm.setTypeLimit(recordType.id!!)
            }
        })
        rvTypeLimit.adapter = typeLimitAdapter
        dlgLimitType = BottomSheetDialog(context!!)
        dlgLimitType.setContentView(rvTypeLimit)
        dlgLimitType.setTitle("选择记录类型：")

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

    private fun subscribeUI() {
        vm.recordTypeList.observe(this, Observer {
            val selectAdapter = rvSelect.adapter as RecordTypeAdapter
            selectAdapter.submitList(it)

            val limitAdapter = rvTypeLimit.adapter as RecordTypeAdapter
            limitAdapter.submitList(it)
        })
        vm.timeLimitList.observe(this, Observer {
            val adapter = rvTimeLimit.adapter as TimeLimitAdapter
            adapter.submitList(it)
        })
        vm.recordList.observe(this, Observer {

        })
        vm.recordTypeTitle.observe(this, Observer {
            tvTypeLimit.text = it
        })
        vm.recordTypeTime.observe(this, Observer {
            tvTimeLimit.text = it
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_record -> {
                dlgSelectType.show()
            }
            R.id.tv_time_limit_record -> {
                dlgLimitTime.show()
            }
            R.id.tv_type_limit_record -> {
                dlgLimitType.show()
            }
        }
    }

    override fun onClick(recordType: RecordType) {
        dlgSelectType.dismiss()
        if (recordType.id == null) {
            onLongClick(recordType)
        }
    }

    override fun onLongClick(recordType: RecordType) {
        dlgSelectType.dismiss()

        val intent = Intent(context, RecordTypeEditActivity::class.java)
        intent.putExtra(RecordTypeEditActivity.RECORD_TYPE, recordType.id)
        startActivityForResult(intent, FOR_TYPE_RESULT)
    }

    override fun onClick(item: TimeLimitItem) {
        dlgLimitTime.dismiss()
        vm.setTimeLimit(item.id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                FOR_TYPE_RESULT -> {
                    vm.updateRecordType()
                }
                FOR_RECORD_RESULT -> {

                }
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = RecordFragment()

        val FOR_TYPE_RESULT = 1
        val FOR_RECORD_RESULT = 2
    }
}