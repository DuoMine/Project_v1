package com.example.project_v1

import com.example.project_v1.R
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class Decorator(private val context: Activity) : com.prolificinteractive.materialcalendarview.DayViewDecorator {
    private var drawable: Drawable? = null
    private var dates: HashSet<CalendarDay>? = null

    constructor(dates: Collection<CalendarDay>?, context: Activity) : this(context) {

        drawable = context.getDrawable(R.drawable.daylight)
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates!!.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(drawable!!)
    }

}