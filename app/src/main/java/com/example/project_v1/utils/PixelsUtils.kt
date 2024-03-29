package com.example.project_v1.utils

import android.content.Context
import android.util.TypedValue

class PixelsUtils {
    fun pixelsToDp(px: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, px.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

}