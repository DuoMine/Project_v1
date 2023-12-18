package com.example.project_v1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.example.project_v1.ExpandableHeightGridView
import com.example.project_v1.ItemAdapter
import com.example.project_v1.ItemDBHelper
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentDexBackgroundBinding
import com.example.project_v1.model.ItemData
import com.google.firebase.auth.FirebaseAuth

class FragmentDexBackground: Fragment(),OnItemClickListener {
    private lateinit var binding: FragmentDexBackgroundBinding
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
        binding = FragmentDexBackgroundBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = FirebaseAuth.getInstance().currentUser?.uid!!

        itemDB = ItemDBHelper(context)
        if (!itemDB.checkTypeItems(id, "background")){
            binding.currentText.text = "현재 가지고 있는 아이템이 없습니다"
        } else {
            binding.currentText.text = ""
        }

        arrayList = itemDB.getItems(id,"background")

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