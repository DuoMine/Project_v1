package com.example.project_v1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.project_v1.model.BadgeInnerBox
import com.example.project_v1.fragments.FragmentModifyTodoDialog
import com.example.project_v1.R
import com.example.project_v1.ViewPagerAdapter
import com.example.project_v1.fragments.FragmentCheckHabitDialog
import com.example.project_v1.fragments.FragmentModifyHabitDialog
import com.example.project_v1.model.HabitInnerBox
import com.example.project_v1.model.SerializableCalendarDay
import com.example.project_v1.model.TodoInnerBox
import com.example.project_v1.model.UserData
import com.example.project_v1.model.toSerializableCalendarDay
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager:ViewPager2
    private lateinit var tabLayout:TabLayout
    private lateinit var db: FirebaseDatabase
    val uid = FirebaseAuth.getInstance().currentUser?.uid //현재 사용자의 uid로 db에서 검색
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseDatabase.getInstance() //파이어베이스 데이터베이스
        val userReference = db.reference.child("Users").child(uid!!)

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

        val userReference = db.reference.child("Users").child(uid!!)
        userReference.child("gold").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val tabLayoutTextArray = arrayOf("할일", "습관", "게임", "상점") //4가지 탭 설정
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach() //탭과 프래그먼트들을 연동
    }


    /*여기서부터 todo-----------------------------------------------------------------------*/
    fun reloadTodo(linearLayout: LinearLayout) { //todo프래그먼트에 있는 리니어레이아웃 안의 todoInnerBox객체들을 리로드
        linearLayout.removeAllViews() //리니어레이아웃의 모든 뷰를 제거
        for (item in userData.todoList) {
            val inflatedLayout = layoutInflater.inflate(R.layout.todo_inner, linearLayout, false)
            inflatedLayout.id = item.tag
            linearLayout.addView(inflatedLayout, 0)

            linearLayout.findViewById<CheckBox>(R.id.todoCompleteCheckBox).isChecked = item.isCompleted.toBoolean()
            if(item.isCompleted.toBoolean()){
                inflatedLayout.setBackgroundResource(R.drawable.plus_btn_2_checked)
            }else{
                inflatedLayout.setBackgroundResource(R.drawable.plus_btn_2)
            }
            inflatedLayout.findViewById<TextView>(R.id.todoInnerTextView).text = item.title
            inflatedLayout.findViewById<LinearLayout>(R.id.todoboxLinearLayout).setOnClickListener {
                todoOnClick(it, item)
            }
            inflatedLayout.findViewById<LinearLayout>(R.id.todoboxLinearLayout).setOnLongClickListener {
                todoOnLongClick(it, item)
            }
            inflatedLayout.findViewById<CheckBox>(R.id.todoCheckbox).setOnClickListener {
                todoOnClick(it, item)
            }
            inflatedLayout.findViewById<CheckBox>(R.id.todoCompleteCheckBox).setOnClickListener {
                todoOnClick(it, item)
            }

        } //유저 데이터의 todo리스트에 있는 모든 todoInnerBox를 가져와서 리니어레이아웃 안에 등록
    }
    fun addTodoBox(todoInnerBox: TodoInnerBox){ //TodoInnerBox를 리스트에 추가하고 리로드
        userData.addTodoInnerBox(todoInnerBox)
        reloadTodo(findViewById(R.id.todoInnerLinearLayout))
        userData.addBadge(BadgeInnerBox("New","천리 길도 한걸음부터",R.drawable.tanghuru_image))

    }
    fun removeTodoBox(){ //TodoInnerBox를 리스트에서 제거하고 리로드
        for(i in userData.todoList.reversed()){
            val todoBox = findViewById<LinearLayout>(i.tag)
            val checkBox = todoBox.findViewById<CheckBox>(R.id.todoCheckbox)
            if(checkBox.isChecked){
                findViewById<LinearLayout>(R.id.todoboxLinearLayout).removeView(findViewById(i.tag))
                userData.removeTodoInnerBox(i)

            }
        }
        reloadTodo(findViewById(R.id.todoInnerLinearLayout))
    }
    private fun todoOnLongClick(v: View, todoInnerBox: TodoInnerBox): Boolean {
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
    private fun todoOnClick(v: View, todoInnerBox: TodoInnerBox) {
        val userReference = db.reference.child("Users").child(uid!!)
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
                    val linearLayout = findViewById<LinearLayout>(i.tag)
                    val checkBox = linearLayout.findViewById<CheckBox>(R.id.todoCheckbox)
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
            R.id.todoCompleteCheckBox -> { //체크박스를 체크 시 완료
                val linearLayout = findViewById<LinearLayout>(todoInnerBox.tag)
                if(linearLayout.findViewById<CheckBox>(R.id.todoCompleteCheckBox).isChecked){
                    todoInnerBox.isCompleted = "true"
                    userReference.child("todoList").child(userData.todoList.indexOf(todoInnerBox).toString()).child("completed").setValue("true")
                    linearLayout.setBackgroundResource(R.drawable.plus_btn_2_checked)
                }else{
                    todoInnerBox.isCompleted = "false"
                    userReference.child("todoList").child(userData.todoList.indexOf(todoInnerBox).toString()).child("completed").setValue("false")
                    linearLayout.setBackgroundResource(R.drawable.plus_btn_2)
                }
            }
        }
    }
    /*여기서부터 Habit-------------------------------------------------------------------*/
    fun reloadHabit(linearLayout: LinearLayout){
        linearLayout.removeAllViews() //리니어레이아웃의 모든 뷰를 제거
        for (item in userData.habitList) {
            val inflatedLayout = layoutInflater.inflate(R.layout.habit_inner, linearLayout, false)
            inflatedLayout.id = item.tag
            linearLayout.addView(inflatedLayout, 0)
            inflatedLayout.findViewById<TextView>(R.id.habitInnerTextView).text = item.title
            for(i in item.habitCheckList){
                if(i == CalendarDay.today().toSerializableCalendarDay()){
                    inflatedLayout.findViewById<CheckBox>(R.id.habitCompleteCheckBox).isChecked = true
                    inflatedLayout.findViewById<LinearLayout>(R.id.habitboxLinearLayout).setBackgroundResource(R.drawable.plus_btn_2_checked)
                }
            }
            inflatedLayout.findViewById<LinearLayout>(R.id.habitboxLinearLayout).setOnClickListener {
                habitOnClick(it, item)
            }
            inflatedLayout.findViewById<LinearLayout>(R.id.habitboxLinearLayout).setOnLongClickListener {
                habitOnLongClick(it, item)
            }
            inflatedLayout.findViewById<CheckBox>(R.id.habitCheckbox).setOnClickListener {
                habitOnClick(it, item)
            }
            inflatedLayout.findViewById<CheckBox>(R.id.habitCompleteCheckBox).setOnClickListener {
                habitOnClick(it, item)
            }

        } //유저 데이터의 todo리스트에 있는 모든 todoInnerBox를 가져와서 리니어레이아웃 안에 등록
    }
    fun addHabitBox(habitInnerBox: HabitInnerBox){
        userData.addHabitInnerBox(habitInnerBox)
        reloadHabit(findViewById(R.id.habitInnerLinearLayout))
    }
    fun removeHabitBox(){
        for(i in userData.habitList.reversed()){
            val habitBox = findViewById<LinearLayout>(i.tag)
            val checkBox = habitBox.findViewById<CheckBox>(R.id.habitCheckbox)
            if(checkBox.isChecked){
                findViewById<LinearLayout>(R.id.habitboxLinearLayout).removeView(findViewById(i.tag))
                userData.removeHabitInnerBox(i)

            }
        }
        reloadHabit(findViewById(R.id.habitInnerLinearLayout))
    }
    private fun habitOnLongClick(v: View, habitInnerBox: HabitInnerBox): Boolean {
        when(v.id){
            R.id.habitboxLinearLayout -> {//박스를 길게 누르면 체크박스를 띄움.
                for(item in userData.habitList){
                    findViewById<LinearLayout>(item.tag).findViewById<CheckBox>(R.id.habitCheckbox).isVisible = true
                }//체크가 하나라도 되어있으면 체크박스 보이게 하기
                findViewById<LinearLayout>(habitInnerBox.tag).findViewById<CheckBox>(R.id.habitCheckbox).isChecked = true
                findViewById<Button>(R.id.removeHabitBtn).isVisible = true
                findViewById<Button>(R.id.addHabitBtn).isVisible = false
            }
        }
        return true
    }
    private fun habitOnClick(v: View, habitInnerBox: HabitInnerBox) {
        val userReference = db.reference.child("Users").child(uid!!)
        when(v.id){
            R.id.habitboxLinearLayout -> { //박스를 터치 시 HabitInnerBox 확인 창을 띄움
                val mfragment = FragmentCheckHabitDialog()
                val bundle = Bundle()
                bundle.putSerializable("habitInnerBox",habitInnerBox)
                mfragment.arguments = bundle
                mfragment.show(supportFragmentManager,"keychecktododialog")
            }
            R.id.habitCheckbox -> { //체크박스를 체크 시 수정 버튼이 나타남.
                var checked = false
                for(i in userData.habitList){
                    val linearLayout = findViewById<LinearLayout>(i.tag)
                    val checkBox = linearLayout.findViewById<CheckBox>(R.id.habitCheckbox)
                    if(checkBox!=null&&checkBox.isChecked){
                        checked = true
                    }
                }//리스트의 모든 박스의 체크가 풀려있으면 checked == false
                for(item in userData.habitList){ //체크가 모두 풀리면 체크박스 사라짐
                    findViewById<LinearLayout>(item.tag).findViewById<CheckBox>(R.id.habitCheckbox).isVisible = checked
                }
                findViewById<Button>(R.id.removeHabitBtn).isVisible = checked // 체크가 하나라도 되어있으면 추가 버튼이 삭제 버튼으로 바뀜
                findViewById<Button>(R.id.addHabitBtn).isVisible = !checked
            }
            R.id.habitCompleteCheckBox -> { //체크박스를 체크 시 완료
                val linearLayout = findViewById<LinearLayout>(habitInnerBox.tag)
                if(linearLayout.findViewById<CheckBox>(R.id.habitCompleteCheckBox).isChecked){
                    habitInnerBox.isCompleted = "true"
                    habitInnerBox.addHabitCheck(CalendarDay.today())
                    userReference.child("habitList").setValue(userData.habitList)
                    linearLayout.setBackgroundResource(R.drawable.plus_btn_2_checked)
                }else{
                    habitInnerBox.isCompleted = "false"
                    userReference.child("habitList").child(userData.habitList.indexOf(habitInnerBox).toString()).child("completed").setValue("false")
                    habitInnerBox.removeHabitCheck(CalendarDay.today())
                    linearLayout.setBackgroundResource(R.drawable.plus_btn_2)
                }
            }
        }
    }

}