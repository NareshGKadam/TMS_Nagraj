package com.gisdemo.app.mygisapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gisdemo.app.mygisapplication.colorpicker.PreferenceConnector;

import org.json.JSONArray;

import java.io.File;

//import com.drew.imaging.ImageMetadataReader;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public final static int CAMERA_REQUEST = 2;
    Button b4;
    ImageView im;
    TextView textView2;
//    String[] country = {  "Laxminagar","Dharmpeth","Hanumannagar","Dhantoli","Nehrunagar","Gandhibag","Satrangipura","Lakadganj","Ashinagar","Mangalwari"};
//    String[] country = {  "Laxminagar","Dharmpeth","Dhantoli","Satrangipura","Lakadganj","Ashinagar"};
        /*String country[] = {"Select Ward","1","2","3","4","5","6","7","8","9","10",
                            "11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30",
                            "31","32","33","34","35","36","37","38","39","40",
                            "41","42","43","44","45","46","47","48","49","50",
                            "51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70",
                            "71","72","73","74","75","76","77"};*/



   /* String country[] ={"Select Ward","Ward 1-Dhantoli","Ward 2-Dhantoli","Ward 3-Dhantoli","Ward 4-Dhantoli","Ward 5-Dhantoli","Ward 6-Dhantoli","Ward 7-Dhantoli","Ward 8-Dhantoli","Ward 9-Hanumannagar","Ward 10-Hanumannagar",
            "Ward 11-Hanumannagar","Ward 12-Hanumannagar","Ward 13-Hanumannagar","Ward 14-Hanumannagar","Ward 15-Hanumannagar","Ward 16-Hanumannagar","Ward 17-Gandhibag","Ward 18-Gandhibag","Ward 19-Gandhibag","Ward 20-Nehrunagar",
            "Ward 21-Lakadganj","Ward 22-Lakadganj","Ward 23-Lakadganj","Ward 24-Lakadganj","Ward 25-Lakadganj","Ward 26-Gandhibag","Ward 27-Gandhibag","Ward 28-Gandhibag","Ward 29-Gandhibag","Ward 30-Gandhibag",
            "Ward 31-Gandhibag","Ward 32-Gandhibag","Ward 33-Gandhibag","Ward 34-Gandhibag","Ward 35-Gandhibag","Ward 36-Lakadganj","Ward 37-Lakadganj","Ward 38-Lakadganj","Ward 39-Satrangipura","Ward 40-Satrangipura",
            "Ward 41-Satrangipura","Ward 42-Satrangipura","Ward 43-Satrangipura","Ward 44-Satrangipura","Ward 45-Satrangipura","Ward 46-Satrangipura","Ward 47-Satrangipura","Ward 48-Satrangipura","Ward 49-Satrangipura","Ward 50-Satrangipura",
            "Ward 51-Satrangipura","Ward 52-Satrangipura","Ward 53-Ashinagar","Ward 54-Ashinagar","Ward 55-Ashinagar","Ward 56-Ashinagar","Ward 57-Ashinagar","Ward 58-Mangalwari","Ward 59-Mangalwari","Ward 60-Mangalwari",
            "Ward 61-Mangalwari","Ward 62-Mangalwari","Ward 63-Mangalwari","Ward 64-Mangalwari","Ward 65-Mangalwari","Ward 66-Dharmpeth","Ward 67-Mangalwari","Ward 68-Dharmpeth","Ward 69-Dharmpeth","Ward 70-Dharmpeth",
            "Ward 71-Dharmpeth","Ward 72-Laxminagar","Ward 73-Dharmpeth","Ward 74-Laxminagar","Ward 75-Laxminagar","Ward 76-Hanumannagar","Ward 77-Nehrunagar"};
*/
    String country[] ={"Select Ward","Ward 1","Ward 2","Ward 3","Ward 4","Ward 5","Ward 6","Ward 7","Ward 8","Ward 9","Ward 10",
            "Ward 11","Ward 12","Ward 13","Ward 14","Ward 15","Ward 16","Ward 17"};



//        Arrays.sort(array);

    String[] country1 = {  "2"};
    String  tag_json_obj = "string_req";
    Spinner spin;

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);





        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(MainActivity.this);

        //Creating the ArrayAdapter instance having the country list
