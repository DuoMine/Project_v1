package com.example.project_v1.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.cookandroid.testproject.Dialog.ResultDialog
import com.example.project_v1.ExpandableHeightGridView
import com.example.project_v1.ItemAdapter
import com.example.project_v1.ItemDBHelper
import com.example.project_v1.ItemData
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentDexTanghuluBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentDexTanghulu: Fragment(), AdapterView.OnItemClickListener {
    private lateinit var binding : FragmentDexTanghuluBinding
    private lateinit var itemDB: ItemDBHelper
    private lateinit var gridView: ExpandableHeightGridView
    private lateinit var arrayList: ArrayList<ItemData>
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDexTanghuluBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = FirebaseAuth.getInstance().currentUser?.uid!!

        itemDB = ItemDBHelper(context)
        if (!itemDB.checkTypeItems(id, "tanghulu")){
            binding.currentText.text = "현재 가지고 있는 아이템이 없습니다"
        } else {
            binding.currentText.text = ""
        }

        arrayList = itemDB.getItems(id,"tanghulu")

        gridView = view.findViewById(R.id.gridView)
        itemAdapter = ItemAdapter(requireContext(), arrayList)

        gridView.adapter = itemAdapter
        gridView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var itemData: ItemData = arrayList.get(position)

        var name = itemData.name
        var content = itemData.content
        var image = itemData.image

        var args = Bundle()

        args.putString("name", name)
        args.putString("content", content)
        args.putInt("image", image!!)

        val dialog = FragmentItemDialog()
        dialog.arguments = args
        dialog.show(parentFragmentManager,"itemCheckDialog")
    }


}