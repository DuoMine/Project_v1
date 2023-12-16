package com.example.project_v1

import com.example.project_v1.R
import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade


class Decorator(private val context: Activity) : com.prolificinteractive.materialcalendarview.DayViewDecorator {
    private var drawable: Drawable? = null
    private var dates: HashSet<CalendarDay>? = null

    constructor(dates: Collection<CalendarDay>?, context: Activity) : this(context) {
        drawable = context.getDrawable(R.drawable.checked_calendar_background)
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates?.contains(day) == true
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(drawable as Drawable)
    }

    fun setDates(dates: Collection<CalendarDay>?) {
        this.dates = HashSet(dates)
    }

    fun setBackgroundDrawable(drawable: Drawable?) {
        this.drawable = drawable
    }
}