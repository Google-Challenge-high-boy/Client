package com.highboy.gomantle

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class PrefRepository(private val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    private fun String.put(history: String) {
        editor.putString(this, history)
        editor.commit()
    }

    fun setWordHistory(history: String) {
        GlobalConstants.PREF_HISTORY.put(history)
    }
}