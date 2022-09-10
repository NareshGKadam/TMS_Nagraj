package com.gisdemo.app.mygisapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisdemo.app.mygisapplication.colorpicker.PreferenceConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

//import org.apache.http.message.BasicNameValuePair;

public class Utils {
	public static String path="";

	private static ImageView img;
	private static boolean flagDoc=false;
//	static SharedPrefData mySharedPref;

	public static SharedPreferences settings1;
	public static SharedPreferences.Editor editor1;
	//	static boolean isUpcoming=false;
	static boolean isUpcoming1=true;
	static boolean isVisited=false;
	static String eventId="";

	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";




	//	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
	//			.compile("^[^.][a-zA-Z0-9._-]*@[a-z0-9]+\\.[^.]+[a-z.]+"); //^[^.][a-zA-Z0-9 .]*@[a-z0-9]+\\.[^.]+[a-z.]+



	public final static Pattern PASSWORD_PATTERN = Pattern
			.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*");
	//			("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]))");

	//			.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");


	//	// store value with their key in shared preference
	//	public static void storedInSharedPreference(Context context, String key, String value)
	//	{
	//		SharedPreferences sPreferences=context.getSharedPreferences(Constants.SHARED_PREFERENCE,Context.MODE_PRIVATE);
	//		android.content.SharedPreferences.Editor edit=sPreferences.edit();
	//
	//		edit.putString(key,value);
	//		edit.commit();
	//	}
	//	
	//	// get value from shared preferences
	//	public static String getSharedPreferenceData(Context context, String key, String defValue)
	//	{
	//		String value=defValue;
	//		SharedPreferences sPreferences=context.getSharedPreferences(Constants.SHARED_PREFERENCE,Context.MODE_PRIVATE);
	//		
	//		value = sPreferences.getString(key, defValue);
	//		
	//		return value;
	//	}


	/*public static void scheduleInitialSync(Context context) {

		int longValueSyncIntervalTime = Globals.initialDataSyncTime;
		Calendar calendar = Calendar.getInstance();
		Intent intent = new Intent(context, SyncAllDataService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		//		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		//				calendar.getTimeInMillis(), longValueSyncIntervalTime,
		//				pendingIntent);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

	}

	public static void cancelScheduleInitialSync(Context context) {
		Intent intent = new Intent(context, SyncAllDataService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
	 */


	/*public static void scheduleInitialSync(Context context) {

		int longValueSyncIntervalTime = Globals.initialDataSyncTime;
		Calendar calendar = Calendar.getInstance();
		Intent intent = new Intent(context, SyncAllDataActiveService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				// calendar.getTi meInMillis(), Constants.BG_SERVICE_FREQUENCY,
				calendar.getTimeInMillis(), longValueSyncIntervalTime,
				pendingIntent);

	}*/

	/**
	 * cancel the schedule of background service
	 *
	 */
	//	public static void cancelScheduleInitialSync(Context context) {
	//		Intent intent = new Intent(context, SyncAllDataActiveService.class);
	//		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
	//				intent, 0);
	//		AlarmManager alarmManager = (AlarmManager) context
	//				.getSystemService(Context.ALARM_SERVICE);
	//		alarmManager.cancel(pendingIntent);
	//	}



	/*
	 private Bitmap  initBarcode(Context mcontext) {
			WindowManager mWinMgr = (WindowManager) mcontext.getSystemService(
					Context.WINDOW_SERVICE);
			Bitmap mQRBitmap = null;

			Display mDisplay = mWinMgr.getDefaultDisplay();
			Point mPoint = new Point();
			int mWidth = 0;
			int mHeight = 0;
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
				mDisplay.getSize(mPoint);
				mWidth = mPoint.x;
				mHeight = mPoint.y;
			} else {
				mWidth = mDisplay.getWidth();
				mHeight = mDisplay.getHeight();
			}

			int mSmallerDime = mWidth < mHeight ? mWidth : mHeight;
			//mSmallerDime = mSmallerDime * 3 / 4;

			QRCodeEncoder mQREncoder=new QRCodeEncoder("123123", null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), mSmallerDime);
			try {
				mQRBitmap=mQREncoder.encodeAsBitmap();
			//	mBarcode.setImageBitmap(mQRBitmap);

			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return mQRBitmap;
		}*/


