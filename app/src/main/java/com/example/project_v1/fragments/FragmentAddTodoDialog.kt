package com.example.project_v1.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.R
import com.example.project_v1.TodoInnerBox
import com.example.project_v1.databinding.FragmentAddTodoDialogBinding
import java.sql.Date
import java.text.SimpleDateFormat

class FragmentAddTodoDialog : DialogFragment(), View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private lateinit var binding: FragmentAddTodoDialogBinding
    private lateinit var AM_PM:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var now:Long = System.currentTimeMillis()
        Log.d("FragmentAddTodoDialog",now.toString())
        var date: Date = Date(now)
        var dateFormat : SimpleDateFormat = SimpleDateFormat("yyyy년MM월dd일 HH시mm분")
        var getDate : String = dateFormat.format(date).substring(0 until 11)
        var getHour : String = dateFormat.format(date).substring(12 until 14)
        var getMinute : String = dateFormat.format(date).substring(14)

        if(dateFormat.format(date).substring(12 until 14).toInt() < 12){
            AM_PM = "AM"
        }else{
            AM_PM = "PM"
        }
        binding.setTodoDateTextView.setText(getDate)
        binding.radioDateBtn.setText("   "+getDate+"   ")
        binding.setTodoTimeTextView.setText((getHour.toInt()-12).toString()+getMinute)
        binding.radioTimeBtn.setText("   "+(getHour.toInt()-12).toString()+getMinute+" "+AM_PM+"   ")



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeAddTodoTextView.setOnClickListener(this)
        binding.createNewTodoBtn.setOnClickListener(this)
        binding.timeLinearLayout.setOnClickListener(this)
        binding.radioDateBtn.setOnClickListener(this)
        binding.radioTimeBtn.setOnClickListener(this)
        binding.addDatePicker.setOnDateChangedListener(this)
        binding.addTimePicker.setOnTimeChangedListener(this)
    }

    override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.setTodoDateTextView.setText(year.toString()+"년"+monthOfYear.toString()+"월"+dayOfMonth.toString()+"일")
        binding.radioDateBtn.setText("   "+year.toString()+"년"+monthOfYear.toString()+"월"+dayOfMonth.toString()+"일   ")
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.setTodoTimeTextView.setText(hourOfDay.toString()+"시"+minute.toString()+"분")
        if(hourOfDay>12){
            AM_PM = "PM"
            binding.radioTimeBtn.setText("   오후"+(hourOfDay-12).toString()+"시"+minute.toString()+"분 "+AM_PM+"   ")
        }else
            AM_PM = "AM"
            binding.radioTimeBtn.setText("   오전"+(hourOfDay).toString()+"시"+minute.toString()+"분 "+AM_PM+"   ")
    }
    override fun onClick(v: View?){
        when(v?.id){
            R.id.closeAddTodoTextView -> {
                this.dismiss()
            }
            R.id.timeLinearLayout ->{
                binding.setTodoDateTextView.isVisible = binding.setTodoDateTextView.isVisible != true
                binding.setTodoTimeTextView.isVisible = binding.setTodoTimeTextView.isVisible != true
                binding.plusLayout.isVisible = binding.plusLayout.isVisible != true
            }
            R.id.radioDateBtn -> {
                if(binding.addDatePicker.isVisible != true){
                    binding.addDatePicker.isVisible = true
                    binding.addTimePicker.isVisible = false
                }
            }
            R.id.radioTimeBtn -> {
                if(binding.addTimePicker.isVisible != true){
                    binding.addTimePicker.isVisible = true
                    binding.addDatePicker.isVisible = false
                }
            }
            R.id.addTodoAlarmToggleBtn -> {

                if(binding.addTodoAlarmToggleBtn.isChecked == true){
                    binding.addTodoAlarmToggleBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.alarmon))
                }else{
                    binding.addTodoAlarmToggleBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.alarmoff))
                }
            }
            R.id.createNewTodoBtn -> {
                var tag = System.currentTimeMillis().toInt()
                var todoInnerBox = TodoInnerBox(
                    binding.titleEditText.text.toString(),
                    binding.addDatePicker.year,
                    binding.addDatePicker.month,
                    binding.addDatePicker.dayOfMonth,
                    binding.addTimePicker.hour,
                    binding.addTimePicker.minute,
                    AM_PM, tag)
                (activity as MainActivity).addTodoBox(todoInnerBox)
                dismiss()
            }
        }
    }

}