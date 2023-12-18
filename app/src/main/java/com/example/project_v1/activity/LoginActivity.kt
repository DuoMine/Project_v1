package com.example.project_v1.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_v1.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() { // 로그인 클래스
    private lateinit var userEmail : EditText
    private lateinit var password : EditText
    private lateinit var btnLogin : Button
    private lateinit var textRegister : TextView
    private lateinit var btnAutoLogin : CheckBox
    private val PREFERENCE_LOGIN : String = "LoginPreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var firebaseAuth = FirebaseAuth.getInstance()
        userEmail = findViewById(R.id.idEdittext)
        password = findViewById(R.id.passEdittext)
        btnLogin = findViewById(R.id.btnLogin)
        textRegister = findViewById(R.id.textRegister)
        btnAutoLogin = findViewById(R.id.btnAutoLogin)

        btnLogin.setOnClickListener (){
            var email : String = userEmail.text.toString()
            var pass : String = password.text.toString()
            firebaseAuth.signInWithEmailAndPassword(email,pass) // firebase 로그인
                .addOnCompleteListener(this, OnCompleteListener<AuthResult>() { task ->
                    if(task.isSuccessful()){
                        if (btnAutoLogin.isChecked){
                            // SharedPreferences 클래스를 통해 Key - Value 값을 반영구적으로 저장
                            var loginPreferences: SharedPreferences = getSharedPreferences(PREFERENCE_LOGIN, Context.MODE_PRIVATE)
                            var autoLoginEdit: SharedPreferences.Editor = loginPreferences.edit()
                            // Edit 객체를 통해 Key - Value 값을 저장
                            autoLoginEdit.putString("email", email)
                            autoLoginEdit.putString("pass", pass)
                            autoLoginEdit.apply()
                        }
                        Toast.makeText(applicationContext, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }else{
                        Toast.makeText(this, "로그인 오류",Toast.LENGTH_SHORT).show()
                    }
                })
        }
        textRegister.setOnClickListener{// 가입 버튼 - 회원가입 화면으로 이동
            Log.d("LoginActivity", "textRegister clicked")
            var intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        // 비밀번호란을 엔터키한 후 키보드를 내림
        password.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                var manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(password.windowToken, 0)
            }
            false
        }
    }
}