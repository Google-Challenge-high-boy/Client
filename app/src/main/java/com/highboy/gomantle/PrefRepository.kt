package com.highboy.gomantle

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefRepository(private val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    private fun putStringAndCommit(key: String, string: String) {
        editor.putString(key, string)
        editor.commit()
    }

    private fun putIntAndCommit(key: String, int: Int) {
        editor.putInt(key, int)
        editor.commit()
    }

    private fun putBooleanAndCommit(key: String, boolean: Boolean) {
        editor.putBoolean(key, boolean)
        editor.commit()
    }

    private fun getStringAndReturn(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    private fun getIntAndReturn(key: String): Int {
        return pref.getInt(key, 0)
    }

    private fun getBooleanAndReturn(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        putStringAndCommit(key, value)
    }

    fun putInt(key: String, value: Int) {
        putIntAndCommit(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        putBooleanAndCommit(key, value)
    }

    fun putListOfString(key: String, value: List<String>) {
        putStringAndCommit(key, gson.toJson(value))
    }

    fun getString(key: String): String {
        return getStringAndReturn(key)
    }

    fun getInt(key: String): Int {
        return getIntAndReturn(key)
    }

    fun getBoolean(key: String): Boolean {
        return getBooleanAndReturn(key)
    }

    fun getListOfString(key: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(getStringAndReturn(key), listType)
    }
}