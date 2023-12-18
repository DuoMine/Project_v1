package com.example.project_v1.model

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.io.Serializable

data class SerializableCalendarDay(
    val year: Int=0,
    val month: Int=0,
    val day: Int=0
) : Serializable {
    // 필요에 따라 다른 메서드나 생성자를 추가할 수 있음
}

// CalendarDay를 SerializableCalendarDay로 변환하는 확장 함수
fun CalendarDay.toSerializableCalendarDay(): SerializableCalendarDay {
    return SerializableCalendarDay(year, month, day)
}

// SerializableCalendarDay를 CalendarDay로 변환하는 확장 함수
fun SerializableCalendarDay.toCalendarDay(): CalendarDay {
    return CalendarDay.from(year, month, day)
}