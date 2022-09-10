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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gisdemo.app.mygisapplication.colorpicker.ColorPickerPopup;
import com.gisdemo.app.mygisapplication.colorpicker.PreferenceConnector;
import com.gisdemo.app.mygisapplication.spinnerpicker.DropDownListDataModel;
import com.gisdemo.app.mygisapplication.spinnerpicker.ListDropDownAdapter;
import com.gisdemo.app.mygisapplication.spinnerpicker.ListPopUpListWindowClickListener;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

//https://www.androidtutorialpoint.com/intermediate/android-map-app-showing-current-location-android/
//        https://www.javatpoint.com/android-google-map-displaying-current-location
//        https://androidclarified.com/display-current-location-google-map-fusedlocationproviderclient/
//        http:
//www.zoftino.com/android-show-current-location-on-map-example

public class ActivityGoogleMapAnalytics extends Activity implements  OnMapReadyCallback {
    private GoogleMap mMap;
    View markerView;
    public static String  filter="all";
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
    TextView tvnonres;
    TextView tvunpaid;
    TextView tvres;
    Handler h = new Handler();
    int delay = 5 * 1000;
    ArrayList<JSONArray> alist=new ArrayList<>();
    private LruCache<String, Bitmap> mMemoryCache;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


    ArrayList<Locationbean> listLoc=new ArrayList<Locationbean>();

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    EditText etfrom,etto;
    Spinner sptype;
    Button bclear,bapply;
    Button bfilter;
    LinearLayout llfilter;
    ImageView imgFilterAnalytics;
    Context mContext;
    private ArrayAdapter<String> dataAdapter;
    private ListDropDownAdapter mSpinnerAdapter;
    private ArrayList<DropDownListDataModel> mPropertyTypeArrayList  = new ArrayList<>();
    private ArrayList<JSONObject> mPropertyObjArrayList  = new ArrayList<>();
    private ArrayList<Locationbean> mPropertyLatLngArrayList  = new ArrayList<>();
    private String strPropertyType = "RESIDENTIAL",strFrom = "0" ,strTo = "0";
    public static String strWardNo;
    private String apiUrlFilter;
    String  tag_json_obj = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gmap);
        mContext = this;
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
//        mMap  = ((MapFragment) getFragmentManager().findFragmentById(R.id.g_map)).getMap();

        ImageView img=findViewById(R.id.imageView12);
        LinearLayout ll12=findViewById(R.id.ll12);
        img.setVisibility(View.VISIBLE);
        ll12.setVisibility(View.VISIBLE);

        llfilter=findViewById(R.id.llfilter);

        tvnonres=findViewById(R.id.tvnonres);
        tvunpaid=findViewById(R.id.tvunpaid);
        tvres=findViewById(R.id.tvres);

        etfrom=findViewById(R.id.etfrom);
        etto=findViewById(R.id.etto);
        sptype=findViewById(R.id.spinnere2);
        bclear=findViewById(R.id.bclear);
        bapply=findViewById(R.id.bapply);
        bfilter=findViewById(R.id.imgfilter);
        imgFilterAnalytics=findViewById(R.id.imgFilterAnalytics);

        List<String> categories = new ArrayList<String>();
        categories.add("Non Residential");
        categories.add("Residential");
        categories.add("Mixed");
        categories.add("Open Plot");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptype.setAdapter(dataAdapter);


        bfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llfilter.getVisibility()==View.GONE) {
                    bfilter.setText("Close");
                    llfilter.setVisibility(View.VISIBLE);
                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
                    llfilter.startAnimation(slide);
                }
                else {

                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
                    llfilter.startAnimation(slide);
                    llfilter.setVisibility(View.GONE);

                    bfilter.setText("Filter");
                }
            }
        });

        bapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
                llfilter.startAnimation(slide);
                llfilter.setVisibility(View.GONE);
                bfilter.setText("Filter");

            }
        });

        bclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etfrom.setText("");
                etto.setText("");
                sptype.setSelection(0);


            }
        });

        imgFilterAnalytics.setVisibility(View.VISIBLE);
        imgFilterAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterDialog();
            }
        });

        setUpSpinnerAdapter();

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
//            locationManager.requestLocationUpdates(LocationManager
//                    .GPS_PROVIDER, 3000, 1, this);

        } else {
            Toast.makeText(ActivityGoogleMapAnalytics.this,"Your GPS is off",Toast.LENGTH_SHORT).show();

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
//-----------------------------------------------------------------
        try {

            j = new JSONArray(jdata);
            filter="all";
//            setmapdata("all , comm");


                    setmapdata(filter);


          /*  if (j.length()!=0){

                double paid=0,unpaid=0,res=0,comm=0;
                for (int x=0;x<j.length();x++) { //   for (int x=0;x<j.length();x++) {
                    JSONObject obj = j.getJSONObject(x);
                    JSONArray jarr = obj.optJSONArray("Property");
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jloc = jarr.getJSONObject(i);
                        String dd=jloc.optString("Total_Due");
                        String type = jloc.optString("PROPERTY_TYPE");
                        if (dd.equalsIgnoreCase("0") || dd.equalsIgnoreCase("0.0") || dd.contains("-")) {
                        }else{
                            double p= Double.parseDouble(dd);
                            unpaid=unpaid+p;
                            if (type.equalsIgnoreCase("NON-RESIDENTIAL")){
                                double w= Double.parseDouble(dd);
                                comm=comm+w;
                            }else if(type.equalsIgnoreCase("RESIDENTIAL")){
                                double ww= Double.parseDouble(dd);
                                res=res+ww;
                            }else {

                            }
                        }
                    }
                }
//                BigDecimal big = new BigDecimal(unpaid+"");
                int ddd=(int) unpaid;
                tvunpaid.setText("₹ "+ddd);

                int ddd1=(int) comm;
                tvnonres.setText("₹ "+ddd1);

                int ddd2=(int) res;
                tvres.setText("₹ "+ddd2);


                int pinlimit  = 300;
                try{
                    if (j.length()>pinlimit){
                    }else{
                        pinlimit=j.length();
                    }
                }catch(Exception e){
                }
//                int pinlimit = j.length();
                for (int x=0;x<pinlimit;x++) { //   for (int x=0;x<j.length();x++) {
                    JSONObject obj = j.getJSONObject(x);
                    JSONArray jarr = obj.optJSONArray("Property");
                    Locationbean lb1 = new Locationbean();
                    String amt="";
                    int forlimit = 3;
                    try{
                        if (jarr.length()>forlimit){
                        }else{
                            forlimit=jarr.length();
                        }
                    }catch(Exception e){
                    }
                    for (int i = 0; i < forlimit; i++) {
                        JSONObject jloc = jarr.getJSONObject(i);
//                        lb1.setLon(Double.parseDouble(jloc.optString("LONG")));
//                        lb1.setLat(Double.parseDouble(jloc.optString("LAT")));

                        lb1.setLon(Double.parseDouble(jloc.optString("LAT")));
                        lb1.setLat(Double.parseDouble(jloc.optString("LONG")));

//                        lb1.setStatus("Not Paid");//Html.fromHtml("<font color='rgb'>"+text contain+"</font>")
                        String cdem ;
                        String upin ;
                        String tdem ;
                        String type;

                        if (jloc.isNull("Assessment_number")) {
                            cdem = jloc.optString("CURRENT_DEMAND");
                            upin = jloc.optString("UPIN");
                            tdem = jloc.optString("Total_Due");



//                            double tot=Double.parseDouble(cdem);
                            if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0")) {
                                lb1.setStatus("Paid");
                            }else{
                                lb1.setStatus("Not Paid");
                            }



                        }else {
                            cdem = jloc.optString("Duesason_DemandDate");
                            upin = jloc.optString("Assessment_number");
                            tdem = jloc.optString("Ulb_name");

//                            lb1.setStatus("Not Paid");

                            if (cdem.contains("7"))
                                lb1.setStatus("Paid");
                            else
                                lb1.setStatus("Not Paid");

                        }


                        amt= amt+"<br>"+upin + " : <b>" + cdem + "</b> / " + tdem;
                    }
                    lb1.setAmt(amt);


                    listLoc.add(lb1);


                }
            }

      */
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //- - - - - - - - - - - - - - - end onCreate - - - - - - - - - - - - - - - - - - -

/*

        Locationbean lb1=new Locationbean();
        lb1.setLon(78.36882838);
        lb1.setLat(17.446505);
        lb1.setStatus("Paid");//Html.fromHtml("<font color='rgb'>"+text contain+"</font>")
        lb1.setAmt("Capgemini:8000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb1);

        Locationbean lb2=new Locationbean();
        lb2.setLon(78.36921741);
        lb2.setLat(17.44712012);
        lb2.setStatus("Not Paid");
        lb2.setAmt("Mars:7000\nCapgemini:75000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb2);

        Locationbean lb3=new Locationbean();
        lb3.setLon(78.3702312);
        lb3.setLat(17.4473758);
        lb3.setStatus("Paid");
        lb3.setAmt("\nCapgemini:5500\nOTS:8000");
        listLoc.add(lb3);

        Locationbean lb4=new Locationbean();
        lb4.setLon(78.37083825);
        lb4.setLat(17.44700762);
        lb4.setStatus("Not Paid");
        lb4.setAmt("6000");
        listLoc.add(lb4);

        Locationbean lb5=new Locationbean();
        lb5.setLon(78.37016022);
        lb5.setLat(17.44660315);
        lb5.setStatus("Paid");
        lb5.setAmt("Capgemini:7000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb5);

        Locationbean lb6=new Locationbean();
        lb6.setLon(78.37063394);
        lb6.setLat(17.44596197);
        lb6.setStatus("Not Paid");
        lb6.setAmt("5000");
        listLoc.add(lb6);

        Locationbean lb7=new Locationbean();
        lb7.setLon(78.37185792);
        lb7.setLat(17.44678607);
        lb7.setStatus("Paid");
        lb7.setAmt("CTS:8000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb7);

        Locationbean lb8=new Locationbean();
        lb8.setLon(78.37118525);
        lb8.setLat(17.44741092);
        lb8.setStatus("Not Paid");
        lb8.setAmt("5000");
        listLoc.add(lb8);

        Locationbean lb9=new Locationbean();
        lb9.setLon(78.37072519);
        lb9.setLat(17.44810437);
        lb9.setStatus("Not Paid");
        lb9.setAmt("Capgemini:7000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb9);

        Locationbean lb10=new Locationbean();
        lb10.setLon(78.37259296	);
        lb10.setLat(17.44796496);
        lb10.setStatus("Not Paid");
        lb10.setAmt("5000");
        listLoc.add(lb10);

        Locationbean lb11=new Locationbean();
        lb11.setLon(78.37314344		);
        lb11.setLat(17.44745298);
        lb11.setStatus("Not Paid");
        lb11.setAmt("TCS:2000\nCapgemini:75000\nOTS:8000\nMIST:3800\nCTS:10500");
        listLoc.add(lb11);

        Locationbean lb12=new Locationbean();
        lb12.setLon(78.3726318	);
        lb12.setLat(17.44720106);
        lb12.setStatus("Not Paid");
        lb12.setAmt("6000");
        listLoc.add(lb12);

*/
/*      ------lakdi ka pool--------
        Lat	         Long	   Owner   Tax due
        17.401772	78.458494	A	    101
        17.401933	78.458497	B   	102
        17.402092	78.4585	    C	    103
        17.401756	78.458731	D	    104
        17.401707	78.45919	E	    105
        17.401936	78.459156	F	    106
        17.402214	78.459114	G	    107
        17.402397	78.459086	H	    108
        17.402429	78.458516	I	    109*//*



        Locationbean lb13=new Locationbean();
        lb13.setLon(78.458494	);
        lb13.setLat(17.401772);
        lb13.setStatus("Not Paid");
        lb13.setAmt("A : 101");
        listLoc.add(lb13);

        Locationbean lb14=new Locationbean();
        lb14.setLon(78.458497	);
        lb14.setLat(17.401933);
        lb14.setStatus("Not Paid");
        lb14.setAmt("B : 102");
        listLoc.add(lb14);

        Locationbean lb15=new Locationbean();
        lb15.setLon(78.4585	);
        lb15.setLat(17.402092);
        lb15.setStatus("Not Paid");
        lb15.setAmt("C : 103");
        listLoc.add(lb15);

        Locationbean lb16=new Locationbean();
        lb16.setLon(78.458731	);
        lb16.setLat(17.401756);
        lb16.setStatus("Not Paid");
        lb16.setAmt("D : 104");
        listLoc.add(lb16);

        Locationbean lb17=new Locationbean();
        lb17.setLon(78.45919	);
        lb17.setLat(17.401707);
        lb17.setStatus("Not Paid");
        lb17.setAmt("E : 105");
        listLoc.add(lb17);


        Locationbean lb18=new Locationbean();
        lb18.setLon(78.459156	);
        lb18.setLat(17.401936);
        lb18.setStatus("Not Paid");
        lb18.setAmt("F : 106");
        listLoc.add(lb18);

        Locationbean lb19=new Locationbean();
        lb19.setLon(78.459114	);
        lb19.setLat(17.402214);
        lb19.setStatus("Not Paid");
        lb19.setAmt("G : 107");
        listLoc.add(lb19);

        Locationbean lb20=new Locationbean();
        lb20.setLon(78.459086	);
        lb20.setLat(17.402397);
        lb20.setStatus("Not Paid");
        lb20.setAmt("H : 108");
        listLoc.add(lb20);

        Locationbean lb21=new Locationbean();
        lb21.setLon(78.458516	);
        lb21.setLat(17.402429);
        lb21.setStatus("Not Paid");
        lb21.setAmt("I : 109");
        listLoc.add(lb21);

        Locationbean lb22=new Locationbean();
        lb22.setLon(80.6636776	);
        lb2.setLat(16.5000092);
        lb22.setStatus("Not Paid");
        lb22.setAmt("110");
        listLoc.add(lb22);

        Locationbean lb23=new Locationbean();
        lb23.setLon(80.66335792886095	);
        lb23.setLat(16.49989085309833);
        lb23.setStatus("Not Paid");
        lb23.setAmt("111");
        listLoc.add(lb23);


        Locationbean lb24=new Locationbean();
        lb24.setLon(80.66362034529449	);
        lb24.setLat(16.49940404713015);
        lb24.setStatus("Not Paid");
        lb24.setAmt("112");
        listLoc.add(lb24);

//---------------------------nagpur---------------------------
       */
/*  lat x          lang y      Property Index  Tax due
        21.154193   79.077314       216203          6555
        21.154495   79.07827        521325          56666
        21.154994   79.078217       522331          65000
        21.154898   79.077652       726081          6500
        21.154914   79.077519       879145          5000
        21.154924   79.077314       1032209         5000*//*

//------------------------------------------------------------

        Locationbean lb25=new Locationbean();
        lb25.setLon(79.077314	);
        lb25.setLat(21.154193);
        lb25.setStatus("Not Paid");
        lb25.setAmt("216203 : 6555");
        listLoc.add(lb25);

        Locationbean lb26=new Locationbean();
        lb26.setLon(79.07827	);
        lb26.setLat(21.154495);
        lb26.setStatus("Not Paid");
        lb26.setAmt("521325 : 56666");
        listLoc.add(lb26);

        Locationbean lb27=new Locationbean();
        lb27.setLon(79.078217	);
        lb27.setLat(21.154994);
        lb27.setStatus("Not Paid");
        lb27.setAmt("522331 : 65000");
        listLoc.add(lb27);

        Locationbean lb28=new Locationbean();
        lb28.setLon(79.077652	);
        lb28.setLat(21.154898);
        lb28.setStatus("Not Paid");
        lb28.setAmt("726081 : 6500");
        listLoc.add(lb28);

        Locationbean lb29=new Locationbean();
        lb29.setLon(79.077519	);
        lb29.setLat(21.154914);
        lb29.setStatus("Not Paid");
        lb29.setAmt("879145 : 5000");
        listLoc.add(lb29);

        Locationbean lb30=new Locationbean();
        lb30.setLon(79.077314	);
        lb30.setLat(21.154924);
        lb30.setStatus("Not Paid");
        lb30.setAmt("1032209 : 5000");
        listLoc.add(lb30);


        Locationbean lb31=new Locationbean();
        lb31.setLon(73.029945);
        lb31.setLat(19.009256);
        lb31.setStatus("Not Paid");
        lb31.setAmt("Mayuresh Square : 40000\nMayuresh Square : 40000\nMayuresh Square : 40000\nMayuresh Square : 40000\n"); // building name : due
        listLoc.add(lb31);

        Locationbean lb32=new Locationbean();
        lb32.setLon(73.030613);
        lb32.setLat(19.009611);
        lb32.setStatus("Not Paid");
        lb32.setAmt("Croma : 30000\nCroma : 30000\nCroma : 30000\n"); // building name : due
        listLoc.add(lb32);

        Locationbean lb33=new Locationbean();
        lb33.setLon(73.031052);
        lb33.setLat(19.00984);
        lb33.setStatus("Not Paid");
        lb33.setAmt("True Value Interior : 35000"); // building name : due
        listLoc.add(lb33);

        Locationbean lb34=new Locationbean();
        lb34.setLon(73.031732);
        lb34.setLat(19.010103);
        lb34.setStatus("Not Paid");
        lb34.setAmt("Brahma House : 30000\nBrahma House : 30000\nBrahma House : 30000\nBrahma House : 30000\nBrahma House : 30000\n"); // building name : due
        listLoc.add(lb34);

        Locationbean lb35=new Locationbean();
        lb35.setLon(73.032456);
        lb35.setLat(19.01025);
        lb35.setStatus("Not Paid");
        lb35.setAmt("Lenovo : 45000\nLenovo : 45000\nLenovo : 45000\nLenovo : 45000\nLenovo : 45000\n"); // building name : due
        listLoc.add(lb35);

        Locationbean lb36=new Locationbean();
        lb36.setLon(73.032796);
        lb36.setLat(19.011124);
        lb36.setStatus("Not Paid");
        lb36.setAmt("Bhumi Mall : 40000"); // building name : due
        listLoc.add(lb36);

        Locationbean lb37=new Locationbean();
        lb37.setLon(73.033216);
        lb37.setLat(19.010561);
        lb37.setStatus("Not Paid");
        lb37.setAmt("Mall : 27000\nMall : 27000\nMall : 27000\nMall : 27000\nMall : 27000\nMall : 27000\n"); // building name : due
        listLoc.add(lb37);

        Locationbean lb38=new Locationbean();
        lb38.setLon(73.033671);
        lb38.setLat(19.010697);
        lb38.setStatus("Not Paid");
        lb38.setAmt("Mall : 32000"); // building name : due
        listLoc.add(lb38);

        Locationbean lb39=new Locationbean();
        lb39.setLon(73.033582);
        lb39.setLat(19.01134);
        lb39.setStatus("Not Paid");
        lb39.setAmt("smart Aviation Software : 42000"); // building name : due
        listLoc.add(lb39);

        Locationbean lb40=new Locationbean();
        lb40.setLon(73.034216);
        lb40.setLat(19.010778);
        lb40.setStatus("Not Paid");
        lb40.setAmt("Jai Tower : 53000"); // building name : due
        listLoc.add(lb40);

        Locationbean lb41=new Locationbean();
        lb41.setLon(73.034333);
        lb41.setLat(19.011473);
        lb41.setStatus("Not Paid");
        lb41.setAmt("Under Construction : 32000"); // building name : due
        listLoc.add(lb41);

        Locationbean lb42=new Locationbean();
        lb42.setLon(73.035125);
        lb42.setLat(19.011239);
        lb42.setStatus("Not Paid");
        lb42.setAmt("Ashwith : 28000\nAshwith : 28000\nAshwith : 28000\nAshwith : 28000\nAshwith : 28000\n"); // building name : due
        listLoc.add(lb42);

        Locationbean lb43=new Locationbean();
        lb43.setLon(73.034953);
        lb43.setLat(19.010902);
        lb43.setStatus("Not Paid");
        lb43.setAmt("White door : 23000"); // building name : due
        listLoc.add(lb43);

        Locationbean lb44=new Locationbean();
        lb44.setLon(73.034809);
        lb44.setLat(19.011767);
        lb44.setStatus("Not Paid");
        lb44.setAmt("Trishool: 48000"); // building name : due
        listLoc.add(lb44);

        Locationbean lb45=new Locationbean();
        lb45.setLon(73.035623);
        lb45.setLat(19.011384);
        lb45.setStatus("Not Paid");
        lb45.setAmt("Mahesh Lunch : 47000"); // building name : due
        listLoc.add(lb45);

        Locationbean lb46=new Locationbean();
        lb46.setLon(73.035924);
        lb46.setLat(19.011441);
        lb46.setStatus("Not Paid");
        lb46.setAmt("Spize & Grills : 48000\nSpize & Grills : 48000\nSpize & Grills : 48000\n"); // building name : due
        listLoc.add(lb46);

        Locationbean lb47=new Locationbean();
        lb47.setLon(73.035437);
        lb47.setLat(19.011916);
        lb47.setStatus("Not Paid");
        lb47.setAmt("Times Square : 52000\nTimes Square : 52000\nTimes Square : 52000\nTimes Square : 52000\n"); // building name : due
        listLoc.add(lb47);
*/

/*
       Locationbean test=new Locationbean(); //17.446922, 78.369433
        test.setLon(78.369433	);
        test.setLat(17.446922);
        test.setStatus("Not Paid");
        test.setAmt("1032209 : 5900");
        listLoc.add(test);*/






    }
    public void setmapdata(String filter){
        try {
            listLoc.clear();
            mPropertyObjArrayList.clear();
            mPropertyLatLngArrayList.clear();
            if (j.length() != 0) {

                double paid = 0, unpaid = 0, res = 0, comm = 0;
                for (int x = 0; x < j.length(); x++) { //   for (int x=0;x<j.length();x++) {
                    JSONObject obj = j.getJSONObject(x);
                    JSONArray jarr = obj.optJSONArray("Property");
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jloc = jarr.getJSONObject(i);
                        String dd = jloc.optString("Total_Due");
                        String type = jloc.optString("PROPERTY_TYPE");
                        if (dd.equalsIgnoreCase("0") || dd.equalsIgnoreCase("0.0") || dd.contains("-")) {
                        } else {
                            double p = Double.parseDouble(dd);
                            unpaid = unpaid + p;
                            if (type.equalsIgnoreCase("NON-RESIDENTIAL")) {
                                double w = Double.parseDouble(dd);
                                comm = comm + w;
                            } else if (type.equalsIgnoreCase("RESIDENTIAL")) {
                                double ww = Double.parseDouble(dd);
                                res = res + ww;
                            } else {

                            }
                        }
                    }
                }
                int ddd = (int) unpaid;
                tvunpaid.setText("₹ " + ddd);

                int ddd1 = (int) comm;
                tvnonres.setText("₹ " + ddd1);

                int ddd2 = (int) res;
                tvres.setText("₹ " + ddd2);

                int pinlimit = 300;
                try {
                    if (j.length() > pinlimit) {
                    } else {
                        pinlimit = j.length();
                    }
                } catch (Exception e) {
                }
                for (int x = 0; x < pinlimit; x++) { //   for (int x=0;x<j.length();x++) {
                    JSONObject obj = j.getJSONObject(x);
                    JSONArray jarr = obj.optJSONArray("Property");
                    Locationbean lb1 = new Locationbean();
                    String amt = "";
                    String totdue = "";

                    int forlimit = 3;
                    try {
                        if (jarr.length() > forlimit) {
                        } else {
                            forlimit = jarr.length();
                        }
                    } catch (Exception e) {
                    }

                    for (int i = 0; i < forlimit; i++) {
                        JSONObject jloc = jarr.getJSONObject(i);
//                        lb1.setLon(Double.parseDouble(jloc.optString("LONG")));
//                        lb1.setLat(Double.parseDouble(jloc.optString("LAT")));
                        double lat  = Double.parseDouble(jloc.optString("LAT"));
                        double lng  = Double.parseDouble(jloc.optString("LONG"));

                        //Log.e("Analytics original","LAT ---> " + jloc.optString("LAT") + " " + "LONG ---> " + jloc.optString("LONG"));
                        lb1.setLon(lat);
                        lb1.setLat(lng);
                        mPropertyLatLngArrayList.add(lb1);
                        mPropertyObjArrayList.add(jloc);
                        //lb1.setLon(Double.parseDouble(jloc.optString("LAT")));
                        //lb1.setLat(Double.parseDouble(jloc.optString("LONG")));


//                        lb1.setStatus("Not Paid");//Html.fromHtml("<font color='rgb'>"+text contain+"</font>")
                        String cdem;
                        String upin;
                        String tdem;
                        String type;

                        if (jloc.isNull("Assessment_number")) {
                            cdem = jloc.optString("CURRENT_DEMAND");
                            upin = jloc.optString("UPIN");
                            tdem = jloc.optString("Total_Due");

                            String type2 = jloc.optString("PROPERTY_TYPE");
                            lb1.settype(type2);
                            if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0") || tdem.contains("-")) {
                                lb1.setStatus("Paid");
                            } else {
                                lb1.setStatus("Not Paid");
                            }
                        } else {
                            cdem = jloc.optString("Duesason_DemandDate");
                            upin = jloc.optString("Assessment_number");
                            tdem = jloc.optString("Ulb_name");
//                            lb1.setStatus("Not Paid");
                            if (cdem.contains("7"))
                                lb1.setStatus("Paid");
                            else
                                lb1.setStatus("Not Paid");
                        }
                        amt = amt + "<br>" + upin + " : <b>" + cdem + "</b> / " + tdem;
                        totdue=tdem;

                        if (isUngroupPointViewEnabled()){
                            lb1.setAmt(amt);
                            if (filter.equalsIgnoreCase("comm")){
                                if (lb1.gettype().equalsIgnoreCase("NON-RESIDENTIAL") ) {
                                    if (totdue.equalsIgnoreCase("0") || totdue.equalsIgnoreCase("0.0") || totdue.contains("-")) {}else{

                                        listLoc.add(lb1);
                                        Log.i("---->", "---->>>" + lb1.amt);

                                    }
                                }
                            }else{
                                listLoc.add(lb1);
                            }
                        }
                    }

                    if (!isUngroupPointViewEnabled()){
                        lb1.setAmt(amt);
                        if (filter.equalsIgnoreCase("comm")){
                            if (lb1.gettype().equalsIgnoreCase("NON-RESIDENTIAL") ) {
                                if (totdue.equalsIgnoreCase("0") || totdue.equalsIgnoreCase("0.0") || totdue.contains("-")) {}else{

                                    listLoc.add(lb1);
                                    Log.i("---->", "---->>>" + lb1.amt);
                                }
                            }
                        }else{
                            listLoc.add(lb1);
                        }
                    }
                    /*lb1.setAmt(amt);
                    if (filter.equalsIgnoreCase("comm")){
                        if (lb1.gettype().equalsIgnoreCase("NON-RESIDENTIAL") ) {
                            if (totdue.equalsIgnoreCase("0") || totdue.equalsIgnoreCase("0.0") || totdue.contains("-")) {}else{

                                listLoc.add(lb1);
                                Log.i("---->", "---->>>" + lb1.amt);
                                Log.e("Analytics","NON-RESIDENTIAL  " + "LAT ---> " + lb1.getLat() + " " + "LONG ---> " + lb1.getLon());

                            }
                        }
                    }else{
                        listLoc.add(lb1);
                        Log.e("Analytics","ELSE  " + "LAT ---> " + lb1.getLat() + " " + "LONG ---> " + lb1.getLon());
                    }*/
                }
            }

        }catch (Exception e)
        {}

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

       /* numTxt.setText("8000");
        numTxt.setTextColor(getResources().getColor(R.color.green));
        numTxt5.setTextColor(getResources().getColor(R.color.green));
        MarkerOptions m1 = new MarkerOptions();
        LatLng seattle = new LatLng(17.4465049972022, 78.3688283763602);
        Marker mm = mMap.addMarker(m1.position(seattle).snippet("8000").title("Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm.setTag(1);

        numTxt.setText("7000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m2 = new MarkerOptions();
        LatLng seattle2 = new LatLng(17.4471201183609, 78.3692174100899);
        Marker mm2 = mMap.addMarker(m2.position(seattle2).snippet("7000").title("Not Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm2.setTag(2);

        numTxt.setText("5500");
        numTxt.setTextColor(getResources().getColor(R.color.green));
        numTxt5.setTextColor(getResources().getColor(R.color.green));
        MarkerOptions m3 = new MarkerOptions();
        LatLng seattle3 = new LatLng(17.4473758011127, 78.3702312040499
        );
        Marker mm3 = mMap.addMarker(m3.position(seattle3).snippet("5500").title("Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));

        mm3.setTag(3);
        numTxt.setText("6000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m4 = new MarkerOptions();
        LatLng seattle4 = new LatLng(17.4470076211114, 78.3708382549897);
        Marker mm4 = mMap.addMarker(m4.position(seattle4).snippet("6000").title("Not Paid").visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm4.setTag(4);

        numTxt.setText("7000");
        numTxt.setTextColor(getResources().getColor(R.color.green));
        numTxt5.setTextColor(getResources().getColor(R.color.green));
        MarkerOptions m5 = new MarkerOptions();
        LatLng seattle5 = new LatLng(17.4466031521207, 78.3701602247872);
        Marker mm5 = mMap.addMarker(m5.position(seattle5).snippet("7000").title("Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm5.setTag(5);

        numTxt.setText("5000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m6 = new MarkerOptions();
        LatLng seattle6 = new LatLng(17.4459619650419, 78.3706339440462);
        Marker mm6 = mMap.addMarker(m6.position(seattle6).snippet("5000").title("Not Paid").visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm6.setTag(6);

        numTxt.setText("8000");
        numTxt.setTextColor(getResources().getColor(R.color.green));
        numTxt5.setTextColor(getResources().getColor(R.color.green));
        MarkerOptions m7 = new MarkerOptions();
        LatLng seattle7 = new LatLng(17.4467860700014, 78.3718579172869);
        Marker mm7 = mMap.addMarker(m7.position(seattle7).snippet("8000").title("Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm7.setTag(7);

        numTxt.setText("5000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m8 = new MarkerOptions();
        LatLng seattle8 = new LatLng(17.447410923101, 78.371185249282);
        Marker mm8 = mMap.addMarker(m8.position(seattle8).snippet("5000").title("Not Paid").visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm8.setTag(8);
//mm8.showInfoWindow();

        numTxt.setText("7000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m9 = new MarkerOptions();
        LatLng seattle9 = new LatLng(17.4481043672958, 78.3707251897851);
        Marker mm9 = mMap.addMarker(m9.position(seattle9).snippet("7000").title("Not Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm9.setTag(9);
//        mm9.showInfoWindow();


        numTxt.setText("5000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m10 = new MarkerOptions();
        LatLng seattle10 = new LatLng(17.4479649629204, 78.3725929641385);
        Marker mm10 = mMap.addMarker(m10.position(seattle10).snippet("5000").title("Not Paid").visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm10.setTag(10);
//        mm10.showInfoWindow();

        numTxt.setText("2000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m11 = new MarkerOptions();
        LatLng seattle11 = new LatLng(17.4474529771968, 78.3731434389579);
        Marker mm11 = mMap.addMarker(m11.position(seattle11).snippet("2000").title("Not Paid").visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm11.setTag(11);


        numTxt.setText("6000");
        numTxt.setTextColor(getResources().getColor(R.color.red));
        numTxt5.setTextColor(getResources().getColor(R.color.red));
        MarkerOptions m12 = new MarkerOptions();
        LatLng seattle12 = new LatLng(17.4472010605481, 78.3726317967269);
        Marker mm12 = mMap.addMarker(m12.position(seattle12).snippet("6000").title("Not Paid")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView))));
        mm12.setTag(12);

*/


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = (int)(marker.getTag());
                Dialog d=new Dialog(ActivityGoogleMapAnalytics.this);
                d.setContentView(R.layout.dlg);
                d.setCancelable(true);
                d.show();
                LinearLayout ll11=(LinearLayout) d.findViewById(R.id.ll11);
                if (isUngroupPointViewEnabled()){
                    Log.e("Analytics","mPropertyObjArrayList === "+mPropertyObjArrayList.size());
                    Log.e("Analytics","position === "+position);
                    inflateInfoWindowLayout(mPropertyObjArrayList.get(position),ll11);
                } else {
                    try {
                        JSONObject obj = j.getJSONObject(position);
                        JSONArray jarr = obj.optJSONArray("Property");
                        for (int k=0;k<jarr.length();k++) {
                            JSONObject jo=jarr.getJSONObject(k);
                            inflateInfoWindowLayout(jo,ll11);
//                        ll11.addView(layout_number);
                        }

                    }catch (Exception x){
                    }
                }
                return true;
            }
        });

      /* // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
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
        Location location = locationManager.getLastKnownLocation(provider);
        double longitude1 = location.getLongitude();
        double latitude1 = location.getLatitude();
        Log.i("===111>","Longitude:" + Double.toString(longitude1) + "\nLatitude:" + Double.toString(latitude1));*/

