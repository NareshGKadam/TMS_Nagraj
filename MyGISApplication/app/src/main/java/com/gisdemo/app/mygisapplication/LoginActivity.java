package com.gisdemo.app.mygisapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    final String tag_json_obj = "string_req";
    EditText et, et2;
    private Context mContext;
    private String IMEI_NO = "";
    private final int PHONE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mContext = this;
        IMEI_NO = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Login","IMEI_NO === " + IMEI_NO);

//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        String login = "";
        SharedPreferences prefs = getSharedPreferences("AppData", MODE_PRIVATE);
        String restoredText = prefs.getString("login", null);
        if (restoredText != null) {
            login = prefs.getString("login", "0");//"No name defined" is the default value.
        }

        if (login.equals("1")) {
            LoginActivity.this.finish();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);

        } else {
//            setContentView(R.layout.activity_login);
        }

      /*  if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/


        et = (EditText) findViewById(R.id.edt_emwfds);
        et2 = (EditText) findViewById(R.id.edt_pin);


        Button b = (Button) findViewById(R.id.btn_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = et.getText().toString();
                String pass = et2.getText().toString();


                if (id.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter user name", Toast.LENGTH_SHORT).show();//value

                } else if (pass.length() == 0) {

                    Toast.makeText(LoginActivity.this, "Please enter passward", Toast.LENGTH_SHORT).show();//value

                } else {
                    //  Tag used to cancel the request
                    //  http://172.16.25.166:8081/APIcalll/loginAPI?userid=nmctest&pwd=mars1234
//                    String url = "http://203.129.203.8:8080/APIcalll/loginAPI?userid="+id+"&pwd=" + pass+"";
//                    String url = "http://203.129.203.8:8080/APIcalll/loginAPI?userid=nmctest&pwd=mars1234";

                    String url = "http://114.79.182.179:8080/APIcalll/loginAPI?userid=" + id + "&pwd=" + pass + ""+"&IMEI="+IMEI_NO;

                    final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Login","response --> "+ response.toString());
                                    System.out.println("response ---> " + response.toString());
                                    pDialog.hide();
                                    String status = response.optString("Status");
                                    if (status.equalsIgnoreCase("success")
                                    || status.toLowerCase().contains("user is successfullly logged in")) {

                                        SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
                                        editor.putString("login", "1");
                                        editor.putString("userid", "" + id);

                                        editor.apply();


                                        LoginActivity.this.finish();
                                        //    Intent i = new Intent(LoginActivity.this, ActivitySearchByIndex.class);
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);

                                    }else {
                                        Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                                    }


                                  /*  if (response.isNull("PropDetail")) {
                                        Toast.makeText(MainActivity.this,"Property not found",Toast.LENGTH_SHORT).show();//value

                                    }else{
                                        Intent i = new Intent(MainActivity.this, TaxDetails.class);
                                        i.putExtra("json", response.toString());
                                        startActivity(i);

                                    }*/

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("--->>", "Error: " + error.getMessage());
                            // hide the progress dialog
//                            Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
                            pDialog.hide();
                        }
                    });

// Adding request to request queue
                    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                }


            }
        });

    }


    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED;
    }

    private void getDeviceIMEI() {
        if (checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_PERMISSION_CODE);
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI_NO = telephonyManager.getDeviceId();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PHONE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                IMEI_NO = telephonyManager.getDeviceId();
            }
        }
    }

    private void callAPI(String id,String imei_no){
        String url = "http://114.79.182.179:8080/APIcalll/ImeiApi?userid="+id+"&IMEI=" + imei_no;
        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("-respo-->", response.toString());
                        pDialog.hide();
                        if (response.optString("Status").equalsIgnoreCase("success")) {

                        }else{
                            Toast.makeText(LoginActivity.this,"Invalid login details",Toast.LENGTH_SHORT).show();//value

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("--->>", "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("Info");
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

}