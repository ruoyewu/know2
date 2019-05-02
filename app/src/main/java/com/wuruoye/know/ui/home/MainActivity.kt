package com.wuruoye.know.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wuruoye.know.R
import com.wuruoye.know.ui.home.adapter.FragmentAdapter

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var vpMain: ViewPager
    private lateinit var bnvBottom: BottomNavigationView
    private var fragments = arrayListOf<Fragment>(
        ReviewFragment(),
        RecordFragment(),
        UserFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()
        bindListener()
        initView()
    }

    private fun bindView() {
        vpMain = findViewById(R.id.vp_main)
        bnvBottom = findViewById(R.id.bnv_main)
    }

    private fun bindListener() {
        bnvBottom.setOnNavigationItemSelectedListener(this)
    }

    private fun initView() {
        val adapter = FragmentAdapter(supportFragmentManager, fragments.toTypedArray())
        vpMain.adapter = adapter

        bnvBottom.inflateMenu(R.menu.menu_main)
        bnvBottom.selectedItemId = R.id.menu_record_main
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.menu_review_main -> {
                setViewItem(0)
            }
            R.id.menu_record_main -> {
                setViewItem(1)
            }
            R.id.menu_user_main -> {
                setViewItem(2)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (i in 0 until fragments.size) {
            if (i != vpMain.currentItem) {
                fragments[i].onActivityResult(requestCode and 0xffff, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setViewItem(position: Int) {
        vpMain.setCurrentItem(position, false)
    }
}