/*
//        locationTrack = new LocationTrack(ActivityGoogleMapAnalytics.this);
//        if (locationTrack.canGetLocation()) {
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
            if (location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }else{
                Location getLastLocation = locationManager.getLastKnownLocation
                        (LocationManager.NETWORK_PROVIDER);
                longitude = getLastLocation.getLongitude();
                latitude = getLastLocation.getLatitude();
                Log.i("lastknownlocation=>", "Longitude:" + longitude + "\nLatitude:" +latitude);
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
            }*/

        //------------------------------


        //-----------------tepm for mumbai---
//             latitude=19.009256;
//             longitude=73.029945;
        //-----------------------------------


        //-----------------tepm for cdma---
        double longitude;
        double latitude ;
//            latitude=17.3056285;
//            longitude=78.5224135;
        //-----------------------------------


        //-----------------tepm for Nagpur---
//        latitude=21.137251;
//        longitude=79.07957;
        //-----------------------------------



        if (listLoc!=null && listLoc.size()>0){
            latitude=listLoc.get(0).getLat();
            longitude=listLoc.get(0).getLon();


            mylat=listLoc.get(0).getLat();
            mylon=listLoc.get(0).getLon();


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
                    .zoom(15)                   // Sets the zoom
        //                .bearing(30)                // Sets the orientation of the camera to east
        //                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
//            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

          /*  try {
                KmlLayer layer = new KmlLayer(mMap,R.raw.pheonix3, this.getApplicationContext()); // creating the kml layer
                layer.addLayerToMap();// adding kml layer with the **google map**
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivityGoogleMap.this,"catch"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }*/

//            final Bitmap dd= createDrawableFromView(this, markerView);
        h.postDelayed(new Runnable(){
            public void run(){
                listmarker.clear();
//                Log.i("size------>",""+listLoc.size());
                for (int i=0;i<listLoc.size();i++) {
//                        double dist=distance(listLoc.get(i).getLat(), listLoc.get(i).getLon(),mylat,mylon);
//                        Log.i("","dist===>"+dist);
//                        if(dist<=35){
                    //---------------------------
                            /*TextView text = new TextView(ActivityGoogleMapAnalytics.this);
                            text.setTextSize(12);
                            text.setText(Html.fromHtml(listLoc.get(i).getAmt()));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(15,0,15,180);
                            text.setLayoutParams(params);

                            if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                                text.setTextColor(getResources().getColor(R.color.green));
                            }else{
                                text.setTextColor(getResources().getColor(R.color.red));
                            }
                            IconGenerator generator = new IconGenerator(ActivityGoogleMapAnalytics.this);
//                            generator.setBackground(getResources().getDrawable(R.drawable.g44));

//                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pinaa2).copy(Bitmap.Config.ARGB_8888, true);
//                            Canvas canvas = new Canvas(bitmap);
//                            canvas.drawText(offersCount,
//                                    canvas.getWidth()/2,
//                                    canvas.getHeight()/2 - ((text.getFontMetrics().ascent + clustersPaint.getFontMetrics().descent) / 2) ,
//                                    clustersPaint);


                            generator.setBackground(getResources().getDrawable(R.drawable.pinaa2));
                            generator.setContentView(text);
                            Bitmap icon1 = generator.makeIcon();
                            String key=""+i;
                            addBitmapToMemoryCache(key,icon1);

                            Bitmap icon=getBitmapFromMemCache(key);

                            //------------------
                            *//*



                     *//*

                            numTxt.setText(listLoc.get(i).getAmt());
*/
                    MarkerOptions m = new MarkerOptions();
                    LatLng latlon = new LatLng(listLoc.get(i).getLat(), listLoc.get(i).getLon());
                    final Marker marker;
                    if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                        numTxt.setTextColor(getResources().getColor(R.color.green));
//                                text.setTextColor(getResources().getColor(R.color.green));
                        int height = 25;
                        int width = 25;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.greenpoint1);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);



//                        marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
//                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                        marker = mMap.addMarker(m.position(latlon)//.snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

//                                playsound();
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        marker.setTag(i);
                    }else{
                        numTxt.setTextColor(getResources().getColor(R.color.red));
//                                text.setTextColor(getResources().getColor(R.color.red));

                        int height = 25;
                        int width = 25;
                        /*BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.redpoint1);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*/

                        Bitmap smallMarker = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(smallMarker);
                        int color = PreferenceConnector.readInteger(mContext,PreferenceConnector.KEY_UNPAID_PIN_COLOR,R.color.red2);
                        Drawable shape = Utils.changeDrawableColor(mContext,R.drawable.unpaid_pin,color);
                        shape.setBounds(0, 0, smallMarker.getWidth(), smallMarker.getHeight());
                        shape.draw(canvas);

//                        marker = mMap.addMarker(m.position(latlon).snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
//                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                        marker = mMap.addMarker(m.position(latlon)//.snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

//                                playsound();
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        marker.setTag(i);
                    }




                    Locationbean lb=new Locationbean();
                    lb.setLat(listLoc.get(i).getLat());
                    lb.setLon(listLoc.get(i).getLon());
                    lb.setMarker(marker);
                    listmarker.add(lb);
                    //-------------------
                          /*  final Handler handler = new Handler();
                            final long startTime = SystemClock.uptimeMillis();
                            final long duration = 2000;

                            Projection proj = mMap.getProjection();
                            final LatLng markerLatLng = marker.getPosition();
                            Point startPoint = proj.toScreenLocation(markerLatLng);
                            startPoint.offset(0, -100);
                            final LatLng startLatLng = proj.fromScreenLocation(startPoint);

                            final Interpolator interpolator = new BounceInterpolator();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    int z = 0;
                                    long elapsed = SystemClock.uptimeMillis() - startTime;
                                    float t = interpolator.getInterpolation((float) elapsed / duration);
                                    double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                                    double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                                    marker.setPosition(new LatLng(lat, lng));

                                    if (t < 1.0) {
                                        if (z ==0) {
//                                             Post again 16ms later.
                                        handler.postDelayed(this, 16);
                                        z=1;
                                        }
                                    }
                                }
                            });

*/
//                        }
                }
                h.postDelayed(this, delay);
            }
        }, delay);













