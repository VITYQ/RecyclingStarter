package com.infolab.recyclingstarter.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.model.preferences.PreferencesProvider

class SettingsActivity : AppCompatActivity() {
    var loggingOut = false
    override fun onCreate(savedInstanceState: Bundle?) {
        loggingOut = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val tab = findViewById<Toolbar>(R.id.topAppBar)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_Settings, SettingsFragment())
                .commit()


        tab.setNavigationOnClickListener {
            onBackPressed()
        }

        tab.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_logout -> {
                    loggingOut = true
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    PreferencesProvider(this).userLogOut()
                    true
                }
                else -> false
            }
        }
    }

}