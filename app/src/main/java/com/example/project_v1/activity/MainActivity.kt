package com.example.project_v1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.project_v1.fragments.FragmentModifyTodoDialog
import com.example.project_v1.R
import com.example.project_v1.TodoInnerBox
import com.example.project_v1.UserData
import com.example.project_v1.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager:ViewPager2
    private lateinit var tabLayout:TabLayout
    lateinit var db: FirebaseDatabase
    var userData: UserData = UserData()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseDatabase.getInstance() //파이어베이스 데이터베이스

        val userReference = db.reference.child("Users").child(uid!!) //현재 사용자의 uid로 db에서 검색
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserData::class.java)
                userData = data!! // db에서 검색한 데이터를 userData변수에 넣음
                setupUI() // 데이터를 가져온 후 UI를 설정
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리 코드 추가
                Log.e("MainActivity", "Error loading user data: ${databaseError.message}")
            }
        })
    }

    private fun setupUI() {
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        val tabLayoutTextArray = arrayOf("할일", "습관", "게임", "상점") //4가지 탭 설정
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach() //탭과 프래그먼트들을 연동
    }

    fun reloadTodo(linearLayout: LinearLayout) { //todo프래그먼트에 있는 리니어레이아웃 안의 todoInnerBox객체들을 리로드
        linearLayout.removeAllViews() //리니어레이아웃의 모든 뷰를 제거
        for (item in userData.todoList) {
            val inflatedLayout = layoutInflater.inflate(R.layout.todo_inner, linearLayout, false)
            inflatedLayout.id = item.tag
            linearLayout.addView(inflatedLayout, 0)
            inflatedLayout.findViewById<TextView>(R.id.todoInnerTextView).text = item.title
            inflatedLayout.findViewById<LinearLayout>(R.id.todoboxLinearLayout).setOnClickListener {
                onClick(it, item)
            }
            inflatedLayout.findViewById<LinearLayout>(R.id.todoboxLinearLayout).setOnLongClickListener {
                onLongClick(it, item)
            }
            inflatedLayout.findViewById<CheckBox>(R.id.todoCheckbox).setOnClickListener {
                onClick(it, item)
            }
        } //유저 데이터의 todo리스트에 있는 모든 todoInnerBox를 가져와서 리니어레이아웃 안에 등록
    }

    fun addTodoBox(todoInnerBox: TodoInnerBox){ //TodoInnerBox를 리스트에 추가하고 리로드
        userData.addTodoInnerBox(todoInnerBox)
        db.reference.child("Users").child(uid!!).child("todoList").setValue(userData.todoList)//db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
        reloadTodo(findViewById(R.id.todoInnerLinearLayout))
    }
    fun removeTodoBox(){ //TodoInnerBox를 리스트에서 제거하고 리로드
        for(i in userData.todoList.reversed()){
            var todoBox = findViewById<LinearLayout>(i.tag)
            var checkBox = todoBox.findViewById<CheckBox>(R.id.todoCheckbox)
            if(checkBox.isChecked){
                findViewById<LinearLayout>(R.id.todoboxLinearLayout).removeView(findViewById(i.tag))
                userData.removeTodoInnerBox(i)

            }
        }
        db.reference.child("Users").child(uid!!).child("todoList").setValue(userData.todoList) //db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
        reloadTodo(findViewById(R.id.todoInnerLinearLayout))
    }
    private fun onLongClick(v: View, todoInnerBox: TodoInnerBox): Boolean {
        when(v.id){
            R.id.todoboxLinearLayout -> {//박스를 길게 누르면 체크박스를 띄움.
                for(item in userData.todoList){
                    findViewById<LinearLayout>(item.tag).findViewById<CheckBox>(R.id.todoCheckbox).isVisible = true
                }//체크가 하나라도 되어있으면 체크박스 보이게 하기
                findViewById<LinearLayout>(todoInnerBox.tag).findViewById<CheckBox>(R.id.todoCheckbox).isChecked = true
                findViewById<Button>(R.id.removeTodoBtn).isVisible = true
                findViewById<Button>(R.id.addTodoBtn).isVisible = false
            }
        }
        return true
    }
    private fun onClick(v: View, todoInnerBox: TodoInnerBox) {
        when(v.id){
            R.id.todoboxLinearLayout -> { //박스를 터치 시 todoInnerBox 수정 창을 띄움
                val mfragment = FragmentModifyTodoDialog()
                val bundle = Bundle()
                bundle.putSerializable("todoInnerBox",todoInnerBox)
                mfragment.arguments = bundle
                mfragment.show(supportFragmentManager,"keymodifytododialog")
            }
            R.id.todoCheckbox -> { //체크박스를 체크 시 수정 버튼이 나타남.
                var checked = false
                for(i in userData.todoList){
                    var linearLayout = findViewById<LinearLayout>(i.tag)
                    var checkBox = linearLayout.findViewById<CheckBox>(R.id.todoCheckbox)
                    if(checkBox!=null&&checkBox.isChecked){
                        checked = true
                    }
                }//리스트의 모든 박스의 체크가 풀려있으면 checked == false
                for(item in userData.todoList){ //체크가 모두 풀리면 체크박스 사라짐
                    findViewById<LinearLayout>(item.tag).findViewById<CheckBox>(R.id.todoCheckbox).isVisible = checked
                }
                findViewById<Button>(R.id.removeTodoBtn).isVisible = checked // 체크가 하나라도 되어있으면 추가 버튼이 삭제 버튼으로 바뀜
                findViewById<Button>(R.id.addTodoBtn).isVisible = !checked
            }
        }
    }
}