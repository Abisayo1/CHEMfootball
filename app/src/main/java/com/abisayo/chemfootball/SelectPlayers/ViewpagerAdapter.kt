package com.abisayo.chemfootball.SelectPlayers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abisayo.chemfootball.SelectKeepers.*
import com.abisayo.chemfootball.SelectPlayers.*

class ViewpagerAdapter2(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 8;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return Fragment1()
            }
            1 -> {
                return Fragment2()
            }
            2 -> {
                return Fragment3()
            }
            3 -> {
                return Fragment4()
            }
            4 -> {
                return Fragment5()
            }
            5 -> {
                return Fragment6()
            }
            6 -> {
                return Fragment7()
            }
            7 -> {
                return Fragment8()
            }
            else -> {
                return FragmentI()
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