//            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
//        } else {
//            locationTrack.showAlertDialog();
//        }



//        locationManager.get


//        mMap.animateCamera(yourLocation);

       /* mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(ActivityGoogleMapAnalytics.this,"hii",2000).show();
                *//*Intent intent = new Intent(ActivityGoogleMapAnalytics.this ,PopulationCharts.class);
                String title = marker.getTitle();
                intent.putExtra("markertitle", title);
                startActivity(intent);*//*
            }
        });*/



    }


    private void inflateInfoWindowLayout(JSONObject jo,LinearLayout ll11){
        LayoutInflater inflater = LayoutInflater.from(ActivityGoogleMapAnalytics.this);
        View layout_number = inflater.inflate(R.layout.listitem, ll11, false);
        TextView tv11 = (TextView) layout_number.findViewById(R.id.textView11);
        TextView tv22 = (TextView) layout_number.findViewById(R.id.textView22);
        TextView tvtype= (TextView) layout_number.findViewById(R.id.tvstatus);
//                number.setTag(i);
        tv11.setText("" +jo.optString("INDEX_NUMBER"));
        tv22.setText(""+jo.optString("Total_Due"));  //CURRENT_DEMAND

        String tdem = jo.optString("Total_Due");
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




        if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0")|| tdem.contains("-"))
        {
//                            lb1.setStatus("Paid");

            tv11.setTextColor(getResources().getColor(R.color.green1));
            tv22.setTextColor(getResources().getColor(R.color.green1));
        }else{

            tv11.setTextColor(getResources().getColor(R.color.red1));
            tv22.setTextColor(getResources().getColor(R.color.red1));
        }
//                            lb1.setStatus("Not Paid");


        if (filter.equalsIgnoreCase("comm")){
            if (type.equalsIgnoreCase("NON-RESIDENTIAL"))
            {if (tdem.equalsIgnoreCase("0") || tdem.equalsIgnoreCase("0.0")|| tdem.contains("-")){}else{
                ll11.addView(layout_number);
                Log.i("-dialog--->","---->>>"+tdem + " "+jo.optString("INDEX_NUMBER"));
            }}
        }else{
            ll11.addView(layout_number);
        }
    }



       //     "http://114.79.182.179:8080/APIcalll/getTaxDetails?id=" + index;

    public void itemclick(View v){

        RelativeLayout rl=(RelativeLayout)  v.getParent();
        LinearLayout ll1=(LinearLayout)rl.getChildAt(0);
        TextView tv=(TextView)ll1.getChildAt(0);
        String index=tv.getText().toString();
        String url = "http://114.79.182.179:8080/APIcalll/propertyDCB?index_number=" + index;
        Log.e("Analytics"," ---> "+ url);
        final ProgressDialog pDialog = new ProgressDialog(ActivityGoogleMapAnalytics.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("--->", response.toString());
                        Log.e("Analytics"," ---> "+ response.toString());
                        pDialog.hide();
                        if (response==null) {
                            Toast.makeText(ActivityGoogleMapAnalytics.this,"Property not found",Toast.LENGTH_SHORT).show();//value
                        }else{
                            Intent i = new Intent(ActivityGoogleMapAnalytics.this, TaxDetails.class);
                            i.putExtra("json", response.toString());
                            startActivity(i);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("--->>", "Error: " + error.getMessage());
                Toast.makeText(ActivityGoogleMapAnalytics.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, "");


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

//    private void showDefaultLocation() {
//        Toast.makeText(this, "Location permission not granted, " +
//                        "showing default location",
//                Toast.LENGTH_SHORT).show();
//
//
////        17.446949, 78.373594
//        LatLng redmond = new LatLng(  17.446949, 78.373594);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(redmond,14.0f));
////        CameraUpdateFactory.newLatLngZoom(new LatLng(xxxx,xxxx) , 14.0f) );
//        mMap.setMinZoomPreference(25);
//    }
/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
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

                } else {
//                    showDefaultLocation();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER


                }
                return;
            }

        }
    }
