package com.ece493.assignment2;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.activity_settings);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
		Preference pref = findPreference("Filter Size");
	    if (pref instanceof EditTextPreference) {
			EditTextPreference textPref = (EditTextPreference) pref;
			
			pref.setSummary(textPref.getText());
		}
	    
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		@SuppressWarnings("deprecation")
		Preference pref = findPreference(key);
		if (pref instanceof EditTextPreference) {
			EditTextPreference textPref = (EditTextPreference) pref;
			Integer i = Integer.parseInt(textPref.getText());
			if(i%2==0)
			{
				i++;
				textPref.setText(i.toString());
			}
			pref.setSummary(textPref.getText());
		}
	}

}
