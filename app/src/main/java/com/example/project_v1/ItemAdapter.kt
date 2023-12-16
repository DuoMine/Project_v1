package com.example.project_v1

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ItemAdapter(var context: Context, var arrayList: ArrayList<ItemData>) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = View.inflate(context, R.layout.grid_data, null)

        var images = view.findViewById<ImageView>(R.id.gridImage)
        var names = view.findViewById<TextView>(R.id.gridText)

        var itemData: ItemData = arrayList[position]

        images.setImageResource(itemData.image)
        if (itemData.name.contains("탕후루")){
            if (itemData.name.trim().length > 10){
                var str: String = itemData.name.trimEnd().substring(0, itemData.name.length - 4)
                str += "\r\n" + "탕후루"
                names.text = str
            } else {
                names.text = itemData.name
            }
        } else {
            names.text = itemData.name
        }

        return view
    }
}