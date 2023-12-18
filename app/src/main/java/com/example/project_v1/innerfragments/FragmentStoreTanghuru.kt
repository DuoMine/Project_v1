package com.example.project_v1.innerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_v1.R
import com.example.project_v1.activity.userData

class FragmentStoreTanghuru: Fragment(), OnClickListener { //탕후루 뽑기 클래스
    private lateinit var choiceButton: Button
    private lateinit var choiceContinueButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_tanghulu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choiceButton = view.findViewById(R.id.btnChoice)
        choiceContinueButton = view.findViewById(R.id.btnContinueChoice)

        choiceButton.setOnClickListener(this)
        choiceContinueButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnChoice->{
                if(userData.gold < 20){
                    Toast.makeText(context,"골드가 부족합니다",Toast.LENGTH_SHORT).show()
                }else{
                    // 뽑힌 탕후루의 정보 값을 DialogFragment에 Bundle()객체로 전달
                    var args = Bundle()

                    args.putString("hint", "골드 20개를 소모해서 \n 뽑기 1회를 진행하시겠습니까?")
                    args.putInt("count", 1)

                    val dialog = ChoiceCheckDialog() //뽑힌 탕후루의 정보를 간략하게 표기할 Dialog 생성
                    dialog.arguments = args
                    dialog.show(parentFragmentManager,"ChoiceDialog")
                }
            }
            R.id.btnContinueChoice->{
                if(userData.gold < 100){
                    Toast.makeText(context,"골드가 부족합니다",Toast.LENGTH_SHORT).show()
                }else{
                    // 뽑힌 탕후루의 정보 값을 DialogFragment에 Bundle()객체로 전달
                    var args = Bundle()

                    args.putString("hint", "골드 100개를 소모해서 \n 뽑기 5회를 진행하시겠습니까?")
                    args.putInt("count", 5)

                    val dialog = ChoiceCheckDialog() //뽑힌 탕후루의 정보를 간략하게 표기할 Dialog 생성
                    dialog.arguments = args
                    dialog.show(parentFragmentManager,"ContinueChoiceDialog")
                }
            }
        }
    }
}