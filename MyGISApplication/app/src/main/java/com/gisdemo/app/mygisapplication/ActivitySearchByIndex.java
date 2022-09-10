package com.gisdemo.app.mygisapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class ActivitySearchByIndex extends Activity {

    final String  tag_json_obj = "string_req";
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_main22);

        et=(EditText) findViewById(R.id.editText);


        Button b= (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=et.getText().toString();


                if (id.length()==0){
                    Toast.makeText(ActivitySearchByIndex.this,"Please enter Property number",Toast.LENGTH_SHORT).show();//value

                }else {
                     //http://172.16.25.166:8081/APIcalll/api2?id=20021221
                    String url = "http://114.79.182.179:8080/APIcalll/propertyDCB?index_number=" + id;
                    Log.d("--->", url);
                    final ProgressDialog pDialog = new ProgressDialog(ActivitySearchByIndex.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("--->", response.toString());
                                    pDialog.hide();
                                    if (response==null) {
                                        Toast.makeText(ActivitySearchByIndex.this,"Property not found",Toast.LENGTH_SHORT).show();//value
                                    }else{
                                        Intent i = new Intent(ActivitySearchByIndex.this, TaxDetails.class);
                                        i.putExtra("json", response.toString());
                                        startActivity(i);
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("--->>", "Error: " + error.getMessage());
                            Toast.makeText(ActivitySearchByIndex.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
                            pDialog.hide();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                }
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivitySearchByIndex.this);
        builder1.setMessage("Do you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
                        editor.putString("login", "0");
                        editor.apply();

                        ActivitySearchByIndex.this.finish();
                        Intent i = new Intent(ActivitySearchByIndex.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
