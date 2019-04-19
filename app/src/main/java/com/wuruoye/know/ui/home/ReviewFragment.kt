package com.wuruoye.know.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R

/**
 * Created at 2019/4/9 21:11 by wuruoye
 * Description:
 */
class ReviewFragment : Fragment(){
    private lateinit var tvTitle: TextView
    private lateinit var rv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        val newInstance = ReviewFragment()
    }
}