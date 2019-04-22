package com.wuruoye.know .ui.home.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created at 2019/4/12 20:22 by wuruoye
 * Description:
 */
class FragmentAdapter(
    private val fm: FragmentManager,
    private val fragments: Array<Fragment>,
    private val titles: Array<String>? = null
) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fm.beginTransaction().show(fragment).commit()
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment = fragments[position]
        fm.beginTransaction().hide(fragment).commit()
    }
}