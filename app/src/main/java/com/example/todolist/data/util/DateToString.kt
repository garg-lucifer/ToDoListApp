package com.example.todolist.data.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateToString {
    @SuppressLint("SimpleDateFormat")
    companion object {
        fun convertDateToString(date : Date) : String{
            val format1 = "MMM dd, yyyy"
            val format2 = "MMM dd, yyyy, hh:mm aaa"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if(dateInfinity.compareTo(date) == 0) "N/A"
            else if(date.seconds == 0){
                val df = SimpleDateFormat(format1)
                df.format(date)
            }else {
                val df = SimpleDateFormat(format2)
                df.format(date)
            }
        }
    }
}