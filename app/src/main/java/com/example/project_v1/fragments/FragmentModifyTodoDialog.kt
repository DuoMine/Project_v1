package com.example.project_v1.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.activity.userData
import com.example.project_v1.model.TodoInnerBox
import com.example.project_v1.databinding.FragmentModifyTodoDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FragmentModifyTodoDialog : DialogFragment(), View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private lateinit var binding: FragmentModifyTodoDialogBinding
    private lateinit var todoInnerBox: TodoInnerBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        todoInnerBox = arguments?.getSerializable("todoInnerBox") as TodoInnerBox
        binding = FragmentModifyTodoDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.modifyTitleEditText.setText(todoInnerBox.title)
        binding.radioModifyDateBtn.setText("   "+todoInnerBox.year+"년"+todoInnerBox.month+"월"+todoInnerBox.day+"일   ")
        binding.radioModifyTimeBtn.setText("   "+todoInnerBox.hour+"시"+todoInnerBox.minute+"분 "+todoInnerBox.meridiem+"   ")
        binding.modifyDatePicker.updateDate(todoInnerBox.year,todoInnerBox.month,todoInnerBox.day)
        binding.modifyTimePicker.hour = todoInnerBox.hour
        binding.modifyTimePicker.minute = todoInnerBox.minute
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.modifyTimePicker.setOnTimeChangedListener(this)
        binding.modifyDatePicker.setOnDateChangedListener(this)
        binding.radioModifyDateBtn.setOnClickListener(this)
        binding.radioModifyTimeBtn.setOnClickListener(this)
        binding.modifyCheckTodoBtn.setOnClickListener(this)
        binding.modifyCancelTodoBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.modifyCheckTodoBtn -> {
                todoInnerBox.title = binding.modifyTitleEditText.text.toString()
                todoInnerBox.year = binding.modifyDatePicker.year
                todoInnerBox.month = binding.modifyDatePicker.month
                todoInnerBox.day = binding.modifyDatePicker.dayOfMonth
                todoInnerBox.hour = binding.modifyTimePicker.hour
                todoInnerBox.minute = binding.modifyTimePicker.minute
                (activity as MainActivity).reloadTodo((activity as MainActivity).findViewById(R.id.todoInnerLinearLayout))
                FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().uid!!).child("todoList").setValue(
                    userData.todoList)//db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
                this.dismiss()
            }
            R.id.modifyCancelTodoBtn -> {
                this.dismiss()
            }
            R.id.radioModifyDateBtn -> {
                binding.modifyDatePicker.isVisible = true
                binding.modifyTimePicker.isVisible = false
            }
            R.id.radioModifyTimeBtn -> {
                binding.modifyTimePicker.isVisible = true
                binding.modifyDatePicker.isVisible = false
            }
        }
    }

    override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.radioModifyDateBtn.setText("   "+year.toString()+"년"+monthOfYear.toString()+"월"+dayOfMonth.toString()+"일   ")
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(hourOfDay>12){
            binding.radioModifyTimeBtn.setText("   "+(hourOfDay).toString()+"시"+minute.toString()+"분 PM   ")
        }else
            binding.radioModifyTimeBtn.setText("   "+(hourOfDay).toString()+"시"+minute.toString()+"분 AM   ")
    }

}
