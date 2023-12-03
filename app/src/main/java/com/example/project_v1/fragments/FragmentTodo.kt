package com.example.project_v1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.project_v1.activity.MainActivity
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentTodoBinding

class FragmentTodo : Fragment(), View.OnClickListener, View.OnLongClickListener{ //todo탭
    private  lateinit var binding : FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        binding.userText.text = (activity as MainActivity).userData.name
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myPageBtn.setOnLongClickListener(this)
        binding.addTodoBtn.setOnClickListener(this)
        binding.removeTodoBtn.setOnClickListener(this)
        (activity as MainActivity).reloadTodo(binding.todoInnerLinearLayout)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addTodoBtn -> { //리스트에 todo를 더함
                val fragmentAddTodoDialog = FragmentAddTodoDialog()
                fragmentAddTodoDialog.show(parentFragmentManager,"keyaddtododialog")
            }
            R.id.removeTodoBtn -> { //리스트에서 todo를 제거함
                (activity as MainActivity).removeTodoBox()
                binding.removeTodoBtn.isVisible = false
                binding.addTodoBtn.isVisible = true
            }
        }
    }
    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.myPageBtn -> { //이미지버튼을 길게 누르면 유저 관리 창이 열림
                val fragmentUserDialog = FragmentUserDialog()
                fragmentUserDialog.show(parentFragmentManager,"keyuserdialog")
            }
        }
        return true
    }
}