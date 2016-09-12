package com.tc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �����װ��SharedPreference��һЩ���ò���
 * 
 * @author hgs
 *
 */
public class SharedPreferenceUtils {
	public static final String NAME = "user";
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Context context;
	public static String Key1 = "ISFIRST";// �Ƿ��һ��ʹ��

	public SharedPreferenceUtils(Context context) {
		this.context = context;
	}

	// ����һ��SharedPreference
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
