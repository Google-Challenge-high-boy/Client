package com.highboy.gomantle

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.highboy.gomantle.data.Word

class PrefRepository {

    companion object {
        private lateinit var context: Context
        private val pref: SharedPreferences = context.getSharedPreferences(GlobalConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        private val editor = pref.edit()
        private val gson = Gson()

        fun setContext(context: Context) {
            this.context = context
        }

        fun checkPrefExist(): Boolean {
            return pref.contains(GlobalConstants.USER_EMAIL)
        }

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

        fun putListOfWord(key: String, value: List<Word>) {
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

        fun getListOfWord(key: String): List<Word> {
            val listType = object : TypeToken<List<Word>>() {}.type
            return if(getStringAndReturn(key) != "") {
                gson.fromJson(getStringAndReturn(key), listType)
            } else {
                listOf()
            }
        }
    }
}