package com.gisdemo.app.mygisapplication;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

//https://www.androidtutorialpoint.com/intermediate/android-map-app-showing-current-location-android/
//        https://www.javatpoint.com/android-google-map-displaying-current-location
//        https://androidclarified.com/display-current-location-google-map-fusedlocationproviderclient/
//        http:
//www.zoftino.com/android-show-current-location-on-map-example

public class MainActivityGoogleMap extends Activity implements LocationListener, OnMapReadyCallback {
    private GoogleMap mMap;
    View markerView;
    JSONArray j= null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;
    LocationListener locationListener = null;
    float mRotationMatrix[];
    float mDeclination = 0;
    ArrayList<Locationbean> listmarker=new ArrayList<>();
    TextView numTxt,numTxt5;
    public static String jdata;
    double mylat=0.0;
    double mylon=0.0;
    final String  tag_json_obj = "string_req";
    Handler h = new Handler();
    int delay = 5 * 1000;
    int mDist=400;
    int tilt=10;  //45;
    ArrayList<JSONArray> alist=new ArrayList<>();
    private LruCache<String, Bitmap> mMemoryCache;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


    ArrayList<Locationbean> listLoc=new ArrayList<Locationbean>();

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gmap);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
//        mMap  = ((MapFragment) getFragmentManager().findFragmentById(R.id.g_map)).getMap();


        ImageView img=findViewById(R.id.imageView12);
        LinearLayout ll12=findViewById(R.id.ll12);
        img.setVisibility(View.GONE);
        ll12.setVisibility(View.GONE);


        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        boolean flag = displayGpsStatus();
        if (flag) {

//            locationListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 3000, 1, this);

        } else {
            Toast.makeText(MainActivityGoogleMap.this,"Your GPS is off",Toast.LENGTH_SHORT).show();

        }
//------------- hitech---------
        // status   amt       lon           lat
        /*Paid	    8000	78.36882838	,17.446505
       2 Not Paid	7000	78.36921741	,17.44712012
       3 Paid	    5500	78.3702312	,17.4473758
       4 Not Paid	6000	78.37083825	,17.44700762
       5 Paid	    7000	78.37016022	,17.44660315

       6 Not Paid	5000	78.37063394	,17.44596197
       7 Paid	    8000	78.37185792	,17.44678607
       8 Not Paid	5000	78.37118525	,17.44741092
       9 Not Paid	7000	78.37072519	,17.44810437
       10 Not Paid	5000	78.37259296	,17.44796496
       11 Not Paid	2000	78.37314344	,17.44745298
       12 Not Paid	6000	78.3726318	,17.44720106*/

        /*{"LAT":"21.137251","Current_Collection":"26369.0","Arrears_Due":"0.0",
                "OWNERS_NAME":" GUJRATI SAMAJ BHAWAN THROUGH THE VAISHYA BANK LTD. NGP.  ","LONG":"79.07957","Total_Due":"6509.0",
                "MOBILE_NUMBER":"0712244200","INDEX_NUMBER":"04000145A001","UPIN":"0040060008T002","CURRENT_DEMAND":"32878.0"}

                 "Property": [
      {
        "LAT": "21.134796",
        "Current_Collection": "11108.0",
        "Arrears_Due": "-1263.0",
        "OWNERS_NAME": "M/S PRIME INFRASTRUCTURE THROUGH PARTNER SHRI. NAVINDERBIR SINGH KHURANA S/O DIWANSINGH KHURANA",
        "LONG": "79.077745",
        "Total_Due": "-2526.0",
        "MOBILE_NUMBER": "9422101223",
        "INDEX_NUMBER": "074051192",
        "UPIN": "0740320051T027",
        "CURRENT_DEMAND": "9845.0"
      },
      {},{},{}
//---------------for CDMA------------------
[
  {
    "Property": [
      {
        "LAT": "17.3049867",
        "Duesason_DemandDate": "1474.0",
        "LONG": "78.5228345",
        "Assessment_number": "1177001670",
        "Ulb_name": "Badangpet"
      }
    ]
  },


                */
        //------------------------------------------------

        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


