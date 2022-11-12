package com.example.customproject

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //eferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }




    class SettingsFragment : PreferenceFragmentCompat(),SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_screen, rootKey)


            val sharedPreferences = this@SettingsFragment.context?.let {
                PreferenceManager.getDefaultSharedPreferences(it)
            }
            sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

            val themePref:SwitchPreferenceCompat? = findPreference("theme_mode")
            val textSizePref:SeekBarPreference? = findPreference("text_size")
            if (sharedPreferences != null) {
                themePref?.isChecked = sharedPreferences.getBoolean("theme_mode", true)
                textSizePref?.value = sharedPreferences.getInt("text_size", 20)

            }

        }



        override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
            if(key == "theme_mode"){
                val isNight = preference?.getBoolean(key, false)
                if(isNight == true){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }else if(key == "text_size"){
                val value = preference?.getInt(key, 0)


            }
        }


    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = this@SettingsActivity.let {
            PreferenceManager.getDefaultSharedPreferences(it)
        }
        if(sharedPreferences?.getBoolean("theme_mode", true) == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}