package com.wuruoye.know.ui.home

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
import com.wuruoye.know.R
import com.wuruoye.know.ui.base.LeakFragment
import com.wuruoye.know.ui.edit.RecordShowActivity
import com.wuruoye.know.ui.home.adapter.ReviewListAdapter
import com.wuruoye.know.ui.home.adapter.scroll.BaseAdapter
import com.wuruoye.know.ui.home.vm.IReviewVM
import com.wuruoye.know.ui.home.vm.ReviewViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_RECORD
import com.wuruoye.know.util.model.RequestCode.RECORD_FOR_TYPE
import com.wuruoye.know.util.model.RequestCode.REVIEW_FOR_SHOW
import com.wuruoye.know.util.model.RequestCode.USER_FOR_BACKUP
import com.wuruoye.know.util.model.RequestCode.USER_FOR_RECORD_TYPE
import com.wuruoye.know.util.model.beans.RecordListItem
import java.util.*

/**
 * Created at 2019/4/9 21:11 by wuruoye
 * Description:
 */
class ReviewFragment : LeakFragment(), BaseAdapter.OnActionListener<RecordListItem>,
    ReviewListAdapter.OnClickListener {
    private lateinit var tvTitle: TextView
    private lateinit var rv: RecyclerView

    private lateinit var vm: IReviewVM
    private var mFirstShow: Boolean = true

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this,
            InjectorUtil.reviewViewModelFactory(context!!))
            .get(ReviewViewModel::class.java)

        mFirstShow = true
        bindView(view)
        initView()
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            tvTitle = findViewById(R.id.tv_title_toolbar)
            rv = findViewById(R.id.rv_review)
        }
    }

    private fun initView() {
        tvTitle.text = "复习"

        val adapter = ReviewListAdapter()
        adapter.setOnActionListener(this)
        adapter.setOnClickListener(this)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeUI() {
        vm.recordList.observe(this, Observer {
            (rv.adapter as ReviewListAdapter).submitList(it)
            if (mFirstShow) {
                mFirstShow = false
                rv.post { rv.scrollToPosition(0) }
            }
        })
    }

    override fun onClick(item: RecordListItem) {
        val list = (rv.adapter as ReviewListAdapter).currentList
        val position = list.indexOf(item)
        val intent = Intent(context, RecordShowActivity::class.java)
        intent.putParcelableArrayListExtra(RecordShowActivity.RECORD_LIST,
            ArrayList(list))
        intent.putExtra(RecordShowActivity.RECORD_POSITION, position)
        startActivityForResult(intent, REVIEW_FOR_SHOW)
    }

    override fun onRight(item: RecordListItem) {
        vm.rememberRecord(item, false)
    }

    override fun onLeft(item: RecordListItem) {
        vm.rememberRecord(item, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                RECORD_FOR_TYPE,
                RECORD_FOR_RECORD,
                USER_FOR_RECORD_TYPE,
                REVIEW_FOR_SHOW,
                USER_FOR_BACKUP -> vm.updateRecordList()
            }
        }
    }
}