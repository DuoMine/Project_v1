package com.example.project_v1.innerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.project_v1.model.BackgroundModel
import com.example.project_v1.R

class FragmentStoreBackground: Fragment(), OnClickListener { // 배경 구매 클래스
    private var cardView = arrayOfNulls<CardView>(10)
    private var imageView = arrayOfNulls<ImageView>(10)
    private var coin = arrayOfNulls<TextView>(10)
    private var backgroundModel = BackgroundModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_background, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 각각의 배경 id값을 받음
        for (i in cardView.indices){
            cardView[i] = view.findViewById(backgroundModel.cardviewID[i])
            imageView[i] = view.findViewById(backgroundModel.imageViewID[i])
            coin[i] = view.findViewById(backgroundModel.coinID[i])
            cardView[i]!!.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            backgroundModel.cardviewID[0]-> setDialog(0)
            backgroundModel.cardviewID[1]-> setDialog(1)
            backgroundModel.cardviewID[2]-> setDialog(2)
            backgroundModel.cardviewID[3]-> setDialog(3)
            backgroundModel.cardviewID[4]-> setDialog(4)
            backgroundModel.cardviewID[5]-> setDialog(5)
            backgroundModel.cardviewID[6]-> setDialog(6)
            backgroundModel.cardviewID[7]-> setDialog(7)
            backgroundModel.cardviewID[8]-> setDialog(8)
            backgroundModel.cardviewID[9]-> setDialog(9)
        }
    }

    private fun setDialog(index: Int){ // 배경 정보를 Bundle() 객체를 사용하여 DialogFragment에 값을 전달
        var args = Bundle()
        var str: String = "@drawable/bgimg" + (index + 1).toString()
        var resId = resources.getIdentifier(str, "drawable", activity?.packageName)
        var coin = coin[index]?.text.toString()
        var name = backgroundModel.nameID[index]
        var content = backgroundModel.contentID[index]

        args.putInt("image", resId)
        args.putString("name", name)
        args.putString("content", content)
        args.putString("coin", coin)

        val dialog = BackgroundDialog()
        dialog.arguments = args
        dialog.show(parentFragmentManager,"BackgroundDialog")
    }
}