//        String jdata=getIntent().getStringExtra("json");

        try {

//            j = new JSONArray(getIntent().getStringExtra("myjson"));


//            j = new JSONArray(jdata);

            String jj="[\n" +
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"0.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"NO DATA FOUND\",\n" +
                    "        \"CURRENT_DEMAND\": \"500.0\",\n" +
                    "        \"UPIN\": \"0020110030\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"500.0\",\n" +
                    "        \"OWNERS_NAME\": \"Satish Kumar P \",\n" +
                    "        \"PROPERTY_TYPE\": \"NON-RESIDENTIAL\",\n" +
                    "        \"INDEX_NUMBER\": \"02000080\",\n" +
                    "        \"LAT\": \"17.488716\",\n" +
                    "        \"LONG\": \"78.384640\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"0.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"8698817646\",\n" +
                    "        \"CURRENT_DEMAND\": \"300.0\",\n" +
                    "        \"UPIN\": \"0020010012\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"300.0\",\n" +
                    "        \"OWNERS_NAME\": \" Venkateswarlu CH  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000092\",\n" +
                    "        \"LAT\": \"17.488596\",\n" +
                    "        \"LONG\": \"78.384867\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"-964.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9822922384\",\n" +
                    "        \"CURRENT_DEMAND\": \"200.0\",\n" +
                    "        \"UPIN\": \"0020110001\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"200.0\",\n" +
                    "        \"OWNERS_NAME\": \" Karthik Reddy V\\t\",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000081\",\n" +
                    "        \"LAT\": \"17.489016\",\n" +
                    "        \"LONG\": \"78.384780\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"350.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" Srikanth M  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488862\",\n" +
                    "        \"LONG\": \"78.384965\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"333.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" Hanumanth V  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.489032\",\n" +
                    "        \"LONG\": \"78.385051\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"358.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" SriRam N  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488767\",\n" +
                    "        \"LONG\": \"78.385288\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"383.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" T Sheshu  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488474\",\n" +
                    "        \"LONG\": \"78.385101\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"408.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" Susheela P  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488370\",\n" +
                    "        \"LONG\": \"78.385282\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"433.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" Veeresh K  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488333\",\n" +
                    "        \"LONG\": \"78.385407\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "  {\n" +
                    "    \"Property\": [\n" +
                    "      {\n" +
                    "        \"Total_Due\": \"11109.0\",\n" +
                    "        \"MOBILE_NUMBER\": \"9923596256\",\n" +
                    "        \"CURRENT_DEMAND\": \"458.0\",\n" +
                    "        \"UPIN\": \"0020010011\",\n" +
                    "        \"Arrears_Due\": \"0.0\",\n" +
                    "        \"Current_Collection\": \"0.0\",\n" +
                    "        \"OWNERS_NAME\": \" TN Srinu  \",\n" +
                    "        \"PROPERTY_TYPE\": \"MIXED\",\n" +
                    "        \"INDEX_NUMBER\": \"02000094\",\n" +
                    "        \"LAT\": \"17.488963\",\n" +
                    "        \"LONG\": \"78.38532\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n"+
                    "]";


            j = new JSONArray(jj);

            if (j.length()!=0){
                for (int x=0;x<j.length();x++) {
                    JSONObject obj = j.getJSONObject(x);
                    JSONArray jarr = obj.optJSONArray("Property");
                    Locationbean lb1 = new Locationbean();
                    String amt="";
                    int forlimit=3;
                    try{
                        if (jarr.length()>forlimit){
                        }else{
                            forlimit=jarr.length();
                        }}catch(Exception e){
                    }
                    for (int i = 0; i < forlimit; i++) {
                        JSONObject jloc = jarr.getJSONObject(i);
                        lb1.setLon(Double.parseDouble(jloc.optString("LONG")));
                        lb1.setLat(Double.parseDouble(jloc.optString("LAT")));

//                        lb1.setLon(Double.parseDouble(jloc.optString("LAT")));
//                        lb1.setLat(Double.parseDouble(jloc.optString("LONG")));

//                        lb1.setStatus("Not Paid");//Html.fromHtml("<font color='rgb'>"+text contain+"</font>")


                        String cdem ;
                        String upin ;
                        String tdem ;

                        if (jloc.isNull("Assessment_number")) {
                            cdem = jloc.optString("CURRENT_DEMAND");
                            upin = jloc.optString("INDEX_NUMBER");
                            tdem = jloc.optString("Total_Due");
                            if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0")||tdem.contains("-")) {
                                lb1.setStatus("Paid");
                            }else{
                                lb1.setStatus("Not Paid");
                            }
                        }else {
                            cdem = jloc.optString("Duesason_DemandDate");
                            upin = jloc.optString("Assessment_number");
                            tdem = jloc.optString("Ulb_name");
                            lb1.setStatus("Not Paid");
                        }

                        amt= amt+"<br>"+upin + " : <b>" + cdem + "</b> / " + tdem;
                    }
                    lb1.setAmt(amt);
                    listLoc.add(lb1);
/*          "LAT": "17.3049867",
            "Duesason_DemandDate": "1474.0",
            "LONG": "78.5228345",
            "Assessment_number": "1177001670",
            "Ulb_name": "Badangpet"            */
                }
            }else{}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //-------------------------------------------------






    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


    /* @Override
     public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;

         //seattle coordinates
         LatLng seattle = new LatLng(47.6062095, -122.3320708);
         mMap.addMarker(new MarkerOptions().position(seattle).title("Seattle"));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));
     }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
//        mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        enableMyLocationIfPermitted();
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.setMinZoomPreference(11);

        markerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        numTxt = (TextView) markerView.findViewById(R.id.textView7);
        numTxt5 = (TextView) markerView.findViewById(R.id.textView5);


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style1));

            if (!success) {
                Log.e("error", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("err", "Can't find style. Error: ", e);
        }



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = (int)(marker.getTag());
                //Using position get Value from arraylist



                Dialog d=new Dialog(MainActivityGoogleMap.this);
                d.setContentView(R.layout.dlg);
                d.setCancelable(true);
                d.show();

                try {
                    JSONObject obj = j.getJSONObject(position);
                    JSONArray jarr = obj.optJSONArray("Property");

                    LinearLayout ll11=(LinearLayout) d.findViewById(R.id.ll11);

                    LayoutInflater inflater = LayoutInflater.from(MainActivityGoogleMap.this);
//Log.i("","====>"+jarr.length());
                    for (int k=0;k<jarr.length();k++) {
                        JSONObject jo=jarr.getJSONObject(k);
                        View layout_number = inflater.inflate(R.layout.listitem, ll11, false);
                        TextView tv11 = (TextView) layout_number.findViewById(R.id.textView11);
                        TextView tv22 = (TextView) layout_number.findViewById(R.id.textView22);
                        TextView tvtype = (TextView) layout_number.findViewById(R.id.tvstatus);
//                number.setTag(i);
                        tv11.setText("" +jo.optString("INDEX_NUMBER"));
                        tv22.setText(""+jo.optString("Total_Due"));  //CURRENT_DEMAND

                        String type = jo.optString("PROPERTY_TYPE");
                        if (type.equalsIgnoreCase("RESIDENTIAL")){
                            tvtype.setText("Res");
                        }else if (type.equalsIgnoreCase("NON-RESIDENTIAL")){
                            tvtype.setText("Non Res");
                            tvtype.setTextColor(getResources().getColor(R.color.sha_blue_pressed));
                        }else  if (type.equalsIgnoreCase("OPEN PLOT")){
                            tvtype.setText("Open");
                        }else{
                            tvtype.setText(""+type);
                        }

                        String tdem = jo.optString("Total_Due");
                        if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0")|| tdem.contains("-")) {
//                            lb1.setStatus("Paid");
                            tv11.setTextColor(getResources().getColor(R.color.green1));
                            tv22.setTextColor(getResources().getColor(R.color.green1));
                        }else{
                            tv11.setTextColor(getResources().getColor(R.color.red1));
                            tv22.setTextColor(getResources().getColor(R.color.red1));

//                            lb1.setStatus("Not Paid");
                        }


                        ll11.addView(layout_number);
                    }



                }catch (Exception x){

                }





//                layout.addView(row);


                return true;
            }
        });


        locationTrack = new LocationTrack(MainActivityGoogleMap.this);
        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            Log.i("==2222222222=>", "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude));
            Location location = null;
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
//                    Toast.makeText(MainActivityGoogleMap.this, location.getLatitude() +"--- ,--- "+location.getLongitude(),Toast.LENGTH_SHORT).show();
                }}

            try {

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                } else {
                    Location getLastLocation = locationManager.getLastKnownLocation
                            (LocationManager.NETWORK_PROVIDER);
                    longitude = getLastLocation.getLongitude();
                    latitude = getLastLocation.getLatitude();
                    Log.i("lastknownlocation=>", "Longitude:" + longitude + "\nLatitude:" + latitude);
                }

            }catch (Exception e){
                Log.d("lastknownlocation",""+e.toString());
            }
//            Toast.makeText(MainActivityGoogleMap.this,"initial geooo->" +location.getLatitude() +"--- ,--- "+location.getLongitude(),Toast.LENGTH_SHORT).show();

            Log.i("==11--11=>", "Longitude:" + longitude + "\nLatitude:" +latitude);
            //----------------------------
            if ( longitude== 0.0 || latitude==0.0) {
                Location getLastLocation = locationManager.getLastKnownLocation
                        (LocationManager.GPS_PROVIDER);
                longitude = getLastLocation.getLongitude();
                latitude = getLastLocation.getLatitude();
                Log.i("lastknownlocation=>", "Longitude:" + longitude + "\nLatitude:" +latitude);
            }

            //------------------------------


            //-----------------tepm for mumbai---
//             latitude=19.009256;
//             longitude=73.029945;
            //-----------------------------------

            //-----------------tepm for Nagpur---
//            latitude=21.137251;
//            longitude=79.07957;
            //-----------------------------------

            //-----------------tepm for cdma---
//            latitude=17.3056285;
//            longitude=78.5224135;
            //-----------------------------------


            //-----------------tepm for KBHP hyd demo---
            latitude=17.488716;
            longitude=78.384640;
//            17.488716,78.384640


            mylat=latitude;
            mylon=longitude;



           /*
            double dist11=distance( 17.4459619650419, 78.3706339440462,mylat,mylon);
            Log.i("","dist11===>"+dist11);
            double dist12=distance(17.4467860700014, 78.3718579172869,mylat,mylon);
            Log.i("","dist12===>"+dist12);
*/
//            dist2===>20.155636262329605
//            dist3===>155.32076346326193
//            dist4===>95.22109008530347
//            dist5===>197.05055649517902

            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(coordinate)      // Sets the center of the map to Mountain View
                    .zoom(19)                   // Sets the zoom
//                .bearing(30)                // Sets the orientation of the camera to east
                    .tilt(tilt)                 // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

          /*  try {
                KmlLayer layer = new KmlLayer(mMap,R.raw.pheonix3, this.getApplicationContext()); // creating the kml layer
                layer.addLayerToMap();// adding kml layer with the **google map**
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivityGoogleMap.this,"catch"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }*/

            final Bitmap dd= createDrawableFromView(this, markerView);
//            h.postDelayed(new Runnable(){
//                public void run(){
                    listmarker.clear();
                    Log.i("","listLoc.size()===>"+listLoc.size());
                    for (int i=0;i<listLoc.size();i++) {
                        double dist=distance(listLoc.get(i).getLat(), listLoc.get(i).getLon(),mylat,mylon);
                        Log.i("","dist===>"+dist);
                        if(dist<=mDist){ //35
                            //---------------------------
                            TextView text = new TextView(MainActivityGoogleMap.this);
                            text.setTextSize(12);
                            text.setText(Html.fromHtml(listLoc.get(i).getAmt()));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(15,0,15,180);
                            text.setLayoutParams(params);
                            Log.i("","status===>"+listLoc.get(i).getStatus());
                            if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                                text.setTextColor(getResources().getColor(R.color.green1));
                            }else{
                                text.setTextColor(getResources().getColor(R.color.red1));
                            }
                            text.setTextColor(getResources().getColor(R.color.white));
                            IconGenerator generator = new IconGenerator(MainActivityGoogleMap.this);
//                            generator.setBackground(getResources().getDrawable(R.drawable.g44));

//                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pina).copy(Bitmap.Config.ARGB_8888, true);
//                            Canvas canvas = new Canvas(bitmap);
//                            canvas.drawText(offersCount,
//                                    canvas.getWidth()/2,
//                                    canvas.getHeight()/2 - ((text.getFontMetrics().ascent + clustersPaint.getFontMetrics().descent) / 2) ,
//                                    clustersPaint);


                            generator.setBackground(getResources().getDrawable(R.drawable.tpin3));   //pinaa2
                            generator.setContentView(text);
                            Bitmap icon1 = generator.makeIcon();
                            String key=""+i;
                            addBitmapToMemoryCache(key,icon1);

                            Bitmap icon=getBitmapFromMemCache(key);

                            //------------------
                            /*



                             */

                            numTxt.setText(listLoc.get(i).getAmt());

                            MarkerOptions m = new MarkerOptions();
                            LatLng latlon = new LatLng(listLoc.get(i).getLat(), listLoc.get(i).getLon());
                            final Marker marker;
                            Log.i("","status=1==>"+listLoc.get(i).getStatus());
                            if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                                numTxt.setTextColor(getResources().getColor(R.color.green));
                                text.setTextColor(getResources().getColor(R.color.green));
                                marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                        .icon(BitmapDescriptorFactory.fromBitmap(icon)));
//                                playsound();
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                marker.setTag(i);
                            }else{
                                numTxt.setTextColor(getResources().getColor(R.color.red));
                                text.setTextColor(getResources().getColor(R.color.red));
                                marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                        .icon(BitmapDescriptorFactory.fromBitmap(icon)));
