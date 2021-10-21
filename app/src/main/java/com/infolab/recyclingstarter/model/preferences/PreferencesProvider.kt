package com.infolab.recyclingstarter.model.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.infolab.recyclingstarter.model.User
import com.infolab.recyclingstarter.model.authResponse

class PreferencesProvider(context: Context) {

        private val appContext = context

    private val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun setUserSettings(user: User){
        preferences.edit()
                .putString("email", user.email)
                .putString("name", user.first_name)
                .putString("phone", user.phone)
                .putString("location", user.room)
                .apply()
    }

    fun setUserTokenAndId(credentials: authResponse){
        preferences.edit()
            .putString("token", credentials.token)
            .putInt("id", credentials.id)
            .apply()
    }

    fun getUserToken() = "Bearer " + preferences.getString("token", "null")

    fun getUserId() = preferences.getInt("id", -1)

    fun userLogOut() = preferences.edit().clear().apply()

}