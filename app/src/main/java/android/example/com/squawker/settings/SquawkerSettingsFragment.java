package android.example.com.squawker.settings;

import android.example.com.squawker.R;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


/**
 * Created by lyla on 1/9/17.
 */

public class SquawkerSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.preferences_squawker);

    }
}
