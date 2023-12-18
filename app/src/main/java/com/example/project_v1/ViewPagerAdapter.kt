package com.example.project_v1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project_v1.fragments.FragmentGame
import com.example.project_v1.fragments.FragmentHabit
import com.example.project_v1.fragments.FragmentStore
import com.example.project_v1.fragments.FragmentTodo
private const val NUM_PAGES = 4

class ViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentTodo()
            1-> FragmentHabit()
            2-> FragmentGame()
            3-> FragmentStore()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}