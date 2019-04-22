package com.wuruoye.know.ui.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wuruoye.know.R
import com.wuruoye.know.ui.edit.vm.RecordTypeEditViewModel
import com.wuruoye.know.util.InjectorUtil
import com.wuruoye.know.util.ViewFactory

/**
 * Created at 2019-04-22 16:17 by wuruoye
 * Description:
 */
class RecordTypeEditShowFragment : Fragment() {
    private lateinit var llShow: LinearLayout
    private lateinit var vm: RecordTypeEditViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_type_edit_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(activity!!,
            InjectorUtil.recordTypeEditViewModelFactory(context!!))
            .get(RecordTypeEditViewModel::class.java)

        bindView(view)
        subscribeUI()
    }

    private fun bindView(view: View) {
        with(view) {
            llShow = findViewById(R.id.ll_record_type_edit_show)
        }
    }

    private fun subscribeUI() {
        vm.recordType.observe(this, Observer {
            llShow.removeAllViews()
            for (view in it.items) {
                ViewFactory.generateShowView(context!!, view, llShow, true)
            }
        })
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = RecordTypeEditShowFragment()
    }
}