//        Arrays.sort(country);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        editor = getSharedPreferences("Demo", MODE_PRIVATE).edit();
//        editor.putString("url", "http://203.129.203.8:8080/APIcalll/getDataForGIS?zoneName=");
//        editor.apply();
        prefs = getSharedPreferences("Demo", MODE_PRIVATE);
//        editor.putString("url", "http://114.79.182.179:8080/APIcalll/getDataForGIS?zoneName=");
        editor.putString("url", "http://114.79.182.179:8080/APIcalll/getDataForGIS?wardNo=");
        editor.apply();



        textView2= findViewById(R.id.textView2);
        textView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final String[] colors = {"NMC", "CDMA"};


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Environment");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        if (colors[which].equalsIgnoreCase("NMC")){
                            editor.putString("url", "http://114.79.182.179:8080/APIcalll/getDataForGIS?wardNo=");
//                            editor.putString("url", "http://114.79.182.179:8080/APIcalll/getDataForGIS?zoneName=");
                            editor.apply();

                            ArrayAdapter aa = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,country);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            spin.setAdapter(aa);

                        }else if (colors[which].equalsIgnoreCase("CDMA")){
                            editor.putString("url", "http://125.18.179.57:8081/CGG_BillColl_Services/cdma/getdata/ptmap?ulbcode=1177&Accesscode=5f440s955&zoneno=");
                            editor.apply();

                            ArrayAdapter aa = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,country1);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            spin.setAdapter(aa);
                        }
                    }
                });
                builder.show();


                return false;
            }
        });


        ImageView bout=findViewById(R.id.imageView9);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
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

                                MainActivity.this.finish();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
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

                android.support.v7.app.AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });


        Button b11=findViewById(R.id.button);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GeotagActivity.class));

            }
        });

        Button b12=findViewById(R.id.button7);
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ActivitySearchByIndex.class));

            }
        });

   /*     Button b2=findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,MainActivityScene.class));


                File KML = new File("/sdcard/doc.kml");
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.earth");
                i.setDataAndType(Uri.fromFile(KML), "xml");
                startActivity(i);
            }
        });
*/

        im=findViewById(R.id.imageView);

        Button b3=findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callapi("field");  // for field agent view

            }
        });

        Button b4=findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callapi("analytic");  // analytical view

/*
try {

 File dcimImg = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/test.jpg");
//    File sdCard = Environment.getExternalStorageDirectory();
//    File directory1 = new File(sdCard.getAbsolutePath() + "/0512-12F8/");
    Log.i("path=0==>",""+dcimImg.getAbsolutePath());

//    File file = new File(directory1, "Testimg.jpg"); //or any other format supported
    FileInputStream streamIn = new FileInputStream(dcimImg);
//    Log.i("path=1==>",""+dcimImg.getPath());

    Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image
    streamIn.close();


    Metadata metadata = ImageMetadataReader.readMetadata(dcimImg);

    for (Directory directory : metadata.getDirectories()) {
        for (com.drew.metadata.Tag tag : directory.getTags()) {
           Log.i("[%s] - %s = %s"+"===>>",""+directory.getName()+"  "+ tag.getTagName()+"  "+ tag.getDescription());
        }
        if (directory.hasErrors()) {
            for (String error : directory.getErrors()) {
                System.err.format("ERROR: %s", error);
            }
        }
    }
}catch(Exception e)
{

}*/

