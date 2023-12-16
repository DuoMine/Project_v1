package com.example.project_v1.model

import java.io.Serializable

data class TodoInnerBox(var title:String=""
                        ,var year:Int=0, var month:Int=0
                        ,var day:Int=0, var hour:Int=0
                        ,var minute:Int=0,var meridiem:String=""
                        , var tag:Int=0, var isCompleted:String = "false") : Serializable {}