//

*/


//    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
//            new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//                    mMap.setMinZoomPreference(25);
//                    return false;
//                }
//            };

   /* private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };*/

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

                } else {
                    try {
                        j = new JSONArray(jdata);
                        setmapdata(filter);
                        createMapMarkers();
                        if (listLoc.size()>0){
                            LatLng coordinate = new LatLng(listLoc.get(0).getLat(), listLoc.get(0).getLon()); //Store these lat lng values somewhere. These should be constant.
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                    coordinate, 15);
                            mMap.animateCamera(location);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivityGoogleMapAnalytics.this)
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
            AssetFileDescriptor afd = ActivityGoogleMapAnalytics.this.getResources().openRawResourceFd(R.raw.bubble_sound);
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

    private void openFilterDialog(){
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.dialog_filter);

        final View viewColorUnpaid      =   mDialog.findViewById(R.id.dialog_view_color_unpaid);
        final ImageView imgClose        =   mDialog.findViewById(R.id.dialog_img_close);
        Button btnApply                 =   mDialog.findViewById(R.id.dialog_btn_apply);
        Button btnClear                 =   mDialog.findViewById(R.id.dialog_btn_clear);
        final EditText edtFrom          =   mDialog.findViewById(R.id.dialog_edt_amount_from);
        final EditText edtTo            =   mDialog.findViewById(R.id.dialog_edt_amount_to);
        final CheckBox chkUngroupPoints =   mDialog.findViewById(R.id.dialog_chk_ungroup_points);
        final TextView
                txtFilterPropertyType   =   mDialog.findViewById(R.id.dialog_txt_property_type_filter);
        final RelativeLayout
                spnPropertyType         =   mDialog.findViewById(R.id.dialog_spn_property_type);

        if (!strPropertyType.trim().isEmpty()){
            for (DropDownListDataModel model: mPropertyTypeArrayList) {
                if (model.getId().equalsIgnoreCase(strPropertyType)){
                    txtFilterPropertyType.setText(model.getName());
                    break;
                }
            }
        }

        if (!strFrom.trim().isEmpty() && !strFrom.equals("0")){
            edtFrom.setText(strFrom);
        }

        if (!strTo.trim().isEmpty() && !strTo.equals("0")){
            edtTo.setText(strTo);
        }

        if (isUngroupPointViewEnabled()){
            chkUngroupPoints.setChecked(true);
        }else {
            chkUngroupPoints.setChecked(false);
        }

        int color = PreferenceConnector.readInteger(mContext,PreferenceConnector.KEY_UNPAID_PIN_COLOR,R.color.red2);
        viewColorUnpaid.setBackgroundColor(color);
        viewColorUnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker(viewColorUnpaid);
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        spnPropertyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListPopUpDialog(
                        spnPropertyType,
                        spnPropertyType.getWidth(),
                        new ListPopUpListWindowClickListener() {
                            @Override
                            public void onItemClicked(int position) {
                                clearLastSelectionDocument(mPropertyTypeArrayList);
                                DropDownListDataModel model = mPropertyTypeArrayList.get(position);
                                model.setSelected(true);
                                strPropertyType = model.getId();
                                txtFilterPropertyType.setText(model.getName());
                            }
                        });
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Utils.getBackgroundColorFromView(viewColorUnpaid);
                PreferenceConnector.writeInteger(mContext,PreferenceConnector.KEY_UNPAID_PIN_COLOR,color);
                String from  = edtFrom.getText().toString().trim();
                String to    = edtTo.getText().toString().trim();

                if (!from.trim().isEmpty()){
                    strFrom = from;
                }else {
                    strFrom = "0";
                }

                if (!to.trim().isEmpty()){
                    strTo = to;
                }else {
                    strTo = "0";
                }

                if (chkUngroupPoints.isChecked()){
                    setUngroupPointViewEnabled(true);
                }else {
                    setUngroupPointViewEnabled(false);
                }

                callFilterApi();
                mDialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPropertyType = "RESIDENTIAL";
                strFrom = "0";
                strTo = "0";
                setUngroupPointViewEnabled(false);
                PreferenceConnector.writeInteger(mContext,PreferenceConnector.KEY_UNPAID_PIN_COLOR, Color.RED);
                callClearFilterApi();
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void openColorPicker(final View colorPicker){

        new ColorPickerPopup.Builder(mContext)
                .initialColor(Utils.getBackgroundColorFromView(colorPicker))
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .onlyUpdateOnTouchEventUp(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        colorPicker.setBackgroundColor(color);
                    }
                },colorPicker);
    }

    private void createMapMarkers(){
        int height = 25;
        int width = 25;
        listmarker.clear();
        mMap.clear();
        Log.e("Markers","count marker === " + listLoc.size());

        double lastLat = 0.0;
        double lastLng = 0.0;
        for (int i=0;i<listLoc.size();i++) {
            MarkerOptions m = new MarkerOptions();
            double lat = listLoc.get(i).getLat();
            double lng = listLoc.get(i).getLon();
            if (lat-lastLat==0 && lng-lastLng==0){
                lat = Utils.truncateDecimal(lat + 0.0002f);
                lng = Utils.truncateDecimal(lng + 0.0002f);
            }
            lastLat = lat;
            lastLng = lng;
            Log.e("Markers","lat === " + lat + " lng === " + lng);
            final Marker marker;
            LatLng latlon = new LatLng(lat, lng);
            if (listLoc.get(i).getStatus().equalsIgnoreCase("paid")) {
                numTxt.setTextColor(getResources().getColor(R.color.green));
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.greenpoint1);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                marker = mMap.addMarker(m.position(latlon)//.snippet( listLoc.get(i).getAmt()).title( listLoc.get(i).getStatus())//);
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                marker.setTag(i);
            }else{
                numTxt.setTextColor(getResources().getColor(R.color.red));
                Bitmap smallMarker = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(smallMarker);
                int color = PreferenceConnector.readInteger(mContext,PreferenceConnector.KEY_UNPAID_PIN_COLOR,R.color.red2);
                Drawable shape = Utils.changeDrawableColor(mContext,R.drawable.unpaid_pin,color);
                shape.setBounds(0, 0, smallMarker.getWidth(), smallMarker.getHeight());
                shape.draw(canvas);
                marker = mMap.addMarker(m.position(latlon)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                marker.setTag(i);
            }

            Locationbean lb=new Locationbean();
            lb.setLat(listLoc.get(i).getLat());
            lb.setLon(listLoc.get(i).getLon());
            lb.setMarker(marker);
            listmarker.add(lb);
        }

        if (listLoc.size()>0){
            LatLng coordinate = new LatLng(listLoc.get(0).getLat(), listLoc.get(0).getLon()); //Store these lat lng values somewhere. These should be constant.
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    coordinate, mMap.getCameraPosition().zoom);
            mMap.animateCamera(location);
        }
    }


    public void showListPopUpDialog(View viewAnchor, int viewWidth, final ListPopUpListWindowClickListener listener){
        if (mSpinnerAdapter!=null){
            final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);
            listPopupWindow.setAdapter(mSpinnerAdapter);
            listPopupWindow.setAnchorView(viewAnchor);
            listPopupWindow.setWidth(viewWidth);

            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    listener.onItemClicked(position);
                    listPopupWindow.dismiss();
                }
            });

            listPopupWindow.show();
        }
    }

    private void setUpSpinnerAdapter(){
        mPropertyTypeArrayList.add(new DropDownListDataModel("RESIDENTIAL","Residential",true));
        mPropertyTypeArrayList.add(new DropDownListDataModel("CENTRAL GOVERNMENT","Central Government"));
        mPropertyTypeArrayList.add(new DropDownListDataModel("STATE GOVERNMENT","State Government"));
        mPropertyTypeArrayList.add(new DropDownListDataModel("OPEN PLOT","Open Plot"));
        mPropertyTypeArrayList.add(new DropDownListDataModel("MIXED","Mixed"));
        mPropertyTypeArrayList.add(new DropDownListDataModel("NON-RESIDENTIAL","Non Residential"));
        mPropertyTypeArrayList.add(new DropDownListDataModel("ALL","All"));

        mSpinnerAdapter = new ListDropDownAdapter(
                mContext,
                R.layout.activity_gmap,
                R.id.lbl_spinner_item,
                mPropertyTypeArrayList);
    }

    private void clearLastSelectionDocument(ArrayList<DropDownListDataModel> listDataModelArrayList){
        for (int i = 0; i < listDataModelArrayList.size(); i++) {
            if (listDataModelArrayList.get(i).isSelected()){
                listDataModelArrayList.get(i).setSelected(false);
                break;
            }
        }
    }


    public void callFilterApi() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        apiUrlFilter = "http://114.79.182.179:8080/APIcalll/getDataForGIS1?wardNo="+strWardNo+"&TYPE="+strPropertyType+"&MIN="+strFrom+"&MAX="+strTo;
        Log.e("GoogleMapAnalytics","apiUrlFilter === " + apiUrlFilter);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                apiUrlFilter,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("--->", response.toString());
                        pDialog.hide();
                        if (response.length() == 0) {
                            Toast.makeText(mContext, "Property not found", Toast.LENGTH_SHORT).show();//value
                        } else {
                            addNewData(response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("--->>", "Error: " + error.getMessage());
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();//value
                pDialog.hide();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void callClearFilterApi(){
        String url = "http://114.79.182.179:8080/APIcalll/getDataForGIS?wardNo=" + strWardNo;
        Log.e("-url-->", url);

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("--->", response.toString());
                        pDialog.hide();
                        if (response.length() == 0) {
                            Toast.makeText(mContext, "Property not found", Toast.LENGTH_SHORT).show();//value
                        } else {
                            addNewData(response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("--->>", "Error: " + error.getMessage());
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();//value
                pDialog.hide();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void addNewData(String jsonResponse){
        jdata = jsonResponse;
        try {
            j = new JSONArray(jdata);
            setmapdata(filter);
            createMapMarkers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isUngroupPointViewEnabled () {
        return PreferenceConnector.readBoolean(mContext,PreferenceConnector.KEY_UNGROUP_POINT_VIEW,false);
    }

    public void setUngroupPointViewEnabled (boolean flag) {
        PreferenceConnector.writeBoolean(mContext,PreferenceConnector.KEY_UNGROUP_POINT_VIEW,flag);
    }

    private void checkIfDuplicate(){
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < listLoc.size(); i++) {
            for (int j = i + 1 ; j < listLoc.size(); j++) {
                if (listLoc.get(i).getLat() == listLoc.get(j).getLat() && listLoc.get(i).getLon() == listLoc.get(j).getLon()) {
                    // got the duplicate element
                    Log.e("Analytics","j === "+j);
                    integerList.add(j);
                    /*double lat = listLoc.get(j).getLat();
                    double lng = listLoc.get(j).getLon();
                    listLoc.get(j).setLat(Utils.truncateDecimal(lat+0.0002f));
                    listLoc.get(j).setLon(Utils.truncateDecimal(lng+0.0002f));*/
                }
            }
        }

        for (Integer position:integerList) {
            listLoc.remove((int)position);
        }

        for (Integer position:integerList) {
            double lat = mPropertyLatLngArrayList.get(position).getLat();
            double lng = mPropertyLatLngArrayList.get(position).getLon();
            mPropertyLatLngArrayList.get(position).setLat(Utils.truncateDecimal(lat+0.0002f));
            mPropertyLatLngArrayList.get(position).setLon(Utils.truncateDecimal(lng+0.0002f));
            listLoc.add(position,mPropertyLatLngArrayList.get(position));
        }
    }

}