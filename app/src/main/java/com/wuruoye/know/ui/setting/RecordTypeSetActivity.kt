package com.wuruoye.know.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.RecordTypeEditActivity
import com.wuruoye.know.ui.setting.adapter.RecordTypeSetAdapter
import com.wuruoye.know.ui.setting.vm.IRecordTypeSetVM
import com.wuruoye.know.ui.setting.vm.RecordTypeSetViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.RequestCode.RECORD_TYPE_SET_FOR_ADD
import com.wuruoye.know.util.model.RequestCode.RECORD_TYPE_SET_FOR_UPDATE
import com.wuruoye.know.util.orm.table.RecordType

/**
 * Created at 2019/4/13 14:48 by wuruoye
 * Description:
 */
class RecordTypeSetActivity :
    AppCompatActivity(),
    View.OnClickListener,
    RecordTypeSetAdapter.OnClickListener {

    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton

    private lateinit var vm: IRecordTypeSetVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_type_set)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordTypeSetViewModelFactory(applicationContext!!))
            .get(RecordTypeSetViewModel::class.java)

        bindView()
        bindListener()
        initView()
        subscribeUI()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        rv = findViewById(R.id.rv_setting)
        fab = findViewById(R.id.fab_setting)
    }

    private fun bindListener() {
        fab.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    private fun initView() {
        tvTitle.text = "记录类型"
        ivBack.setImageResource(R.drawable.ic_left)

        val adapter = RecordTypeSetAdapter()
        adapter.setOnClickListener(this)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun subscribeUI() {
        vm.recordTypeList.observe(this, Observer {
            (rv.adapter as RecordTypeSetAdapter).submitList(it)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_setting -> {
                val intent = Intent(this, RecordTypeEditActivity::class.java)
                startActivityForResult(intent, RECORD_TYPE_SET_FOR_ADD)
            }
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
        }
    }

    override fun onClick(item: RecordType) {
        val intent = Intent(this, RecordTypeEditActivity::class.java)
        intent.putExtra(RecordTypeEditActivity.RECORD_TYPE, item.id)
        startActivityForResult(intent, RECORD_TYPE_SET_FOR_UPDATE)
    }

    override fun onDelClick(item: RecordType) {
        vm.deleteRecordType(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            vm.updateTypeList()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}