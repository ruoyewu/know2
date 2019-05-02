package com.wuruoye.know.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakActivity
import com.wuruoye.know.ui.edit.RecordTypeEditActivity
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.setting.adapter.BaseSelectAdapter
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
    LeakActivity(),
    View.OnClickListener,
    PopupMenu.OnMenuItemClickListener, BaseSelectAdapter.OnClickListener<RecordType>,
    BaseAdapter.OnActionListener<RecordType> {

    private lateinit var popupMenu: PopupMenu

    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton

    private lateinit var vm: IRecordTypeSetVM
    private var mMoreType: Int = MORE_TYPE_ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_type_set)

        vm = ViewModelProviders.of(this,
            InjectorUtil.recordTypeSetViewModelFactory(applicationContext!!))
            .get(RecordTypeSetViewModel::class.java)

        bindView()
        bindListener()
        initDlg()
        initView()
        subscribeUI()
    }

    private fun bindView() {
        toolbar = findViewById(R.id.toolbar)
        tvTitle = findViewById(R.id.tv_title_toolbar)
        ivBack = findViewById(R.id.iv_back_toolbar)
        ivMore = findViewById(R.id.iv_more_toolbar)
        rv = findViewById(R.id.rv_setting)
        fab = findViewById(R.id.fab_setting)
    }

    private fun bindListener() {
        fab.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivMore.setOnClickListener(this)
    }

    private fun initDlg() {
        popupMenu = PopupMenu(this, ivMore)
        popupMenu.menuInflater.inflate(R.menu.menu_record_type_set, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(this)
    }

    private fun initView() {
        tvTitle.text = "记录类型"
        ivBack.setImageResource(R.drawable.ic_left)
        ivMore.setImageResource(R.drawable.ic_menu)

        val adapter = RecordTypeSetAdapter()
        adapter.setOnClickListener(this)
        adapter.setOnActionListener(this)
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
                when(mMoreType) {
                    MORE_TYPE_ADD -> {
                        val intent = Intent(this, RecordTypeEditActivity::class.java)
                        startActivityForResult(intent, RECORD_TYPE_SET_FOR_ADD)
                    }
                    MORE_TYPE_DELETE -> {
                        val deleteSet = (rv.adapter as RecordTypeSetAdapter).getSelectSet()
                        vm.deleteRecordType(deleteSet.toTypedArray())
                    }
                }
            }
            R.id.iv_back_toolbar -> {
                onBackPressed()
            }
            R.id.iv_more_toolbar -> {
                popupMenu.show()
            }
        }
    }

    override fun onClick(item: RecordType) {
        val intent = Intent(this, RecordTypeEditActivity::class.java)
        intent.putExtra(RecordTypeEditActivity.RECORD_TYPE, item.id)
        startActivityForResult(intent, RECORD_TYPE_SET_FOR_UPDATE)
    }

    override fun onLeft(item: RecordType) {}

    override fun onRight(item: RecordType) {
        vm.deleteRecordType(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_record_type_set, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_add_record_type_set -> {
                mMoreType = MORE_TYPE_ADD
                fab.setImageResource(R.drawable.ic_add)
                (rv.adapter as RecordTypeSetAdapter).setSelectable(false)
            }
            R.id.menu_delete_record_type_set -> {
                mMoreType = MORE_TYPE_DELETE
                fab.setImageResource(R.drawable.ic_check)
                (rv.adapter as RecordTypeSetAdapter).setSelectable(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_add_record_type_set -> {
                mMoreType = MORE_TYPE_ADD
                fab.setImageResource(R.drawable.ic_add)
                (rv.adapter as RecordTypeSetAdapter).setSelectable(false)
            }
            R.id.menu_delete_record_type_set -> {
                mMoreType = MORE_TYPE_DELETE
                fab.setImageResource(R.drawable.ic_check)
                (rv.adapter as RecordTypeSetAdapter).setSelectable(true)
            }
        }
        return true
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

    companion object {
        const val MORE_TYPE_ADD = 1
        const val MORE_TYPE_DELETE = 2
    }
}