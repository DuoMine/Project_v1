package com.example.project_v1

import java.io.Serializable

//유저 데이터 저장 데이터클래스
data class UserData(var email: String = "", var uid: String = "", var name: String = "") : Serializable {
    val todoList: MutableList<TodoInnerBox> = mutableListOf()//todo의 데이터 저장, 서버에 리스트 통째로 업로드/다운로드함
    val badgeList: MutableList<BadgeInnerBox> = mutableListOf()//뱃지 데이터, 기본적으로 얻은 순서대로 저장
    var equippedBadge: BadgeInnerBox = BadgeInnerBox("none","none", 0)
    fun addBadge(badge:BadgeInnerBox) { //뱃지 추가
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
    }
    fun badgeListsortbyname():MutableList<BadgeInnerBox>{ //뱃지 리스트를 이름순으로 정렬해서 리턴
        val list = badgeList
        list.sortBy {it.badgeName}
        return list
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
    }

    fun removeTodoInnerBox(todoInnerBox: TodoInnerBox) { //todoInnerBox를 리스트에서 삭제. 정렬
        todoList.remove(todoInnerBox)
        todoList.sortBy { it.minute }
        todoList.sortBy { it.hour }
        todoList.sortBy { it.day }
        todoList.sortBy { it.month }
        todoList.sortBy { it.year }
    }

}