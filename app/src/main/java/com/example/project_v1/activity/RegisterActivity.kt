package com.example.project_v1.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_v1.R
import com.example.project_v1.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() { // 회원가입 클래스
    private lateinit var userEmail : EditText
    private lateinit var nickname : EditText
    private lateinit var password : EditText
    private lateinit var repassword : EditText
    private lateinit var signup : Button
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var firebaseAuth = FirebaseAuth.getInstance()
        userEmail = findViewById(R.id.idRegisterText)
        nickname = findViewById(R.id.nickRegisterText)
        password = findViewById(R.id.passRegisterText)
        repassword = findViewById(R.id.repassRegisterText)
        signup = findViewById(R.id.btnRegister)
        loginActivity = LoginActivity()

        signup.setOnClickListener() {
            var email: String = userEmail.text.toString()
            var nick: String = nickname.text.toString()
            var pass: String = password.text.toString()
            var repass: String = repassword.text.toString()

            if (email.isEmpty() || nick.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(applicationContext, "다시 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (pass == repass) {
                    Log.d("login", "등록버튼 $email, $pass")
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val email = user?.email as String
                                val uid = user?.uid as String
                                val name = nick.trim()
                                val userData = UserData(email,uid,name)
                                userData.addBadge("Welcome")
                                val ref = FirebaseDatabase.getInstance().getReference("Users")
                                ref.child(uid).setValue(userData)
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                this.finish()
                                Toast.makeText(this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "회원가입 실패",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 비밀번호 재입력란을 엔터키한 후 키보드를 내림
        repassword.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                var manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(repassword.windowToken, 0)
            }
            false
        }
    }
}