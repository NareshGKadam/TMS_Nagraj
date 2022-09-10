package com.gisdemo.app.mygisapplication.colorpicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.gisdemo.app.mygisapplication.BuildConfig;

import java.util.Map;

public class PreferenceConnector {
	private static final String PREF_NAME 						= 	BuildConfig.APPLICATION_ID;
	private static final int 	MODE 							= 	Context.MODE_PRIVATE;

	public static final String KEY_UNPAID_PIN_COLOR				=   "KEY_UNPAID_PIN_COLOR";
	public static final String KEY_UNGROUP_POINT_VIEW			=   "KEY_UNGROUP_POINT_VIEW";


	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}


	public static void writeString(Context context, String key, String string) {
		getEditor(context).putString(key, string).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	private static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	private static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void cleanPrefrences(Context context){
		getPreferences(context).edit().clear().apply();
	}

	public static Map<String, ?> getAllKeys(Context context) {
		return getPreferences(context).getAll();
	}
}
