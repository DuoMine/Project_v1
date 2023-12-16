package com.example.project_v1.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.activity.BadgeActivity
import com.example.project_v1.activity.DexActivity
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.databinding.FragmentUserDialogBinding

class FragmentUserDialog : DialogFragment(), View.OnClickListener { //유저 관리 창
    private lateinit var binding: FragmentUserDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gotoBadgeBtn.setOnClickListener(this)
        binding.gotoDexBtn.setOnClickListener(this)
        binding.gotoSettingBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.gotoBadgeBtn -> { //뱃지 관리 창으로 이동
                var intent = Intent(requireContext(),BadgeActivity::class.java)
                intent.putExtra("userData",(activity as MainActivity).userData)
                startActivity(intent)
            }
            R.id.gotoDexBtn -> {
                var intent = Intent(requireContext(), DexActivity::class.java)
                startActivity(intent)
                dialog?.dismiss()
            }
            R.id.gotoSettingBtn -> {
                /*명시적 인텐트 열기*/
            }
        }
    }

}