package com.example.project_v1.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.project_v1.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var fullLayout: LinearLayout
    private lateinit var textLayout: LinearLayout
    private lateinit var imageLayout: LinearLayout
    private lateinit var loginActivity: LoginActivity
    private val PREFERENCE_LOGIN : String = "LoginPreference"
    private var isChanged : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        fullLayout = findViewById(R.id.fullLayout)
        textLayout = findViewById(R.id.textLayout)
        imageLayout = findViewById(R.id.imageLayout)
        loginActivity = LoginActivity()

        imageLayout.visibility = View.INVISIBLE

        Handler(Looper.getMainLooper()).postDelayed({ //시작화면 딜레이를 위한 키워드
            if (!isChanged) {
                changeActivity()
            }
        }, 4000)

        // 외부라이브러리를 활용한 애니메이션 삽입
        YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(textLayout)
        Handler(Looper.getMainLooper()).postDelayed({ //시작화면 딜레이를 위한 키워드
            imageLayout.visibility = View.VISIBLE
            YoYo.with(Techniques.RubberBand).duration(2000).repeat(0).playOn(imageLayout)
        }, 1000)

        fullLayout.setOnClickListener {
            isChanged = true
            changeActivity()
        }

        textLayout.setOnClickListener {
            isChanged = true
            changeActivity()
        }

        imageLayout.setOnClickListener {
            isChanged = true
            changeActivity()
        }
    }

    fun changeActivity() {
        // SharedPreferences 클래스를 통해 Key - Value 값을 반영구적으로 저장
        var sharedPreferences: SharedPreferences = getSharedPreferences(PREFERENCE_LOGIN, Context.MODE_PRIVATE)
        // Key값을 통해 value값 가져오기
        var email : String? = sharedPreferences.getString("email", null)
        var pass : String? = sharedPreferences.getString("pass", null)

        if ((email != null) && (pass != null)) {
            var firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, OnCompleteListener<AuthResult>(){ task ->
                if(task.isSuccessful()){
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
            })} else {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            this.finish()
        }
    }
}