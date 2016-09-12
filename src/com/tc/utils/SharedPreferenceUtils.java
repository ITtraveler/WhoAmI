package com.tc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 此类封装了SharedPreference的一些常用操作
 * 
 * @author hgs
 *
 */
public class SharedPreferenceUtils {
	public static final String NAME = "user";
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Context context;
	public static String Key1 = "ISFIRST";// 是否第一次使用

	public SharedPreferenceUtils(Context context) {
		this.context = context;
	}

	// 创建一个SharedPreference
	public void buildPreferences(String name, int mode) {
		preferences = context.getSharedPreferences(name, mode);
	}

	public void editor(String key, String value) {
		editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void editor(String key, boolean value) {
		editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		boolean result = preferences.getBoolean(key, true);
		return result;
	}

	public void clear() {
		editor.clear();
	}
}
