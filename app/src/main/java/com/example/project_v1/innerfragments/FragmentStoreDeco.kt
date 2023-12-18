package com.example.project_v1.innerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.cookandroid.testproject.Dialog.ResourceCheckDialog
import com.cookandroid.testproject.Model.DecoModel
import com.example.project_v1.ItemDBHelper
import com.example.project_v1.R
import com.example.project_v1.activity.userData

class FragmentStoreDeco: Fragment(), OnClickListener { // 장식 구매 클래스
    private var cardView = arrayOfNulls<CardView>(10)
    private var imageView = arrayOfNulls<ImageView>(10)
    private var coin = arrayOfNulls<TextView>(10)
    private var decoModel = DecoModel()
    private lateinit var itemDB: ItemDBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_deco, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 각각의 배경 id값을 받음
        for (i in cardView.indices){
            cardView[i] = view.findViewById(decoModel.cardviewID[i])
            imageView[i] = view.findViewById(decoModel.imageViewID[i])
            coin[i] = view.findViewById(decoModel.coinID[i])
            cardView[i]!!.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            decoModel.cardviewID[0]-> setDialog(0)
            decoModel.cardviewID[1]-> setDialog(1)
            decoModel.cardviewID[2]-> setDialog(2)
            decoModel.cardviewID[3]-> setDialog(3)
            decoModel.cardviewID[4]-> setDialog(4)
            decoModel.cardviewID[5]-> setDialog(5)
            decoModel.cardviewID[6]-> setDialog(6)
            decoModel.cardviewID[7]-> setDialog(7)
            decoModel.cardviewID[8]-> setDialog(8)
            decoModel.cardviewID[9]-> setDialog(9)
        }
    }

    private fun setDialog(index: Int){ // 배경 정보를 Bundle() 객체를 사용하여 DialogFragment에 값을 전달
        var id = userData.uid
        var name = decoModel.nameID[index]
        if (itemDB.checkItems(id, name)){
            Toast.makeText(activity, "이미 보유 중입니다", Toast.LENGTH_SHORT).show()
        } else{
            var args = Bundle()
            var str: String = "@drawable/bgimg" + (index + 1).toString()
            var resId = resources.getIdentifier(str, "drawable", activity?.packageName)
            var coin = coin[index]?.text.toString()
            var content = decoModel.contentID[index]

            args.putInt("image", resId)
            args.putString("name", name)
            args.putString("content", content)
            args.putString("coin", coin)
            args.putString("type", "deco")

            val dialog = ResourceCheckDialog()
            dialog.arguments = args
            dialog.show(parentFragmentManager,"BackgroundDialog")
        }
    }
}