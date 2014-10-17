package com.testapp.fragment;

import java.util.EmptyStackException;

import com.example.testapp.R;
import com.testapp.activity.SettingsActivity;
import com.testapp.service.WeatherDataListService;
import com.testapp.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragment {

	SettingsActivity activity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);


	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		EditTextPreference addNewCityPreference = (EditTextPreference) findPreference("Add_new_city");

		addNewCityPreference
				.setOnPreferenceChangeListener(onPreferenceChangeListener);

		PreferenceCategory targetCategory = (PreferenceCategory) findPreference("CITYES_LIST");


		targetCategory.removeAll();

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void addCity(String name) {

		PreferenceCategory targetCategory = (PreferenceCategory) findPreference("CITYES_LIST");

		Preference customPref = new Preference(getActivity());
		customPref.setTitle(name);
		// customPref.setSummary("Click this for delete");
		customPref.setPersistent(true);
		// customPref.setOnPreferenceClickListener(onPreferenceClickListener);
		targetCategory.addPreference(customPref);
	}

	// private OnPreferenceClickListener onPreferenceClickListener = new
	// OnPreferenceClickListener() {
	//
	// @Override
	// public boolean onPreferenceClick(Preference preference) {
	//
	// PreferenceCategory targetCategory = (PreferenceCategory)
	// findPreference("CITYES_LIST");
	//
	// targetCategory.removePreference(preference);
	//
	//
	//
	// return false;
	// }
	// };

	private OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			if (!((String) newValue).isEmpty())

				addCity((String) newValue);
			((SettingsActivity) getActivity()).mDataListService.NewCity((String) newValue);

			return false;
		}
	};

}
