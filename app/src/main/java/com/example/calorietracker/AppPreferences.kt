package com.example.calorietracker

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    // menyimpan username ke SharedPreferences
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    // mendapatkan username dari SharedPreferences
    fun getUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    fun setUserRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userRole", role)
        editor.apply()
    }

    fun getUserRole(): String? {
        return sharedPreferences.getString("userRole", "")
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

}