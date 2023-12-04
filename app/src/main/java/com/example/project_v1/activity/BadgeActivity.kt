package com.example.project_v1.activity

import android.os.Build
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.project_v1.R
import com.example.project_v1.UserData

class BadgeActivity : AppCompatActivity(){
    var userData= UserData()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badge)
        userData = intent.getSerializableExtra("userData") as UserData
        var badgeGrid = findViewById<GridLayout>(R.id.badgeGridLayout)
        reloadBadge(badgeGrid)

    }
    fun reloadBadge(badgeGrid: GridLayout){
        badgeGrid.removeAllViews()
        for(item in userData.badgeList){
            val inflatedLayout = layoutInflater.inflate(R.layout.badge_inner, badgeGrid,false)
            badgeGrid.addView(inflatedLayout)
            inflatedLayout.findViewById<ImageView>(R.id.badgeInnerImageView).setImageResource(R.drawable.tanghuru_image)
            inflatedLayout.findViewById<TextView>(R.id.badgeInnerTextView).text = "천릿길도 한걸음부터"
        }
    }
}