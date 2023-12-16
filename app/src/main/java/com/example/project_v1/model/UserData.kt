package com.example.project_v1.model

import com.example.project_v1.activity.userData
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

//유저 데이터 저장 데이터클래스
data class UserData(var email: String = "", var uid: String = "", var name: String = "", var gold: Int = 100) : Serializable{
    val todoList: MutableList<TodoInnerBox> = mutableListOf()//todo의 데이터 저장, 서버에 리스트 통째로 업로드/다운로드함
    val habitList: MutableList<HabitInnerBox> = mutableListOf()//habit
    val badgeList: MutableList<BadgeInnerBox> = mutableListOf()//뱃지 데이터, 기본적으로 얻은 순서대로 저장
    var equippedBadge: BadgeInnerBox = BadgeInnerBox("none","none", 0)
    fun useGold(coin:Int){
        gold -= coin
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("gold").setValue(gold)
    }
    fun getGold(coin:Int){
        gold += coin
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("gold").setValue(gold)
    }
    fun addBadge(badge: BadgeInnerBox) { //뱃지 추가A
        if (badgeList.isEmpty()) {
            badgeList.add(badge)
            equippedBadge = badge
        } else {
            for (item in badgeList) {
                if (item == badge) {
                    return
                }
            }
            badgeList.add(badge)
        }
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("badgeList").setValue(userData.badgeList)
    }
    fun badgeListsortbyname():MutableList<BadgeInnerBox>{ //뱃지 리스트를 이름순으로 정렬해서 리턴
        val list = badgeList
        list.sortBy {it.badgeName}
        return list
    }
    fun addHabitInnerBox(habitInnerBox: HabitInnerBox){
        if (habitList.isEmpty()) {
            habitList.add(habitInnerBox)
        } else {
            for (item in habitList) {
                if (item.tag == habitInnerBox.tag) {
                    return
                }
            }
            habitList.add(habitInnerBox)
        }
        habitList.sortBy { it.tag }
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("habitList").setValue(userData.habitList)//db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
    }
    fun removeHabitInnerBox(habitInnerBox: HabitInnerBox) { //todoInnerBox를 리스트에서 삭제. 정렬
        habitList.remove(habitInnerBox)
        habitList.sortBy { it.tag }
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("habitList").setValue(userData.habitList) //db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
    }
    fun addTodoInnerBox(todoInnerBox: TodoInnerBox) { //todo리스트에 todoInnerBox를 추가. 하나를 추가할때마다 년/월/일/시/분 순으로 정렬
        if (todoList.isEmpty()) {
            todoList.add(todoInnerBox)
        } else {
            for (item in todoList) {
                if (item.tag == todoInnerBox.tag) {
                    return
                }
            }
            todoList.add(todoInnerBox)
        }
        todoList.sortBy { it.minute }
        todoList.sortBy { it.hour }
        todoList.sortBy { it.day }
        todoList.sortBy { it.month }
        todoList.sortBy { it.year }
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("todoList").setValue(userData.todoList)//db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
    }

    fun removeTodoInnerBox(todoInnerBox: TodoInnerBox) { //todoInnerBox를 리스트에서 삭제. 정렬
        todoList.remove(todoInnerBox)
        todoList.sortBy { it.minute }
        todoList.sortBy { it.hour }
        todoList.sortBy { it.day }
        todoList.sortBy { it.month }
        todoList.sortBy { it.year }
        FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("todoList").setValue(userData.todoList) //db의 Users/유저uid 아래에 userData데이터클래스를 업데이트함
    }

}