//                callapi("analytic");  // analytical view

            }
        });




       /* b4=findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,Main2Activity.class));

                if(isMissingPermission(Manifest.permission.CAMERA, true, 22)) {

                    return;
                }
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    Uri uri = createImageFile();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (Exception e) {
                    Log.e("-->>",  ": " + e.toString());
                }
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });
*/





    }

    public void callapi(final String isfield) {

        String val="";
        String url1 = prefs.getString("url", null);
        if (url1.contains("cdma")){
            val=""+spin.getSelectedItemPosition();

        }else{
            val=""+spin.getSelectedItemPosition();
        }


        if (spin.getSelectedItemPosition()==0){
            Toast.makeText(MainActivity.this,"Please select zone",Toast.LENGTH_SHORT).show();//value
        }else {
//                    String url = "http://203.129.203.8:8080/APIcalll/getDataForGIS?zoneName="+val;
//                    val="2";
//                    String url = "http://125.18.179.57:8081/CGG_BillColl_Services/cdma/getdata/ptmap?ulbcode=1177&Accesscode=5f440s955&zoneno="+val;
            String url = prefs.getString("url", null) + val;
            Log.e("-url-->", url);

            final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
                   /* JsonRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {*/

          /*  JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            Log.d("--->", response.toString());
                            pDialog.hide();
//                            if (response.length() == 0) {
//                                Toast.makeText(MainActivity.this, "Property not found", Toast.LENGTH_SHORT).show();//value
//                            } else {

                                if (isfield.equalsIgnoreCase("analytic")) {
                                    PreferenceConnector.writeBoolean(MainActivity.this,PreferenceConnector.KEY_UNGROUP_POINT_VIEW,false);
                                    PreferenceConnector.writeInteger(MainActivity.this,PreferenceConnector.KEY_UNPAID_PIN_COLOR, Color.RED);
                                    ActivityGoogleMapAnalytics.jdata = response.toString();
                                    ActivityGoogleMapAnalytics.strWardNo = String.valueOf(spin.getSelectedItemPosition());
                                    Intent i = new Intent(MainActivity.this, ActivityGoogleMapAnalytics.class);
                                    startActivity(i);
                                } else {
                                    MainActivityGoogleMap.jdata = response.toString();
                                    Intent i = new Intent(MainActivity.this, MainActivityGoogleMap.class);
                                    startActivity(i);
                                }

//                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("--->>", "Error: " + error.getMessage());
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();//value
                    pDialog.hide();
                }
            });
// Adding request to request queue
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);*/


            pDialog.hide();
            if (isfield.equalsIgnoreCase("analytic")) {
                PreferenceConnector.writeBoolean(MainActivity.this,PreferenceConnector.KEY_UNGROUP_POINT_VIEW,false);
                PreferenceConnector.writeInteger(MainActivity.this,PreferenceConnector.KEY_UNPAID_PIN_COLOR, Color.RED);
                ActivityGoogleMapAnalytics.jdata = "";//response.toString();
                ActivityGoogleMapAnalytics.strWardNo = String.valueOf(spin.getSelectedItemPosition());
                Intent i = new Intent(MainActivity.this, ActivityGoogleMapAnalytics.class);
                startActivity(i);
            } else {
                MainActivityGoogleMap.jdata = "";//response.toString();
                Intent i = new Intent(MainActivity.this, MainActivityGoogleMap.class);
                startActivity(i);
            }

        }

    }
    private boolean isMissingPermission(String permission , boolean requestPermission, int requestCode) {
        boolean missingPermission = ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED;
        if(missingPermission && requestPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("");
                builder.setPositiveButton("got to setting") , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        goToSettings(activity);
                    }
                });*/
//            }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("");
                builder.setPositiveButton("setting" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        goToSettings(activity);
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{
                                    permission
                            },
                            requestCode);
                }
            }
        }

        return missingPermission;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 22: {
                if(!isMissingPermission(android.Manifest.permission.CAMERA, false, 22)) {
//                    takePhoto();


                }
            }
            break;
           /* case REQUEST_PERMISSION_VIDEOCAMERA: {
                if(!isMissingPermission(Manifest.permission.CAMERA, false, REQUEST_PERMISSION_CAMERA)) {
                    takeVideo();
                }
            }
            break;*/
        }
    }




    private Uri createImageFile() throws Exception {
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), System.currentTimeMillis() + ".jpg");

        File file = new File("/sdcard/newImage.jpg");

        //File file = new File(System.currentTimeMillis() + ".jpg");   fileprovider
        Log.i("","path--->"+file.getAbsolutePath());
        Uri photoUri = null;

        try{
            photoUri = FileProvider.getUriForFile(this, "com.gisdemo.app.mygisapplication.fileprovider", file);
        }
        catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        return photoUri;
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.i("", "Receive the camera result");

        if (resultCode == RESULT_OK) {

            File out = new File("/sdcard/newImage.jpg");

            if(!out.exists()) {

                return;

            }
            Log.i("","path-2-->"+out.getAbsolutePath());
            Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());

            im.setImageBitmap(mBitmap);

        }else{
            Log.i("","else-->");
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();





    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