	public static String getStringFromAssetFile(final Context ctx,
			String filePath) {
		try {
			return new String(ReadBytes(ctx.getAssets().open(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setLocale(Activity act,String lang) { 
		Locale myLocale = new Locale(lang.toLowerCase()); 
		Resources res = act.getResources(); 
		DisplayMetrics dm = res.getDisplayMetrics(); 
		Configuration conf = res.getConfiguration(); 
		conf.setLayoutDirection(new Locale(lang));
		conf.locale = myLocale; 
		res.updateConfiguration(conf, dm); 

	} 





	public static byte[] ReadBytes(final InputStream inputStream) {
		int remaining;
		int offset = 0;
		int read;
		try {
			remaining = inputStream.available();
			byte[] data = new byte[remaining];
			do {
				read = inputStream.read(data, offset, remaining);
				offset += read;
				remaining -= read;
			} while (remaining > 0);

			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}



	// get IST time format
	public static String getISTDateTime(String timeZone){  // time zone should be 'GMT' ,'IST'
		try{
			//SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
			DateFormat sd = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			Date date = new Date();
			// TODO: Avoid using the abbreviations when fetching time zones.
			// Use the full Olson zone ID instead.
			sd.setTimeZone(TimeZone.getTimeZone(timeZone));

			return sd.format(date);
		}catch (Exception e){

		}
		return null;
	}

	// get IST time format
	public static Date getDateTime(String dd){  // time zone should be 'GMT' ,'IST'
		try{
			//SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
			DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
			// Date date = new Date();
			// TODO: Avoid using the abbreviations when fetching time zones.
			// Use the full Olson zone ID instead.
			// sd.setTimeZone(TimeZone.getTimeZone(timeZone));
			Date date = format.parse(dd);

			return date;
		}catch (Exception e){

		}
		return null;
	}


	/*	Date date = new Date();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// Use Madrid's time zone to format the date in
	df.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));

	System.out.println("Date and time in Madrid: " + df.format(date));*/


	// Check camera availability
	public boolean cameraAvailable(Context context){
		PackageManager pm = context.getPackageManager();

		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		}
		return false;
	}


	public static boolean validate(final String regx,final String input){
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(regx);
		matcher = pattern.matcher(input);
		return matcher.matches();
	}


	// get the UDID
	@SuppressLint("MissingPermission")
	public static String getUniqueID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial,  androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();


		return deviceId;
	}



	public static Bitmap loadBitmap(Context context, String picName){
		Bitmap b = null;
		FileInputStream fis;
		try {
			fis = context.openFileInput(picName);
			b = BitmapFactory.decodeStream(fis);
			fis.close();

		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		return b;
	}







	public static boolean isAppInstalledOrNot(Context con,String uri)
	{
		PackageManager pm = con.getPackageManager();
		boolean app_installed = false;
		try
		{
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			app_installed = false;
		}
		return app_installed ;
	}


	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/*public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}*/



	public static String getArrayromlist(List<String> ll)
	{
		String[] arrId = new String[ll.size()];
		arrId = ll.toArray(arrId);
		// remove spaces between values in arrId
		StringBuilder sb_arrId = new StringBuilder(128);
		if(arrId.length>0)
		{
			for (String value : arrId) {
				if (sb_arrId.length() > 0) {
					sb_arrId.append(",");
				}
				sb_arrId.append(value);
			}
			sb_arrId.insert(0, "[");
			sb_arrId.append("]");
			Log.i("===========>>>>> ", "sb_arrId========> "	+ sb_arrId);


		}return sb_arrId.toString();

	}


	public static String getArrayfromlist(ArrayList<Map<String,String>> ll, String key1, String key2)
	{
		String arrayForFav="";

		try {
			String[] arrId1 = new String[ll.size()];
			String[] arrId2 = new String[ll.size()];

			for(int i=0;i<ll.size();i++)
			{
				arrId1[i]= ll.get(i).get(key1);
			}

			for(int i=0;i<ll.size();i++)
			{
				arrId2[i]= ll.get(i).get(key2);
			}

			// remove spaces between values in arrId
			StringBuilder sb_arrId1 = new StringBuilder(128);
			StringBuilder sb_arrId2 = new StringBuilder(128);
			if(arrId1.length>0)
			{
				for (String value : arrId1) {
					if(value!=null)
					{
						if (sb_arrId1.length() > 0) {
							sb_arrId1.append(",");
						}
						sb_arrId1.append(value);
					}
				}
				sb_arrId1.insert(0, "[");
				sb_arrId1.append("]");
				Log.i("===========>>>>> ", "sb_arrId1========> "	+ sb_arrId1);
			}

			if(arrId2.length>0)
			{
				for (String value : arrId2) {
					if(value!=null)
					{
						if (sb_arrId2.length() > 0) {
							sb_arrId2.append(",");
						}
						sb_arrId2.append(value);
					}
				}
				sb_arrId2.insert(0, "[");
				sb_arrId2.append("]");
				Log.i("===========>>>>> ", "sb_arrId========> "	+ sb_arrId2);
			}

			arrayForFav=sb_arrId1.toString()+ " " +sb_arrId2.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("=Fav", "ar=====> "	+ arrayForFav);
		return arrayForFav;

	}


	public static String gettimezone()
	{



		String timezone="";

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),Locale.getDefault());
		Date currentLocalTime = calendar.getTime();
		DateFormat date = new SimpleDateFormat("Z");
		String localTime = date.format(currentLocalTime);


		TimeZone tz = TimeZone.getDefault();
		//System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());

		timezone="id:"+tz.getID()+",Name:"+tz.getDisplayName(false, TimeZone.SHORT)+",Locale:"+localTime;

		return timezone;
	}

	public static String getFileName(String path) {

		if ( path == null)
		{
			return null;
		}
		String newpath = path.replace('\\','/');
		int start = newpath.lastIndexOf("/");
		if ( start == -1)
		{
			start = 0;
		}
		else
		{
			start = start + 1;
		}
		String pageName = newpath.substring(start, newpath.length());

		Log.i("################# ","file name ======>>>>>>>>>"+pageName);

		return pageName;
	}



	public static String fileExt(String url) {
		if (url.indexOf("?")>-1) {
			url = url.substring(0,url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf(".") );
			if (ext.indexOf("%")>-1) {
				ext = ext.substring(0,ext.indexOf("%"));
			}
			if (ext.indexOf("/")>-1) {
				ext = ext.substring(0,ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

/*

	public static String getsecuritykey(Context ctx)
	{
		String key,m_androidId,securityKey;
		m_androidId = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);

		key=Globals.apiaccesskey+m_androidId;
		securityKey=Utils.md5(key);
		Log.i("UDID : "+m_androidId,"securityKey : "+securityKey);

		return securityKey;
	}

	public static String getsecuritykey1(Context ctx)
	{
		String key=CryptUtils.md5(Globals.API_PUBLIC_KEY +""+ DeviceUtils.getDeviceId(ctx));
		Log.i("UDID : ","securityKey : "+key);

		return key;
	}
*/










	public static void showToast(Context context,String msg)
	{
		//		LayoutInflater inflater = getLayoutInflater();
		//		View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_root));
		//
		//		TextView text = (TextView) layout.findViewById(R.id.text);
		//		text.setText(msg);
		//
		//		Toast toast = new Toast(context);
		//		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		//		toast.setDuration(Toast.LENGTH_LONG);
		//		toast.setView(layout);
		//		toast.show();


	}


	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{

			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				//Read byte from input stream

				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;

				//Write byte from output stream
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	/*public static String printUrl(List<BasicNameValuePair> params)
	{
		StringBuilder url = new StringBuilder();

		for (int i = 0; i < params.size(); i++)
		{
			BasicNameValuePair nameValuePair = params.get(i);

			if(i != 0)
				url.append("&");
			url.append(nameValuePair.getName());
			url.append("=");
			url.append(nameValuePair.getValue());
		}

		return url.toString();
	}*/



	public static boolean searchData(String searchString, String value)
	{
		if (searchString.length() <= value.length())
		{
			return value.trim().toLowerCase().contains(searchString.toLowerCase());
		}
		return false;
	}


	//	public static void storeDataSet(Context context, String prefsName,
	//			String keyName, Set<String> set) {
	//
	//		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
	//		SharedPreferences.Editor editor = settings.edit();
	//		editor.putStringSet(keyName, set);
	//		editor.commit();
	//	}
	//

	public static void storeData(Context context, String prefsName, String keyName, String value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(keyName, value);
		editor.commit();
	}

	public static void storeData(Context context, String prefsName, String keyName, int value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(keyName, value);
		editor.commit();
	}

	public static void storeData(Context context, String prefsName, String keyName, boolean value)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(keyName, value);
		editor.commit();
	}

	public static void storeDatafortwitter(Context context, String prefsName,String keyName1,String value1 ,String keyName2,String value2,String keyName3, boolean value3)
	{
		settings1 = context.getSharedPreferences(prefsName, 0);
		editor1 = settings1.edit();
		editor1.putString(keyName1, value1);
		editor1.putString(keyName2, value2);
		editor1.putBoolean(keyName3, value3);
		editor1.commit();
	}


	public static boolean getDataBoolean(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getBoolean(keyName, false);
	}
	//	public static String[] getDatafortweet(Context context, String prefsName, String keyName1,String keyName2)
	//	{
	//		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
	//		String arr[]=new String[2];
	//		arr[0]=settings.getString((keyName1, null);
	//		arr[1]=settings.getString((keyName2, null);
	//
	//		return arr;
	//	}





	public static int getDataInt(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getInt(keyName, -1);
	}

	public static String getDataString(Context context, String prefsName, String keyName)
	{
		SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
		return settings.getString(keyName, null);
	}

	//	public static boolean isValidEmailId(String emailId)
	//	{
	//		return(EMAIL_ADDRESS_PATTERN.matcher(emailId).matches());
	//	}






	public  static boolean isValidEmailId(String testEmail) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(testEmail);
		return matcher.matches();

	}



	public  static boolean isNumberOrNot(String input)
	{
		try
		{
			Long.parseLong(input);
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
		return true;
	}







	public static boolean isValidPassword(String paswd)
	{
		return(PASSWORD_PATTERN.matcher(paswd).matches());
	}


	public static boolean checkConfirmPswd(String pswd1, String pswd2)
	{
		return pswd1.trim().equals(pswd2.trim());
	}

	public static boolean intToBoolean(int response)
	{
		return (response == 0) ? true : false;
	}
	/*  Function for creating
	 * */
	 public static final String md5(final String password) {
		 try {

			 MessageDigest digest = MessageDigest
					 .getInstance("MD5");
			 digest.update(password.getBytes());
			 byte messageDigest[] = digest.digest();

			 StringBuffer hexString = new StringBuffer();
			 for (int i = 0; i < messageDigest.length; i++) {
				 String h = Integer.toHexString(0xFF & messageDigest[i]);
				 while (h.length() < 2)
					 h = "0" + h;
				 hexString.append(h);
			 }
			 return hexString.toString();

		 } catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();
		 }
		 return "";
	 }




	 //
	 //	public static void displayMessage(Context context, String message) {
	 //		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	 //		intent.putExtra(Constants.EXTRA_MESSAGE, message);
	 //		context.sendBroadcast(intent);
	 //	}






	 //	public static final String getSecurityKey(final String password) {
	 //		try {
	 //
	 //
	 //			md5("");
	 //			
	 //			//get IMEI Number
	 //			TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
	 //			String m_deviceId = TelephonyMgr.getDeviceId();
	 //
	 //			//get UDID Number
	 //			String m_androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	 //
	 //			Log.i("IMEI : "+m_deviceId,"UDID : "+m_androidId);
	 //
	 //
	 //		}catch(Exception e)
	 //		{
	 //
	 //		}
	 //	}





	 //	public static String getResponseMessage(int responseCode)
	 //	{
	 //		String msg = "";
	 //		switch(responseCode)
	 //		{
	 //
	 //		case 1: 
	 //			msg = Constants.ERROR_CODE_1;
	 //			break;
	 //
	 //		case 2:
	 //			msg = Constants.ERROR_CODE_2;
	 //			break;
	 //
	 //		case 3:
	 //		case 8:
	 //			msg = Constants.ERROR_CODE_3;
	 //			break;
	 //
	 //		case 4:
	 //			msg = Constants.ERROR_CODE_4;
	 //			break;
	 //
	 //		case 5:
	 //			msg = Constants.ERROR_CODE_5;
	 //			break;
	 //		case 7:
	 //			msg = Constants.ERROR_CODE_7;
	 //			break;
	 //
	 //		}
	 //		return msg;
	 //	}

	 public static boolean checkExternalStorageInstalled()
	 {
		 boolean mExternalStorageAvailable = false;
		 boolean mExternalStorageWriteable = false;
		 String state = Environment.getExternalStorageState();

		 if (Environment.MEDIA_MOUNTED.equals(state)) {
			 // We can read and write the media
			 mExternalStorageAvailable = mExternalStorageWriteable = true;
			 return true;
		 } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			 // We can only read the media
			 mExternalStorageAvailable = true;
			 mExternalStorageWriteable = false;
			 return false;
		 } else {
			 // Something else is wrong. It may be one of many other states, but all we need
			 //  to know is we can neither read nor write
			 mExternalStorageAvailable = mExternalStorageWriteable = false;
			 return false;
		 }
	 }



	 public static boolean isFullname(String str) {
		 String expression = "^[a-zA-Z\\s]+"; 
		 return str.matches(expression);        
	 }

	 //full name without special character but allow space and letter
	 public static boolean isFullnameWithoutSpecialChar(String str) {
		 String expression = "^[\\p{L} .'-]+$"; 
		 return str.matches(expression);        
	 }

	 public static boolean HasSpecialCharacter(String value)

	 {
		 for (int i = 0; i < value.length(); i++) {
			 if (!Character.isLetterOrDigit(value.charAt(i)))
				 return true;
		 }
		 return false;
	 }

	 public static boolean HasWhiteSpace(String value)

	 {
		 for (int i = 0; i < value.length(); i++) {
			 if (!Character.isWhitespace(value.charAt(i)))
				 return true;
		 }
		 return false;
	 }

	 public static boolean HasNumber(String v)
	 {
		 for(int i=0;i< v.length(); i++)
		 {
			 if (Character.isDigit(v.charAt(i)))

				 return true;

		 }
		 return false;

	 }


	 	public  static boolean checkeInternetConnection(Context context){
	 		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	 		if (connectivity != null)
	 		{
	 			NetworkInfo[] info = connectivity.getAllNetworkInfo();
	 			if (info != null)
	 				for (int i = 0; i < info.length; i++)
	 						if (info[i].getState() == NetworkInfo.State.CONNECTED)
	 						{
	 							return true;
	 						}
	 		}
	 		return false;
	 	}


/*
	 public static boolean checkeInternetConnection(Context con) {   //isInternetFast
		 ConnectivityManager connectivity = (ConnectivityManager) con
				 .getSystemService(Context.CONNECTIVITY_SERVICE);
		 if (connectivity != null) {
			 NetworkInfo info = connectivity.getActiveNetworkInfo();
			 if ((info != null)) {
				 if(info.isConnected())
				 {
					 try {
						 if(NetworkUtils.isConnectionFast(con,info.getType(), info.getSubtype()))
						 {
							 return true;
						 }else
						 {
							 return false;
						 }
					 } catch (Exception e) {
						 e.printStackTrace();
						 return true;
					 }
				 }else
				 {
					 return false;
				 }
			 }else
			 {
				 return false;
			 }
		 }
		 return false;
	 }*/



	 public static String getPaddedTime(long value)
	 {
		 if((value+"").length()==1)
		 {
			 return "0"+value;
		 }
		 else
		 {
			 return ""+value;
		 }
	 }






	 /*
	public static void logout(Context c,Activity act)
	{

		Toast.makeText(c,R.string.toast_logout,Toast.LENGTH_SHORT).show();

		Utils.cancelScheduleInitialSync(c);


		mStore.clearAll(c);
		mStore.cleadAllFromDB(c);



		Intent i = new Intent(act, XPLogin.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		act.startActivity(i);




		//		SharedPreferences sPreferences=context.getSharedPreferences("userData",Context.MODE_PRIVATE); 
		//		android.content.SharedPreferences.Editor edit=sPreferences.edit();
		//
		//
		//		edit.putString("exhibitor_tab_user_id", "0");
		//		edit.apply();
		//
		//
		//		Intent i = new Intent(context, XPLogin.class);
		//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		//		context.startActivity(i);
		//
		//		Toast.makeText(context,R.string.toast_logout,Toast.LENGTH_SHORT).show();
		//
		//		Utils.cancelScheduleInitialSync(context);


	}

	  */



	 //use this method for changing font 
	 /*
	  * parentLayout: view/parent view to be updated
	  * fontname: defined in constants Constants.FONT_REGULAR / Constants.FONT_ITALIC
	  * 
	  * */

	 public static  void overrideFonts(final Context context, final View parentLayout,String fontname) {
		 //		Log.v("Utils", "overrideFonts");
		 try {
			 if (parentLayout instanceof ViewGroup) {

				 ViewGroup vg = (ViewGroup)parentLayout;
				 //	            Log.v("Utils", "overrideFonts instanceof ViewGroup no of childs:: "+vg.getChildCount());
				 for (int i = 0; i < vg.getChildCount(); i++) {
					 View child = vg.getChildAt(i);
					 overrideFonts(context, child,fontname);
				 }
			 } else if (parentLayout instanceof TextView ) {
				 //	        	Log.v("Utils", "overrideFonts instanceof textvew");

				 ((TextView) parentLayout).setTypeface(Typeface.createFromAsset(context.getAssets(), fontname));
			 }
			 else if (parentLayout instanceof EditText) {

				 //	        	Log.v("Utils", "overrideFonts instanceof textvew");

				 ((EditText) parentLayout).setTypeface(Typeface.createFromAsset(context.getAssets(),fontname));
			 }
			 else if (parentLayout instanceof Button ) {
				 //	        	Log.v("Utils", "overrideFonts instanceof textvew");

				 ((Button) parentLayout).setTypeface(Typeface.createFromAsset(context.getAssets(),fontname));
			 }

		 } catch (Exception e) {

			 Log.e("Utils", "overrideFontserror in override fonts");
			 e.printStackTrace();
		 }
	 }







	 public static void animate(Context mContext,final View view, final int animId,	final View viewToRemove, final View viewToShow) {
		 Animation animation = AnimationUtils.loadAnimation(mContext, animId);
		 view.startAnimation(animation);

		 animation.setAnimationListener(new AnimationListener() {
			 @Override
			 public void onAnimationStart(Animation animation) {

			 }

			 @Override
			 public void onAnimationRepeat(Animation animation) {

			 }

			 @Override
			 public void onAnimationEnd(Animation animation) {
				 if(viewToRemove!=null)
					 viewToRemove.setVisibility(View.GONE);

				 viewToShow.setVisibility(View.VISIBLE);

			 }
		 });

	 }



	 public static void animate2(Context mContext,final View view, final int animId,	final View viewToRemove, final View viewToShow) {
		 Animation animation = AnimationUtils.loadAnimation(mContext, animId);
		 view.startAnimation(animation);

		 animation.setAnimationListener(new AnimationListener() {
			 @Override
			 public void onAnimationStart(Animation animation) {

			 }

			 @Override
			 public void onAnimationRepeat(Animation animation) {

			 }

			 @Override
			 public void onAnimationEnd(Animation animation) {
				 if(viewToRemove!=null)
					 viewToRemove.setVisibility(View.GONE);

				 //viewToShow.setVisibility(View.VISIBLE);

			 }
		 });

	 }



	 /*public static void showCrouton(Activity mContext, int msg,Style style,int id)
	{

		Crouton.makeText(mContext, mContext.getResources().getString(msg), style, id).show();



	}
	  */




	 public static  ArrayList<Map<String, String>> getlist()
	 {
		 ArrayList<Map<String, String>> alist=new ArrayList<Map<String, String>>();
		 for (int i = 0; i < 4; i++)
		 {
			 Map<String, String> map=new HashMap<String, String>();
			 map.put("url","http://img6a.flixcart.com/image/mobile/x/g/f/lava-iris-magnum-x604-125x125-imady3hpwszpmbpf.jpeg");
			 map.put("name","Mobile Expo 2011");
			 map.put("date","21/02/2011");
			 alist.add(map);

			 Map<String, String> map1=new HashMap<String, String>();
			 map1.put("url","http://img6a.flixcart.com/image/mobile/q/e/4/oppo-find-7-x9076-125x125-imadzcjqpdfhdmeh.jpeg");
			 map1.put("name","Computer Expo 2011");
			 map1.put("date","21/02/2011");
			 alist.add(map1);

			 Map<String, String> map2=new HashMap<String, String>();
			 map2.put("url","http://img5a.flixcart.com/image/mobile/j/e/c/nokia-lumia-520-125x125-imadjzywyysdufbc.jpeg");
			 map2.put("name","TECH EXPO 2011");
			 map2.put("date","21/02/2011");
			 alist.add(map2);

			 Map<String, String> map3=new HashMap<String, String>();
			 map3.put("url","http://img5a.flixcart.com/image/mobile/5/q/w/nokia-106-125x125-imadrhhzrfvk2vgk.jpeg");
			 map3.put("name","SAKAL CAREER Expo 2011");
			 map3.put("date","21/02/2011");
			 alist.add(map3);

			 Map<String, String> map4=new HashMap<String, String>();
			 map4.put("url","http://img5a.flixcart.com/image/mobile/7/9/s/asus-zenfone-5-a501cg-125x125-imadxzdec5ptfdkk.jpeg");
			 map4.put("name","LOKMAT Expo 2011");
			 map4.put("date","21/02/2011");
			 alist.add(map4);

			 Map<String, String> map5=new HashMap<String, String>();
			 map5.put("url","http://i.stack.imgur.com/LcMua.png");
			 map5.put("name","BIO INFO TECH Expo 2011");
			 map5.put("date","21/02/2011");
			 alist.add(map5);


		 }
		 return alist;

	 }


	 public static  ArrayList<Map<String, String>> getcataloglist()
	 {
		 ArrayList<Map<String, String>> alist=new ArrayList<Map<String, String>>();
		 for (int i = 0; i < 3; i++)
		 {
			 Map<String, String> map=new HashMap<String, String>();
			 map.put("url","http://thumbs.dreamstime.com/t/logo-car-design-14923115.jpg");
			 map.put("name","Car");

			 alist.add(map);

			 Map<String, String> map1=new HashMap<String, String>();
			 map1.put("url","http://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Pictograms-nps-misc-trucks-2.svg/150px-Pictograms-nps-misc-trucks-2.svg.png");
			 map1.put("name","Truck");

			 alist.add(map1);

			 Map<String, String> map2=new HashMap<String, String>();
			 map2.put("url","http://thumbs.dreamstime.com/t/logo-car-design-14923115.jpg");
			 map2.put("name","Jeep");

			 alist.add(map2);

			 Map<String, String> map3=new HashMap<String, String>();
			 map3.put("url","http://upload.wikimedia.org/wikipedia/commons/thumb/1/18/CH-Zusatztafel-Traktor.svg/170px-CH-Zusatztafel-Traktor.svg.png");
			 map3.put("name","Tractor");

			 alist.add(map3);

			 Map<String, String> map4=new HashMap<String, String>();
			 map4.put("url","http://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Italian_traffic_signs_-_attraversamento_ciclabile_2.svg/120px-Italian_traffic_signs_-_attraversamento_ciclabile_2.svg.png");
			 map4.put("name","cycle");

			 alist.add(map4);

			 Map<String, String> map5=new HashMap<String, String>();
			 map5.put("url","http://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Bus-logo.svg/454px-Bus-logo.svg.png");
			 map5.put("name","Bus");

			 alist.add(map5);


		 }
		 return alist;

	 }

/*

	 public static void handleError(Context myActivity, VolleyError error, int id)
	 {

         Log.e("Volley ERRR","ERRR  "+error.getMessage());

		 if(mySharedPref == null)
			 mySharedPref= new SharedPrefData(myActivity);

		 // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
		 // For AuthFailure, you can re login with user credentials.
		 // For ClientError, 400 & 401, Errors happening on client side when sending api request.
		 // In this case you can check how client is forming the api and debug accordingly.
		 // For ServerError 5xx, you can do retry or handle accordingly.

		 if( error instanceof NetworkError)
		 {
			 Log.e("Volley ERRR","ERRR --- NetworkError");

			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_something_wrong(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
			 }
			 //			Utils.showCrouton(myActivity,R.string.weak_signal,Style.ALERT,id);

			 //		} else if( error instanceof ClientError) 
			 //		{ 
			 //Utils.showCrouton(myActivity,R.string.no_internet,Style.ALERT);

		 } else if( error instanceof ServerError)
		 {
			 //Utils.showCrouton(myActivity,R.string.no_internet,Style.ALERT);
			 Log.e("Volley ERRR","ERRR --- ServerError");
			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_something_wrong(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
			 }


		 } else if( error instanceof AuthFailureError)
		 {	
			 Log.e("Volley ERRR","ERRR --- AuthFailureError");
			 //Utils.showCrouton(myActivity,R.string.no_internet,Style.ALERT);

			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_something_wrong(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
			 }


		 } else if( error instanceof ParseError)
		 {
			 Log.e("Volley ERRR","ERRR --- ParseError");
			 
			 //			Utils.showCrouton(myActivity,R.string.json_parsing_error,Style.ALERT,id);
			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_something_wrong(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
			 }


		 } else if( error instanceof NoConnectionError)
		 {
			 Log.e("Volley ERRR","ERRR --- NoConnectionError");
			 //Toast.makeText(myActivity, "", Toast.LENGTH_SHORT).show();
			 //			Utils.showCrouton(myActivity,R.string.no_internet,Style.ALERT,id);
			 //			Toast.makeText(myActivity,mySharedPref.getlbl_internet_error(), Toast.LENGTH_SHORT).show();

			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_internet_error(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.internet_error, Toast.LENGTH_SHORT).show();
			 }


		 } else if( error instanceof TimeoutError)
		 {	
			 Log.e("Volley ERRR","ERRR --- TimeoutError");
			 //			Utils.showCrouton(myActivity,R.string.weak_signal,Style.ALERT,id);

			 if (mySharedPref.getlbl_something_wrong().length()!=0 && (! mySharedPref.getlbl_something_wrong().equalsIgnoreCase("null"))) {
				 Toast.makeText(myActivity, mySharedPref.getlbl_something_wrong(), Toast.LENGTH_SHORT).show();
			 }else {
				 Toast.makeText(myActivity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
			 }

		 }else{
			 Log.e("Volley ERRR","ERRR --- Else ERROR");
		 }

	 }

*/


	 //	public static BroadcastReceiver updateReceiver = new BroadcastReceiver() {
	 //		@Override
	 //		public void onReceive(Context context, Intent data) {
	 //
	 //			String action=data.getStringExtra("action");
	 //			//	    	Toast.makeText(context,
	 //			//					"utils New Message action: " + action, Toast.LENGTH_LONG).show();
	 //			Log.i("!!!!!!!!!!!!!!!!!","utils New Message action: "+action);
	 //			if( action.equals("com.xporience.exhibitor.action.NEW_DATA_RECEIVED_FROM_SERVER") ) {
	 //				// DO update app
	 //				//	        	getMeetingData();
	 //
	 //				DataBase db=new DataBase(context);
	 //
	 //				SharedPreferences myPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
	 //				String serializedDataFromPreference = Utils.getDataString(context,Constants.SHARED_PREFERENCE, Constants.EXHIBITOR_DATA);
	 //				ExhibitorData exhibitorData = ExhibitorData.create(serializedDataFromPreference);
	 //
	 //				int countNotif=db.getUnreadNotification(exhibitorData.getEXHIBITOR_TAB_USER_ID(), exhibitorData.getUSERTYPE());
	 //				int countMeetings=db.getUnreadMeetings(exhibitorData.getEXHIBITOR_TAB_USER_ID(), exhibitorData.getUSERTYPE());
	 //
	 //				TextView tv_header_noti=(TextView) ((Activity) context).findViewById(R.id.tv_header_notif);
	 //				int totNoti=countNotif+countMeetings;
	 //				if (totNoti!=0)
	 //				{
	 //					tv_header_noti.setText(String.valueOf(totNoti));
	 //				}else
	 //				{
	 //					tv_header_noti.setVisibility(View.GONE);
	 //				}
	 //
	 //
	 //			}
	 //		}
	 //
	 //
	 //	};

	 public static String urlencode(String value) {

		 String ss = null;
		 try {
			 ss = URLEncoder.encode(value, "utf-8");
		 } catch (UnsupportedEncodingException e) {

			 e.printStackTrace();
		 }

		 return ss;
	 }

	/**  share via intent

	  Resources resources = getResources();

    Intent emailIntent = new Intent();
    emailIntent.setAction(Intent.ACTION_SEND);
    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
    emailIntent.setType("message/rfc822");

    PackageManager pm = getPackageManager();
    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
    sendIntent.setType("text/plain");


    Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
    for (int i = 0; i < resInfo.size(); i++) {
        // Extract the label, append it, and repackage it in a LabeledIntent
        ResolveInfo ri = resInfo.get(i);
        String packageName = ri.activityInfo.packageName;
        if(packageName.contains("android.email")) {
            emailIntent.setPackage(packageName);
        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            if(packageName.contains("twitter")) {
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter));
            } else if(packageName.contains("facebook")) {
                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                // will show the <meta content ="..."> text from that page with our link in Facebook.
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook));
            } else if(packageName.contains("mms")) {
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms));
            } else if(packageName.contains("android.gm")) {
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_gmail)));
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));               
                intent.setType("message/rfc822");
            }

            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
        }
    }

    // convert intentList to array
    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
    startActivity(openInChooser);         


	  */





	 public static boolean chkIsFileExist() {
		 return chkIsFileExist();
	 }

	/**  share via intent

	  Resources resources = getResources();

    Intent emailIntent = new Intent();
    emailIntent.setAction(Intent.ACTION_SEND);
    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
    emailIntent.setType("message/rfc822");

    PackageManager pm = getPackageManager();
    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
    sendIntent.setType("text/plain");


    Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
    for (int i = 0; i < resInfo.size(); i++) {
        // Extract the label, append it, and repackage it in a LabeledIntent
        ResolveInfo ri = resInfo.get(i);
        String packageName = ri.activityInfo.packageName;
        if(packageName.contains("android.email")) {
            emailIntent.setPackage(packageName);
        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            if(packageName.contains("twitter")) {
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter));
            } else if(packageName.contains("facebook")) {
                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                // will show the <meta content ="..."> text from that page with our link in Facebook.
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook));
            } else if(packageName.contains("mms")) {
                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms));
            } else if(packageName.contains("android.gm")) {
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_gmail)));
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));               
                intent.setType("message/rfc822");
            }

            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
        }
    }

    // convert intentList to array
    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
    startActivity(openInChooser);       
//	  * @param actionIdAndIsFavList


	  */





	 public static boolean chkIsFileExist(String sdPath) {

		 boolean isExist=false;
		 final File file = new File(sdPath);

		 try{
			 if (file.exists()) 
			 {
				 isExist=true;
			 }else
			 {
				 isExist=false;
			 }
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 return isExist;

	 }

	 public static String getPackageName(Context mContext) {
		 String packageName="";
		 try {
			 packageName=mContext.getPackageName();
		 } catch (Exception e) {
			 e.printStackTrace();
			 packageName="";
		 }
		 return packageName;

	 }
	 
	 
	 public static void httpsTrustManager(String strUrl)
		{
			try {
				URL url=new URL(strUrl);
				HttpsURLConnection connection = (HttpsURLConnection) url
				        .openConnection();
				connection.setHostnameVerifier(HttpsTrustManager.DUMMY_VERIFIER);
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}


	public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
		Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
		mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
		return mDrawable;
	}

	public static int getBackgroundColorFromView (View colorPicker) {
		int color = Color.RED;
		Drawable background = colorPicker.getBackground();
		if (background instanceof ColorDrawable){
			color = ((ColorDrawable) background).getColor();
		}
		return color;
	}

	public static Double truncateDecimal(double x) {
		DecimalFormat df = new DecimalFormat("#.######");
		df.setRoundingMode(RoundingMode.FLOOR);
		return Double.valueOf(df.format(x));
	}

}