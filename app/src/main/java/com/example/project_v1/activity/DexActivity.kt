package com.example.project_v1.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.project_v1.DexViewPagerAdapter
import com.example.project_v1.R
import com.example.project_v1.databinding.ActivityDexBinding
import com.example.project_v1.fragments.FragmentDexTanghulu
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DexActivity: AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dex)

        tabLayout = findViewById(R.id.dexTabLayout)
        viewPager = findViewById(R.id.dexViewPager)

        var adapter = DexViewPagerAdapter(this)
        viewPager.adapter = adapter

        val tabLayoutTextArray = arrayOf("탕후루", "배경", "장식")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach()

    }
}