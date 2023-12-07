package com.example.project_v1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.cookandroid.testproject.InnerFragment.FragmentStoreTanghuru
import com.example.project_v1.InnerViewPagerAdapter
import com.example.project_v1.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentStore : Fragment() {
    private lateinit var fragmentViewPager: ViewPager2
    private lateinit var fragmentTabLayout: TabLayout
    lateinit var fragment: FragmentStoreTanghuru

    private lateinit var coinText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewPager = view.findViewById(R.id.fragmentViewPager)
        fragmentTabLayout = view.findViewById(R.id.fragmentTabLayout)
        coinText = view.findViewById(R.id.coinText)

        fragment = FragmentStoreTanghuru()

        // Fragment 내에서 TabLayout를 선언
        setUpViewPager(fragmentViewPager)
        val tabLayoutTextArray = arrayOf("탕후루","배경","장식")
        TabLayoutMediator(fragmentTabLayout, fragmentViewPager){ tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach()

    }

    private fun setUpViewPager(viewpager: ViewPager2) {
        var adapter = InnerViewPagerAdapter(childFragmentManager, lifecycle)
        viewpager.adapter = adapter
    }

}