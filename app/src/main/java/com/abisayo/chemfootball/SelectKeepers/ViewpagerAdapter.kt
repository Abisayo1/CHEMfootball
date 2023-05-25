package com.abisayo.chemfootball.SelectKeepers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abisayo.chemfootball.SelectPlayers.*

class ViewpagerAdapter1(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 7;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return FragmentI()
            }
            1 -> {
                return FragmentII()
            }
            2 -> {
                return FragmentIII()
            }
            3 -> {
                return FragmentIV()
            }
            4 -> {
                return FragmentV()
            }
            5 -> {
                return FragmentVI()
            }
            6 -> {
                return FragmentVII()
            }

            else -> {
                return Fragment1()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Tab 1"
            }
            1 -> {
                return "Tab 2"
            }
            2 -> {
                return "Tab 3"
            }
        }
        return super.getPageTitle(position)
    }

}
