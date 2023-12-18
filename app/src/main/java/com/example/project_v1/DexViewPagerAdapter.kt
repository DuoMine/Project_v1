package com.example.project_v1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_v1.fragments.FragmentDexBackground
import com.example.project_v1.fragments.FragmentDexDeco
import com.example.project_v1.fragments.FragmentDexTanghulu

class DexViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FragmentDexTanghulu()
            1 -> FragmentDexBackground()
            2 -> FragmentDexDeco()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}