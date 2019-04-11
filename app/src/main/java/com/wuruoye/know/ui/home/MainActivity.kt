package com.wuruoye.know.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wuruoye.know.R

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var tvTitle: TextView
    private lateinit var flContent: FrameLayout
    private lateinit var bnvBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()
        bindListener()
        initView()

        bnvBottom.selectedItemId = R.id.menu_record_main
    }

    private fun bindView() {
        tvTitle = findViewById(R.id.tv_title_toolbar)
        flContent = findViewById(R.id.fl_content_main)
        bnvBottom = findViewById(R.id.bnv_main)
    }

    private fun bindListener() {
        bnvBottom.setOnNavigationItemSelectedListener(this)
    }

    private fun initView() {
        bnvBottom.inflateMenu(R.menu.menu_main)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.menu_review_main -> {
                tvTitle.text = getString(R.string.review)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_content_main, ReviewFragment.newInstance)
                    .commit()
            }
            R.id.menu_record_main -> {
                tvTitle.text = getString(R.string.record)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_content_main, RecordFragment.newInstance)
                    .commit()
            }
            R.id.menu_user_main -> {
                tvTitle.text = getString(R.string.user)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_content_main, UserFragment.newInstance)
                    .commit()
            }
        }
        return true
    }
}
