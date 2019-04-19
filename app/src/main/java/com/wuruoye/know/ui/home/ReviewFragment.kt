package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
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
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.ReviewListAdapter
import com.wuruoye.know.ui.home.vm.IReviewVM
import com.wuruoye.know.ui.home.vm.ReviewViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.model.beans.RecordListItem

/**
 * Created at 2019/4/9 21:11 by wuruoye
 * Description:
 */
class ReviewFragment : Fragment(), ReviewListAdapter.OnActionListener {
    private lateinit var tvTitle: TextView
    private lateinit var rv: RecyclerView

    private lateinit var vm: IReviewVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this,
            InjectorUtil.reviewViewModelFactory(context!!))
            .get(ReviewViewModel::class.java)

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
        val adapter = ReviewListAdapter()
        adapter.setOnActionListener(this)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeUI() {
        vm.recordList.observe(this, Observer {
            (rv.adapter as ReviewListAdapter).submitList(it)
        })
    }

    override fun onClick(item: RecordListItem) {

    }

    override fun onNotRemember(item: RecordListItem) {

    }

    override fun onRemember(item: RecordListItem) {

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = ReviewFragment()
    }
}