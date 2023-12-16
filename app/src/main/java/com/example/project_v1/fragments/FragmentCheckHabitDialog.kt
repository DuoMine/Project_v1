package com.example.project_v1.fragments

import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.project_v1.Decorator
import com.example.project_v1.databinding.FragmentCheckHabitDialogBinding
import com.example.project_v1.model.HabitInnerBox
import com.example.project_v1.model.toCalendarDay
import com.prolificinteractive.materialcalendarview.CalendarDay


class FragmentCheckHabitDialog : DialogFragment() {
    private lateinit var binding: FragmentCheckHabitDialogBinding
    private lateinit var habitInnerBox: HabitInnerBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        habitInnerBox = arguments?.getSerializable("habitInnerBox") as HabitInnerBox
        binding = FragmentCheckHabitDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var array = mutableListOf<CalendarDay>()
        for (item in habitInnerBox.habitCheckList){
            array.add(item.toCalendarDay())
        }
        var decorator = Decorator(array, activity as FragmentActivity)
        binding.habitCheckCalendarView.addDecorator(decorator)

        binding.habitCheckTitleTextView.setText(habitInnerBox.title)

        val dayOrWeek = arrayOf("Daily","Weekly")
        val dwAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item,dayOrWeek)
        binding.dw2Spinner.adapter = dwAdapter
        binding.dw2Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.dw2strLinearLayout.visibility = View.INVISIBLE
                        binding.dw2LinearLayout.visibility = View.INVISIBLE
                    }
                    1 -> {
                        binding.dw2strLinearLayout.visibility = View.VISIBLE
                        binding.dw2LinearLayout.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.habitmCheck0.isChecked = habitInnerBox.sunCheck
        binding.habitmCheck1.isChecked = habitInnerBox.monCheck
        binding.habitmCheck2.isChecked = habitInnerBox.tueCheck
        binding.habitmCheck3.isChecked = habitInnerBox.wedCheck
        binding.habitmCheck4.isChecked = habitInnerBox.thuCheck
        binding.habitmCheck5.isChecked = habitInnerBox.friCheck
        binding.habitmCheck6.isChecked = habitInnerBox.satCheck
    }

}