package com.example.project_v1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.project_v1.model.BadgeInnerBox
import com.example.project_v1.R
import com.example.project_v1.activity.userData
import com.example.project_v1.databinding.FragmentSetBadgeDialogBinding
import com.google.firebase.database.FirebaseDatabase

class FragmentSetBadgeDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentSetBadgeDialogBinding
    private lateinit var badgeInnerBox: BadgeInnerBox
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        badgeInnerBox = arguments?.getSerializable("badgeInnerBox") as BadgeInnerBox

        binding = FragmentSetBadgeDialogBinding.inflate(inflater, container, false)
        binding.badgeDrawableImageView.setImageResource(badgeInnerBox.badgeDrawableID)
        binding.badgeNameTextView.text = badgeInnerBox.badgeName
        binding.badgeTitleInnerBox.text = badgeInnerBox.badgeTitle
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.badgeChangeBtn.setOnClickListener(this)
        binding.badgeChangeCancelBtn.setOnClickListener(this)
    }
    override fun onClick(v: View?){
        when(v?.id){
            R.id.badgeChangeBtn -> {
                for(item in userData.badgeList){
                    if(item.badgeName == binding.badgeNameTextView.text){
                        userData.equippedBadge = item
                        FirebaseDatabase.getInstance().reference.child("Users").child(userData.uid).child("equippedBadge").setValue(item)
                    }
                }
                this.dismiss()
            }
            R.id.badgeChangeCancelBtn -> {
                dismiss()
            }
        }
    }

}