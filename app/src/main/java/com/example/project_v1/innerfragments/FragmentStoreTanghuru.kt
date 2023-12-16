package com.example.project_v1.innerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_v1.model.TanghuluModel
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
                    userData.useGold(20)
                    choiceTanghuru()
                }
            }
            R.id.btnContinueChoice->{
                if(userData.gold < 100){
                    Toast.makeText(context,"골드가 부족합니다",Toast.LENGTH_SHORT).show()
                }else{
                    for (i in 0..4){
                        userData.useGold(100)
                        choiceTanghuru() //현재는 연속적으로 Dialog를 띄워주지만 가능하면 순차적으로 띄울 수 있도록 구현 예정
                    }
                }
            }
        }
    }

    private fun choiceTanghuru(){
        //탕후루 뽑기 함수, TanghuruModel 클래스에서 구분한 탕후루를 확률 메소드로 가챠 구현
        //현재는 중복으로 단순 구현한 상태, 리스트들이 많아지면 확률 조정
        var tanghuluModel = TanghuluModel()
        val random = Math.random()
        var tanghulu: Int? = null
        var name: String? = null
        var rank: String? = null
        if (random < 0.5){
            val num = (Math.random()*(tanghuluModel.commonTanghulu.size)).toInt()
            tanghulu = tanghuluModel.commonTanghulu[num]
            name = tanghuluModel.commonName[num]
            rank = "Common"
        } else if (random < 0.9){
            val num = (Math.random()*(tanghuluModel.rareTanghulu.size)).toInt()
            name = tanghuluModel.rareName[num]
            tanghulu = tanghuluModel.rareTanghulu[num]
            rank = "Rare"
        } else{
            val num = (Math.random()*(tanghuluModel.epicTanghulu.size)).toInt()
            tanghulu = tanghuluModel.epicTanghulu[num]
            name = tanghuluModel.epicName[num]
            rank = "Epic"
        }
        // 뽑힌 탕후루의 정보 값을 DialogFragment에 Bundle()객체로 전달
        val args = Bundle()
        args.putInt("tanghuru", tanghulu)
        args.putString("name", name)
        args.putString("rank", rank)

        val dialog = TanghuluDialog() //뽑힌 탕후루의 정보를 간략하게 표기할 Dialog 생성
        dialog.arguments = args
        dialog.show(parentFragmentManager,"TanghuluDialog")
    }

}