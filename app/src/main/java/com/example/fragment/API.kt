package com.example.fragment

import android.content.Context
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object API {
    const val Base = "https://dapi.kakao.com"

    const val  HEADER = "KakaoAK 6ee85845a040eb6d5437a2142d9387cf"

    const val  SAVE_NAME="com.example.fragment.ImageSearch"

    const val KEY="Last_Keyword"

    fun getDateFromTimestampWithFormat(
        timestamp: String?,
        fromFormatformat: String?,
        toFormatformat: String?
    ): String {
        var date: Date? = null
        var res = ""
        try {
            val format = SimpleDateFormat(fromFormatformat)
            date = format.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("jbdate", "getDateFromTimestampWithFormat date >> $date")

        val df = SimpleDateFormat(toFormatformat)
        res = df.format(date)
        return res
    }

    fun saveLastSearch(context: Context, query: String) {
        val prefs = context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, query).apply()
    }

    fun getLastSearch(context: Context): String? {
        val prefs = context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY, null)
    }

}
