package com.example.project_v1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_v1.activity.GameActivity
import com.example.project_v1.activity.GameActivity2
import com.example.project_v1.activity.GameActivity3

import com.example.project_v1.databinding.FragmentGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class FragmentGame : Fragment() {

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().uid!!).child("gold").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue<Int>()?.let { updateUI(it) }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), GameActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin2.setOnClickListener {
            val intent = Intent(requireContext(), GameActivity2::class.java)
            startActivity(intent)
        }
        binding.btnLogin3.setOnClickListener {
            val intent = Intent(requireContext(), GameActivity3::class.java)
            startActivity(intent)
        }
    }
    private fun updateUI(gold:Int){/*
        binding.goldTextView.text = gold.toString()*/
    }
}

