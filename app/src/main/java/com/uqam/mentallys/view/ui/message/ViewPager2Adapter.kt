package com.uqam.mentallys.view.ui.message

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2Adapter(manager: FragmentManager,
                        lifecycle: Lifecycle,
                        private val tabLayout: TabLayout? = null,
                        private val viewPager2: ViewPager2? = null)
    : FragmentStateAdapter(manager, lifecycle) {
    private val mFragmentList : ArrayList<Fragment> = arrayListOf()
    private val mFragmentTitleList : ArrayList<String> = arrayListOf()

    fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    private fun setTabLayoutMediator(){
        TabLayoutMediator(tabLayout!!, viewPager2!!) { tab, position ->
            tab.text = mFragmentTitleList[position]
        }.attach()
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    fun attach(){
        viewPager2?.adapter = this
        tabLayout?.let {
            viewPager2?.let {
                setTabLayoutMediator()
            }
        }
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}