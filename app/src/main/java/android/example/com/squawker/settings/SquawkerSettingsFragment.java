package android.example.com.squawker.settings;

import android.content.SharedPreferences;
import android.example.com.squawker.R;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by lyla on 1/9/17.
 */

public class SquawkerSettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.preferences_squawker);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        if (preference != null && preference instanceof SwitchPreferenceCompat) {
            boolean isOn = sharedPreferences.getBoolean(key, false);
            if (isOn) {
                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.e("PreferenceFrag", "Subscribing to " + key);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.e("PreferenceFrag", "Unsubscribing to " + key);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
