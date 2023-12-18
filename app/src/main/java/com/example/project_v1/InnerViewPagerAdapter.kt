package com.example.project_v1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_v1.innerfragments.FragmentStoreBackground
import com.example.project_v1.innerfragments.FragmentStoreDeco
import com.example.project_v1.innerfragments.FragmentStoreTanghuru
private const val COUNT_FRAGMENT = 3
class InnerViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return COUNT_FRAGMENT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentStoreTanghuru()
            1-> FragmentStoreBackground()
            2-> FragmentStoreDeco()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}