package com.example.project_v1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.project_v1.R
import com.example.project_v1.model.UserData
import com.google.firebase.auth.FirebaseAuth

class OptionActivity : AppCompatActivity() {
    private lateinit var userData: UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        userData = intent.getSerializableExtra("userData") as UserData

        val logoutBtn = findViewById<Button>(R.id.optionLogoutBtn)
        logoutBtn.setOnClickListener{
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}