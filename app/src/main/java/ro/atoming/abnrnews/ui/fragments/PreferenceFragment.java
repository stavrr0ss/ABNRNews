package ro.atoming.abnrnews.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import ro.atoming.abnrnews.R;
import ro.atoming.abnrnews.network.ArticleSyncUtils;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public String COUNTRY_PREF;
    public String ARTICLE_NUMBER;
    public static boolean isLanguageChanged;
    public String SELECTED_LANGUAGE;
    public String SORT_BY;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        COUNTRY_PREF = sharedPreferences.getString(getString(R.string.pref_country_key),
                getString(R.string.key_country_UnitedStates));
        ARTICLE_NUMBER = sharedPreferences.getString(getString(R.string.pref_pageSize_key),
                getString(R.string.pref_pageSize_defaultValue));
        SELECTED_LANGUAGE = sharedPreferences.getString(getString(R.string.pref_language_key),
                getString(R.string.value_language_Default));
        SORT_BY = sharedPreferences.getString(getString(R.string.pref_sortBy_key),
                getString(R.string.value_sort_date));

        isLanguageChanged = false;

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int countPref = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < countPref; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
        //ArticleSyncUtils.startImmediateSync(getActivity());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_country_key))) {
            COUNTRY_PREF = sharedPreferences.getString(key, getResources().
                    getString(R.string.key_country_UnitedStates));

        }
        if (key.equals(getString(R.string.pref_pageSize_key))) {
            ARTICLE_NUMBER = sharedPreferences.getString(getString(R.string.pref_pageSize_key),
                    getString(R.string.pref_pageSize_defaultValue));
        }
        if (key.equals(getString(R.string.pref_language_key))) {
            SELECTED_LANGUAGE = sharedPreferences.getString(getString(R.string.pref_language_key),
                    getString(R.string.value_language_Default));
            isLanguageChanged = true;
        }
        if (key.equals(getString(R.string.pref_sortBy_key))) {
            SORT_BY = sharedPreferences.getString(getString(R.string.pref_sortBy_key),
                    getString(R.string.value_sort_date));
        }
        Preference preference = findPreference(key);
        if (preference != null) {
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
        ArticleSyncUtils.startImmediateSync(getActivity());
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
