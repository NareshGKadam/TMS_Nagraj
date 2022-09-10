package com.gisdemo.app.mygisapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import infobots.trec.agni.Adapters.MyAdapter;
//import infobots.trec.agni.R;
//import infobots.trec.agni.Utils.ConnectionDetector;
//import infobots.trec.agni.Utils.SharedPrefData;
//import infobots.trec.agni.Utils.Utils;
//import infobots.trec.agni.modals.Complaints;
//import infobots.trec.agni.network.NetworkUtil;

//import infobots.trec.agni.R;

public class GeotagActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = "CitizenComplaintActivity";
//    ImageView imageView;
//    private ListView mListView;
    Context mContext;
//    private MyAdapter adapter;
//    List<Complaints> mListMainList = new ArrayList<Complaints>();
    //ArrayList<Map<String, String>> mListMainList = new ArrayList<Map<String, String>>();
    TextView empty,cloc,geo;
    JSONObject updateresponse;
    ImageView img;
//    ProgressBar pb;
//    private ConnectionDetector connectionDetector;
//    SharedPrefData mySharedPref;
    public static int start=0;
    public static int created=0;
    private Location location,mylocation;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 2000; // = 20 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    static Activity mActivity;
double clat=0.0,clong=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geotag);


        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);


        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        mActivity = GeotagActivity.this;
        mContext = GeotagActivity.this;
//        connectionDetector = new ConnectionDetector(mContext, mActivity);


        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        img = (ImageView) findViewById(R.id.imageView4);
        geo = (TextView)findViewById(R.id.tvgeo);
        cloc = (TextView)findViewById(R.id.tvlcurrentoc);

        geo.setText("");
        cloc.setText("");

//        pb=(ProgressBar) findViewById(R.id.progressBar1);





        File folder = new File(Environment.getExternalStorageDirectory() + "/PhotoGeotag");
        if (folder.exists() == false) {
            folder.mkdirs();
        }
        File file = null;
        try{
         file = new File(Environment.getExternalStorageDirectory(), "/PhotoGeotag/img" + ".jpg");
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            bitmap = crupAndScale(bitmap, 300); // if you mind scaling
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); Log.i("error","error--->FileNotFoundException data");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); Log.i("error","error--->IOException data");
        }}catch (Exception e){
            Log.i("error","error--->Exception data");
        }

        try {
            Log.i("","path--->"+file.getAbsolutePath());
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

            // your Final lat Long Values
            Float Latitude, Longitude;

            if ((LATITUDE != null)
                    && (LATITUDE_REF != null)
                    && (LONGITUDE != null)
                    && (LONGITUDE_REF != null)) {

                if (LATITUDE_REF.equals("N")) {
                    Latitude = convertToDegree(LATITUDE);
                } else {
                    Latitude = 0 - convertToDegree(LATITUDE);
                }

                if (LONGITUDE_REF.equals("E")) {
                    Longitude = convertToDegree(LONGITUDE);
                } else {
                    Longitude = 0 - convertToDegree(LONGITUDE);
                }

                String lt=""+Latitude.toString().replace("-","");
                String ln=""+Longitude.toString().replace("-","");

                final double Latitude1=Double.parseDouble(lt);
                final double Longitude1=Double.parseDouble(ln);
                Log.i("error","error--->Lat:"+Latitude1+" , Long:"+Longitude1);
                geo.setText("Lat:"+Latitude1+" , Long:"+Longitude1+" show on map");


                geo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Latitude1, Longitude1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);*/

                        String strUri = "http://maps.google.com/maps?q=loc:" + Latitude1 + "," + Longitude1 + " (" + "Photo captured here" + ")";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);

                    }
                });

            }

        }catch(Exception e){
           e.printStackTrace();
            Log.i("error","error--->exif data");
        }








        Button b11=(Button)findViewById(R.id.button6);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/PhotoGeotag/img" + ".jpg")));
                startActivityForResult(intent, 0);

            }
        });

//        getComplaints();

