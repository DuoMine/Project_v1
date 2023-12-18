package com.example.project_v1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.project_v1.R
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.activity.userData
import com.example.project_v1.databinding.FragmentHabitBinding
import com.example.project_v1.databinding.FragmentTodoBinding
import com.example.project_v1.model.toSerializableCalendarDay
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.prolificinteractive.materialcalendarview.CalendarDay

class FragmentHabit : Fragment(), View.OnClickListener { //Habit탭
    private lateinit var binding: FragmentHabitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitBinding.inflate(inflater, container, false)
        var num = 0
        for(item in userData.habitList){
            if(item.habitCheckList.contains(CalendarDay.today().toSerializableCalendarDay())){
                num++
            }
        }
        binding.userTexth.text = userData.name
        binding.progressBar.progress = num * 100/userData.habitList.size
        binding.progressTextView.text = (num * 100/userData.habitList.size).toString() + "%"
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().uid!!).child("gold").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue<Int>()?.let { updateUI(it) }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.userTitleTexth.text = userData.equippedBadge.badgeName
        binding.goldHabitTextView.text = userData.gold.toString()
        binding.myPageBtnh.setOnClickListener(this)
        binding.addHabitBtn.setOnClickListener(this)
        binding.removeHabitBtn.setOnClickListener(this)
        (activity as MainActivity).reloadHabit(binding.habitInnerLinearLayout)
    }
    private fun updateUI(gold:Int){
        binding.goldHabitTextView.text = gold.toString()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addHabitBtn -> { //리스트에 todo를 더함
                val fragmentAddHabitDialog = FragmentAddHabitDialog()
                fragmentAddHabitDialog.show(parentFragmentManager, "keyaddHabitdialog")
            }

            R.id.removeHabitBtn -> { //리스트에서 todo를 제거함
                (activity as MainActivity).removeHabitBox()
                binding.removeHabitBtn.isVisible = false
                binding.addHabitBtn.isVisible = true
            }

            R.id.myPageBtnh -> { //이미지버튼을 누르면 유저 관리 창이 열림
                val fragmentUserDialog = FragmentUserDialog()
                fragmentUserDialog.show(parentFragmentManager, "keyuserdialog")
            }
        }
    }
}