//                                playsound();
//                                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                marker.setTag(i);
                            }

                            Locationbean lb=new Locationbean();
                            lb.setLat(listLoc.get(i).getLat());
                            lb.setLon(listLoc.get(i).getLon());
                            lb.setMarker(marker);
                            listmarker.add(lb);
                            //-------------------

                        }
                    }






//            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            locationTrack.showSettingsAlert();
        }



//        locationManager.get


//        mMap.animateCamera(yourLocation);





    }
    public void itemclick(View v){

//        LinearLayout lp=(LinearLayout) v.getParent();
//        LinearLayout ll=(LinearLayout)lp.getChildAt(0);
        RelativeLayout rl=(RelativeLayout)  v.getParent();
        LinearLayout ll1=(LinearLayout)rl.getChildAt(0);
        TextView tv=(TextView)ll1.getChildAt(0);
        String index=tv.getText().toString();

            //http://172.16.25.166:8081/APIcalll/api2?id=20021221

        //http://114.79.182.179:8080/APIcalll/propertyDCB?index_number=

        //http://114.79.182.179:8080/APIcalll/getTaxDetails?id=

            String url = "http://114.79.182.179:8080/APIcalll/propertyDCB?index_number=" + index;
            Log.e("--->", url);
            final ProgressDialog pDialog = new ProgressDialog(MainActivityGoogleMap.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            /*JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("--->", response.toString());
                            pDialog.hide();
                            if (response==null) {
                                Toast.makeText(MainActivityGoogleMap.this,"Property not found",Toast.LENGTH_SHORT).show();//value
                            }else{
                                Intent i = new Intent(MainActivityGoogleMap.this, TaxDetails.class);
                                i.putExtra("json", response.toString());
                                startActivity(i);
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("--->>", "Error: " + error.getMessage());
                    Toast.makeText(MainActivityGoogleMap.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
                    pDialog.hide();
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);*/

        pDialog.hide();
        Intent i = new Intent(MainActivityGoogleMap.this, TaxDetails.class);
        i.putExtra("json", "");
        startActivity(i);


    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onLocationChanged(final Location location) {

//        Toast.makeText(MainActivityGoogleMap.this,"changed 22"+location.getLatitude()+" , "+location.getLatitude(),Toast.LENGTH_SHORT).show();

        Log.i("-->","location chaned"+location.getLatitude()+" , "+location.getLatitude());

      /*  //------------temp----------------
        double latitude=17.488716;
        double longitude=78.384640;
        LatLng latLng = new LatLng(latitude, longitude);

//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(19)                   // Sets the zoom
//                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(tilt)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();
        listmarker.clear();

        final Bitmap dd= createDrawableFromView(this, markerView);

                Log.i("","listLoc.size()==1=>"+listLoc.size());
                for (int i=0;i<listLoc.size();i++) {
                    double dist=distance(listLoc.get(i).getLat(), listLoc.get(i).getLon(),location.getLatitude(),location.getLongitude());
                    Log.i("","dist==1=>"+dist);
                    if(dist<=mDist){  //35

                        TextView text = new TextView(MainActivityGoogleMap.this);
                        text.setTextSize(12);
                        text.setText(Html.fromHtml(listLoc.get(i).getAmt()));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.setMargins(15,0,15,180);
                        text.setLayoutParams(params);

                        if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                            text.setTextColor(getResources().getColor(R.color.green1));
                        }else{
                            text.setTextColor(getResources().getColor(R.color.red1));
                        }
                        text.setTextColor(getResources().getColor(R.color.white));

                        IconGenerator generator = new IconGenerator(MainActivityGoogleMap.this);



                        generator.setBackground(getResources().getDrawable(R.drawable.tpin3));   //pinaa2);
                        generator.setContentView(text);
                        Bitmap icon1 = generator.makeIcon();
                        String key=""+i;
                        addBitmapToMemoryCache(key,icon1);

                        Bitmap icon=getBitmapFromMemCache(key);


                        numTxt.setText(listLoc.get(i).getAmt());

                        Marker marker;   MarkerOptions m = new MarkerOptions();
                        LatLng latlon = new LatLng(listLoc.get(i).getLat(), listLoc.get(i).getLon());
                        if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                            numTxt.setTextColor(getResources().getColor(R.color.green));
                            numTxt5.setTextColor(getResources().getColor(R.color.green));
                            marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                    .icon(BitmapDescriptorFactory.fromBitmap(icon)));
//                             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            marker.setTag(i);
                        }else{
                            numTxt.setTextColor(getResources().getColor(R.color.red));
                            numTxt5.setTextColor(getResources().getColor(R.color.red));
                            marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                    .icon(BitmapDescriptorFactory.fromBitmap(icon)));
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            marker.setTag(i);
                        }

                        Locationbean lb=new Locationbean();
                        lb.setLat(listLoc.get(i).getLat());
                        lb.setLon(listLoc.get(i).getLon());
                        lb.setMarker(marker);
                        listmarker.add(lb);
                    }
                }




*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
//        Toast.makeText(MainActivityGoogleMap.this,"onStatusChanged",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(MainActivityGoogleMap.this,"onProviderEnabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(MainActivityGoogleMap.this,"onProviderDisabled",Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = (float) (Math.toDegrees(orientation[0]) + mDeclination);
            updateCamera(bearing);
        }

    }



    private void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();

        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }*/




    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivityGoogleMap.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }







    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public void playsound() {
//        AssetManager am;
        try {
//            am = ctx.getAssets();
            AssetFileDescriptor afd = MainActivityGoogleMap.this.getResources().openRawResourceFd(R.raw.bubble_sound);
            MediaPlayer  player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }

            });
            player.setLooping(false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    //    For get bitmap from memory cache
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}