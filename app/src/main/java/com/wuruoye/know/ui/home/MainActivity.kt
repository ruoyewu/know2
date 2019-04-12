package com.wuruoye.know.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.FragmentAdapter

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var tvTitle: TextView
    private lateinit var vpMain: ViewPager
    private lateinit var bnvBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()
        bindListener()
        initView()
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        vpMain = findViewById(R.id.vp_main)
        bnvBottom = findViewById(R.id.bnv_main)
    }

    private fun bindListener() {
        bnvBottom.setOnNavigationItemSelectedListener(this)
    }

    private fun initView() {
        val adapter = FragmentAdapter(supportFragmentManager,
            arrayOf(
                ReviewFragment.newInstance,
                RecordFragment.newInstance,
                UserFragment.newInstance
            ))
        vpMain.adapter = adapter

        bnvBottom.inflateMenu(R.menu.menu_main)
        bnvBottom.selectedItemId = R.id.menu_record_main
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.menu_review_main -> {
                setViewItem(0, getString(R.string.review))
            }
            R.id.menu_record_main -> {
                setViewItem(1, getString(R.string.record))
            }
            R.id.menu_user_main -> {
                setViewItem(2, getString(R.string.user))
            }
        }
        return true
    }

    private fun setViewItem(position: Int, title: String) {
        tvTitle.text = title
        vpMain.setCurrentItem(position, false)
    }
}
