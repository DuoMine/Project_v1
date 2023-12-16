package com.example.project_v1.model

import com.example.project_v1.activity.userData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.io.Serializable

data class HabitInnerBox(val title:String="", val tag:Int=0, var isCompleted:String = "false"
                         , val sunCheck:Boolean = false
                         , val monCheck:Boolean = false
                         , val tueCheck:Boolean = false
                         , val wedCheck:Boolean = false
                         , val thuCheck:Boolean = false
                         , val friCheck:Boolean = false
                         , val satCheck:Boolean = false) : Serializable {
    val habitCheckList = mutableListOf<SerializableCalendarDay>()
    fun addHabitCheck(date:CalendarDay){
        habitCheckList.add(date.toSerializableCalendarDay())
        habitCheckList.sortBy { date.toString() }
    }
    fun removeHabitCheck(date: CalendarDay){
        habitCheckList.remove(date.toSerializableCalendarDay())
        FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().uid as String)
            .child("habitList").child("habitCheckList").setValue(habitCheckList)
        habitCheckList.sortBy { date.toString() }
    }

}