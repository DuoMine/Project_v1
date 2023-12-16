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
import androidx.fragment.app.DialogFragment
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.databinding.FragmentModifyHabitDialogBinding
import com.example.project_v1.model.HabitInnerBox

class FragmentModifyHabitDialog : DialogFragment() ,View.OnClickListener{
    private lateinit var binding: FragmentModifyHabitDialogBinding
    private lateinit var habitInnerBox: HabitInnerBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        habitInnerBox = arguments?.getSerializable("habitInnerBox") as HabitInnerBox
        binding = FragmentModifyHabitDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.habitmCheck0.isChecked = habitInnerBox.monCheck
        binding.habitmCheck0.isChecked = habitInnerBox.tueCheck
        binding.habitmCheck0.isChecked = habitInnerBox.wedCheck
        binding.habitmCheck0.isChecked = habitInnerBox.thuCheck
        binding.habitmCheck0.isChecked = habitInnerBox.friCheck
        binding.habitmCheck0.isChecked = habitInnerBox.satCheck
    }
    override fun onClick(v: View?){
        when(v?.id){
            com.example.project_v1.R.id.closeAddHabitTextView -> {
                this.dismiss()
            }
            com.example.project_v1.R.id.createNewHabitBtn -> {
                val habitInnerBox : HabitInnerBox
                if(binding.dw2Spinner.selectedItemPosition == 0){

                }else{

                }
            }
        }
    }

}