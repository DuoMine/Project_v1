package com.example.project_v1.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.project_v1.model.BadgeInnerBox
import com.example.project_v1.R
import com.example.project_v1.model.UserData
import com.example.project_v1.fragments.FragmentSetBadgeDialog

class BadgeActivity : AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badge)
        userData = intent.getSerializableExtra("userData") as UserData
        val badgeGrid = findViewById<GridLayout>(R.id.badgeGridLayout)

        val badgeSpinner = findViewById<Spinner>(R.id.badgeSpinner)//정렬 순서 메뉴
        val badgesort :Array<String>  = arrayOf("획득 순","이름 순")
        val badgeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,badgesort)
        badgeSpinner.adapter = badgeAdapter
        badgeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        reloadBadge(badgeGrid,userData.badgeList)
                    }
                    1 -> {
                        reloadBadge(badgeGrid, userData.badgeListsortbyname())
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        reloadBadge(badgeGrid,userData.badgeList)//정렬 순서 메뉴
        val badgeBackBtn = findViewById<Button>(R.id.badgeBackBtn)//뒤로가기 버튼
        badgeBackBtn.setOnClickListener{
            this.finish()
        }
    }
    fun reloadBadge(badgeGrid: GridLayout,list: MutableList<BadgeInnerBox>){
        badgeGrid.removeAllViews()
        for(item in list){
            val inflatedLayout = layoutInflater.inflate(R.layout.badge_inner, badgeGrid,false)
            badgeGrid.addView(inflatedLayout)
            inflatedLayout.findViewById<ImageView>(R.id.badgeInnerImageView).setImageResource(item.badgeDrawableID)
            inflatedLayout.findViewById<TextView>(R.id.badgeInnerTextView).text = item.badgeTitle
            inflatedLayout.findViewById<LinearLayout>(R.id.badgeInnerLinearLayout).setOnClickListener {
                val fragmentUserDialog = FragmentSetBadgeDialog()
                val bundle = Bundle()
                bundle.putSerializable("badgeInnerBox",item)
                fragmentUserDialog.arguments = bundle
                fragmentUserDialog.show(supportFragmentManager,"keysetbadgedialog")
            }
        }
    }


}