//        adapter = new MyAdapter(OfficerActionActivity.this, mListMainList);
//        mListView.setAdapter(adapter);
//        mListView.setFastScrollEnabled(true);
//        if (adapter.isEmpty()) {
//            mListView.setEmptyView(empty);
//        }




    }
    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    }

    public static void start(){
        start=1;
        Toast.makeText(mActivity,"Request Accepted. tracking geo activated",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        File file = new File(Environment.getExternalStorageDirectory(), "/PhotoGeotag/img" + ".jpg");
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            bitmap = crupAndScale(bitmap, 300); // if you mind scaling
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("","path-2-->"+file.getAbsolutePath()+", lat,long: "+clat+","+clong);
        geoTag(file.getAbsolutePath(),clat,clong);
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        img.setImageBitmap(bitmap);


    }


    public void geoTag(String filename, double latitude, double longitude){
        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();


            Intent refresh = new Intent(this, GeotagActivity.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.


        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }


    /* public void getSnaks(){
        Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry Agian", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new NetworkUtil(getBaseContext()).isNetworkAvailable()){
                            startActivity(new Intent(OfficerActionActivity.this, MainActivity.class));
                        }else {
                            getSnaks();
                        }
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }*/


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            justToast("You need to install Google Play Services to use the App properly");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
// stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            created=0;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }

    }

    public void justToast(String msg)
    {
        Toast.makeText(GeotagActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            justToast("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            clat=location.getLatitude();
            clong=location.getLongitude();
            mylocation=location;
            cloc.setText("Lat: " + location.getLatitude() + " , Long: " + location.getLongitude());

          /*  if (created==1){

                String string=getcdate();
                JSONObject jsonObject = new JSONObject();
                String id="";

                try {
                    jsonObject.put("attendingId", ""+updateresponse.optString("attendingId"));
                    jsonObject.put("attendingVeChicleType", ""+updateresponse.optString("attendingVeChicleType"));
                    jsonObject.put("attendingVehicleNumber", ""+updateresponse.optString("attendingVehicleNumber"));
                    jsonObject.put("complaintId", ""+updateresponse.optString("complaintId"));
                    id= ""+updateresponse.optString("complaintId");
                    jsonObject.put("createdDate", ""+updateresponse.optString("createdDate"));
                    jsonObject.put("incidentComments", ""+updateresponse.optString("incidentComments"));
                    jsonObject.put("incidentGpsAdress", ""+updateresponse.optString("incidentGpsAdress"));
                    jsonObject.put("incidentLang", ""+updateresponse.optString("incidentLang"));
                    jsonObject.put("incidentLat", ""+updateresponse.optString("incidentLat"));
                    jsonObject.put("mobileNo", ""+updateresponse.optString("mobileNo"));
                    jsonObject.put("status", ""+updateresponse.optString("status"));
                    jsonObject.put("userName", ""+updateresponse.optString("userName"));
                    jsonObject.put("userRole", ""+updateresponse.optString("userRole"));

                    JSONArray jarr=new JSONArray();
                    JSONObject j = new JSONObject();
                    j.put("createdAt", ""+string);
                    j.put("incidentGpsAddress", ""+updateresponse.optString("incidentGpsAdress"));
                    j.put("vechicleLang", ""+location.getLongitude());
                    j.put("vehicleLat", ""+location.getLatitude());

                    jarr.put(j);

                    jsonObject.put("trackMe",jarr);

                    callUpdateLocation(id,jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/


        }
    }

    //    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    //    @Override
    public void onProviderEnabled(String s) {

    }

    //    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
//            locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            mylocation=location;

        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(GeotagActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

/*

    public void callRespond() {

        String string=getcdate();
        JSONObject jsonObject = new JSONObject();
        try {
            if ( (location.getLongitude()!=0 ||  location.getLongitude()!=0.0 ) && (location.getLatitude()!=0 || location.getLatitude()!=0.0)) {
                long uniqueid = System.currentTimeMillis() / 1000L;
                jsonObject.put("attendingId", "");
                jsonObject.put("attendingVeChicleType", "");
                jsonObject.put("attendingVehicleNumber", "");
                jsonObject.put("complaintId", "" + uniqueid);
                jsonObject.put("createdDate", "" + string);
                jsonObject.put("incidentComments", "");
                jsonObject.put("incidentGpsAdress", "");
                jsonObject.put("incidentLang", "");
                jsonObject.put("incidentLat", "");
                jsonObject.put("mobileNo", "9876565432");
                jsonObject.put("status", "");
                jsonObject.put("userName", "Naresh");
                jsonObject.put("userRole", "");

                JSONArray jarr = new JSONArray();
                JSONObject j = new JSONObject();
                j.put("createdAt", "" + string);
                j.put("incidentGpsAddress", "");

                j.put("vechicleLang", location.getLongitude());
                j.put("vehicleLat", location.getLatitude());

                jarr.put(j);

                jsonObject.put("trackMe", jarr);

//            apiLogin(jsonObject);
                if (connectionDetector.isConnectingToInternet()) {
                    String url = WebAPI.BASE_APIURL + "fireprocessing/tracking/";
                    Log.i("url", "url==>>" + url);
                    Log.i("send json checkin ", "json==>>" + jsonObject);

                    final ProgressDialog prog = new ProgressDialog(OfficerActionActivity.this);//Assuming that you are using fragments.
                    prog.setMessage("please waight...");
                    prog.setCancelable(false);
                    prog.setIndeterminate(true);
                    prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    prog.show();
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            prog.dismiss();
                            Log.i("API respo", "" + response.toString());

                            updateresponse = response;
                            created = 1;
                            if (response.isNull("error")) {
                            } else {
                                try {
                                    if (response.getString("error").trim().length() != 0) {
                                        if (!response.getString("error").equalsIgnoreCase("null")) {
                                            Toast.makeText(mActivity, "" + response.getString("error"), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            prog.dismiss();
                            Log.i("send json errr", "checkin");
                            Utils.handleError(mActivity, error, 0);

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
//                    headers.put("deviceId", "" + Utils.getUniqueID(mContext));
                            return headers;
                        }
                    };
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            WebAPI.timeOutLimit,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    AppController.getInstance().addToRequestQueue(jsonObjReq, "");

                } else {
                    connectionDetector.ShowSimpleDialog("Error", getResources().getString(R.string.internet_error));
                }

            }else{
                justToast("Unable to trace GPS location, Please again.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

*/

/*

    public void callUpdateLocation(String cid,JSONObject json) {

        if (connectionDetector.isConnectingToInternet()) {


            String url = WebAPI.BASE_APIURL+ "fireprocessing/complaintid/"+cid;
            Log.i("url", "url==>>" + url);
            Log.i("send json checkin ", "json==>>" + json);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    prog.dismiss();
                    Log.i("API respo", "" + response.toString());


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                prog.dismiss();
                    Log.i("send json errr", "checkin");
                    Utils.handleError(mActivity, error, 0);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
//                    headers.put("deviceId", "" + Utils.getUniqueID(mContext));
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    WebAPI.timeOutLimit,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq, "");

        } else {
//        connectionDetector.ShowSimpleDialog("Error", getResources().getString(R.string.internet_error));
        }



    }


*/

    public String getcdate() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strDate = sdf.format(cal.getTime());
        System.out.println("Current date in String Format: " + strDate);
        String string = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat();
        sdf1.applyPattern("dd-MM-yyyy HH:mm");
        try {
            Date date = sdf1.parse(strDate);
            string = sdf1.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Current date in Date Format: " + string);


        return string;
    }

/*


    public void getComplaints() {

        if (connectionDetector.isConnectingToInternet()) {


            String url = WebAPI.BASE_APIURL+"fireprocessing/";
            Log.i("url", "url==>>" + url);
            final ProgressDialog prog = new ProgressDialog(OfficerActionActivity.this);//Assuming that you are using fragments.
            prog.setMessage("please waight...");
            prog.setCancelable(false);
            prog.setIndeterminate(true);
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
            JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    prog.dismiss();
                    Log.i("url", "url==>>" + response);

                    for (int i=0;i<response.length();i++) {
                        try {
                            JSONObject j = response.getJSONObject(i);
                            Complaints c=new Complaints();
                            c.setUserRole(j.optString("userRole"));
                            c.setMobileNo(j.optString("mobileNo"));
                            c.setComplaintId(j.optString("complaintId"));
                            c.setIncidentGpsAdress(j.optString("incidentGpsAdress"));
                            c.setIncidentLat(j.optString("incidentLat"));
                            c.setIncidentLang(j.optString("incidentLang"));
                            c.setIncidentComments(j.optString("incidentComments"));
                            c.setStatus(j.optString("status"));
                            c.setUsername(j.optString("username"));
                            c.setAttendingId(j.optString("attendingId"));
                            c.setCreatedDate(j.optString("createdDate"));
                            c.setAttendingVeChicleType(j.optString("attendingVeChicleType"));
                            c.setAttendingVehicleNumber(j.optString("attendingVehicleNumber"));

                            mListMainList.add(c);
                        }catch(JSONException e){
                        }

                    }


//                   mListMainList =response.body();

                    adapter = new MyAdapter(OfficerActionActivity.this, mListMainList);
                    mListView.setAdapter(adapter);
                    mListView.setFastScrollEnabled(true);
                    if (adapter.isEmpty())
                        mListView.setEmptyView(empty);




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prog.dismiss();
                    Log.i("send json errr", "checkin");
                    Utils.handleError(mActivity, error, 0);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
//                    headers.put("deviceId", "" + Utils.getUniqueID(mContext));
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    WebAPI.timeOutLimit,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq, "");

        } else {
            connectionDetector.ShowSimpleDialog("Error", getResources().getString(R.string.internet_error));
        }



    }

*/


}



