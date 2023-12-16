package com.example.project_v1.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.databinding.FragmentAddHabitDialogBinding
import com.example.project_v1.model.HabitInnerBox

class FragmentAddHabitDialog : DialogFragment() ,View.OnClickListener{
    private lateinit var binding: FragmentAddHabitDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddHabitDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeAddHabitTextView.setOnClickListener(this)
        binding.createNewHabitBtn.setOnClickListener(this)

        val dayOrWeek = arrayOf("Daily","Weekly")
        val dwAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,dayOrWeek)
        binding.dwSpinner.adapter = dwAdapter
        binding.dwSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.dwstrLinearLayout.visibility = View.INVISIBLE
                        binding.dwLinearLayout.visibility = View.INVISIBLE
                    }
                    1 -> {
                        binding.dwstrLinearLayout.visibility = View.VISIBLE
                        binding.dwLinearLayout.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    override fun onClick(v: View?){
        when(v?.id){
            R.id.closeAddHabitTextView -> {
                this.dismiss()
            }
            R.id.createNewHabitBtn -> {
                val habitInnerBox : HabitInnerBox
                if(binding.dwSpinner.selectedItemPosition == 0){
                    habitInnerBox = HabitInnerBox(binding.habitTitleEditText.text.toString()
                        ,System.currentTimeMillis().toInt(),"false", true
                        , true, true, true
                        , true, true, true)
                }else{
                habitInnerBox = HabitInnerBox(binding.habitTitleEditText.text.toString()
                    ,System.currentTimeMillis().toInt(),"false"
                    ,binding.habitCheck0.isChecked
                    ,binding.habitCheck1.isChecked
                    ,binding.habitCheck2.isChecked
                    ,binding.habitCheck3.isChecked
                    ,binding.habitCheck4.isChecked
                    ,binding.habitCheck5.isChecked
                    ,binding.habitCheck6.isChecked)
                }
                (activity as MainActivity).addHabitBox((habitInnerBox))
                this.dismiss()
            }
        }
    }
}