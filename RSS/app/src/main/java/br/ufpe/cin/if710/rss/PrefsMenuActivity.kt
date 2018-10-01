package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class PrefsMenuActivity: Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefs_menu)

    }

    class RssPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Carrega preferences a partir de um XML
            addPreferencesFromResource(R.xml.preferencias)


        }
    }

}