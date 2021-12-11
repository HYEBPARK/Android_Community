package com.cookandroid.mylife.utils

import java.text.SimpleDateFormat
import java.util.*

class GetDate {
    companion object {
        // 현재 날짜와 시간을 가져오는 함수
        fun getTime(): String {
            val currentDateTime = Calendar.getInstance().time
            val dateFormat =
                SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
            return dateFormat
        }
    }
}