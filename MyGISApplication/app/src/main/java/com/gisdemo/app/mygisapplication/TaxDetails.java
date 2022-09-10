package com.gisdemo.app.mygisapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaxDetails extends Activity {
    JSONObject Jobj=null;
    String pid="";
    TextView tvPenalty;
    int payble;
    TextView tvTotPAmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_tax_details);

        TextView tvName=(TextView) findViewById(R.id.tv_name);
        TextView tvIndex=(TextView) findViewById(R.id.tv_index);
        TextView tvHouseNumber=(TextView) findViewById(R.id.tv_housenumber);
        TextView tvType=(TextView) findViewById(R.id.tv_ptype);
        TextView tvDemand=(TextView) findViewById(R.id.tv_demand);
        TextView tvPaybleAmt=(TextView) findViewById(R.id.tv_payableamount);
        tvPenalty=(TextView) findViewById(R.id.tv_penaltyamount);
        tvTotPAmt=(TextView) findViewById(R.id.tv_totalpayableamount);

        TextView btassessment=(TextView) findViewById(R.id.btassessment);
        TextView btcal=(TextView) findViewById(R.id.btcal);
        TextView btdcb=(TextView) findViewById(R.id.btdcb);
        TextView tvpay=(TextView) findViewById(R.id.tvpay);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("json");
            //The key argument here must match that used in the other activity
            try {
                Jobj=new JSONObject(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(TaxDetails.this,""+value,Toast.LENGTH_SHORT).show();//value
        }

        btassessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://114.79.182.179:8080/ptis/Assesment_register_user.jsp?pid="+pid));
                startActivity(browserIntent);
            }
        });
        btcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://114.79.182.179:8080/ptis/New_CalSheet.jsp?pid="+pid));
                startActivity(browserIntent);
            }
        });
        btdcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://114.79.182.178:9180/ptis/view/viewDCBProperty!displayPropInfo.action?propertyId="+pid));
                startActivity(browserIntent);

            }
        });
        tvpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TaxDetails.this, TaxCollectionActivity.class);
//                i.putExtra("json", Jobj.toString());
//                i.putExtra("tot", tvTotPAmt.getText().toString());

                startActivity(i);
            }
        });


        if (Jobj!=null){
            try {

//                       "INDEX_NUMBER": "20021221",
//                        "ZONE": "5",
//                        "WARD": "20",
//                        "PART_NUMBER": "8",
//                        "OWNERSNAME": "SHAIKH HUSAIN ALIAS BABU MISTRI S/O ABDUL RASHID",
//                        "HOUSE_NUMBER": "3984/68",
//                        "PROPERTY_TYPE": "Residential",
//                        "CURRENT_DEMAND": "2280.0"


//                "penaltyamount": 32824,
//                        "payableamount": 29583,
//                        "totalpayableamount": 62407,


                /*JSONObject jPropDetail=Jobj.getJSONObject("");
                pid=""+jPropDetail.optString("INDEX_NUMBER");

                tvName.setText(""+jPropDetail.optString("OWNERSNAME"));
                tvIndex.setText(""+jPropDetail.optString("INDEX_NUMBER"));
                tvHouseNumber.setText(""+jPropDetail.optString("HOUSE_NUMBER"));
                tvDemand.setText(""+jPropDetail.optString("CURRENT_DEMAND"));
                tvType.setText(""+jPropDetail.optString("PROPERTY_TYPE"));

                tvPaybleAmt.setText(""+Jobj.optInt("payableamount"));
                payble=Integer.parseInt(""+Jobj.optInt("payableamount"));*/

                Log.d("ParseData"," 123 "+Jobj.get("OWNER_NAME"));

                tvName.setText(""+Jobj.get("OWNER_NAME").toString());
                tvIndex.setText(""+Jobj.get("INDEX_NUMBER").toString());

                pid=""+Jobj.get("INDEX_NUMBER").toString();

                tvHouseNumber.setText(""+Jobj.get("PROPERTY_ADDRESS").toString());
                tvDemand.setText(""+Jobj.get("total_payable_amount").toString());
                tvType.setText(""+Jobj.get("PROPERTY_TYPE").toString());

                tvPaybleAmt.setText(""+Jobj.get("total_payable_amount"));
                payble=Integer.parseInt(""+Jobj.get("total_payable_amount"));

                /*tvPenalty.setText(""+Jobj.optInt("penaltyamount"));
                tvTotPAmt.setText(""+Jobj.optInt("totalpayableamount"));*/
                tvPenalty.setText(""+Jobj.get("total_penalty"));
                tvTotPAmt.setText(""+Jobj.get("total_payable_amount"));


//                String indx=jPropDetail.optString("INDEX_NUMBER");

                try {
                    JSONArray jarr = Jobj.getJSONArray("All");



                    Log.d("DtaData  ","222"+jarr);

                    LinearLayout ll = (LinearLayout) findViewById(R.id.ll1);
                    for (int i = 0; i < jarr.length(); i++) {

                        JSONObject obj = jarr.getJSONObject(i);

//                    "total": {
//                            "totalDemand": "2223",
//                            "totAmountCollected": "0",
//                            "totRebate": "0",
//                            "totBalance": "2223"
//                        Jobj.get("");

                       // if (!obj.isNull("total")) {
                        //    JSONObject tot = obj.optJSONObject("total");

                            LayoutInflater inflater = LayoutInflater.from(TaxDetails.this);
                            View view1 = inflater.inflate(R.layout.llview, null);
//                    LinearLayout ll2=(LinearLayout) view1;
                            TextView tv1 = (TextView) view1.findViewById(R.id.textViedwyear);
                            TextView tv2 = (TextView) view1.findViewById(R.id.textView1);
                            TextView tv3 = (TextView) view1.findViewById(R.id.textView2);
                            TextView tv4 = (TextView) view1.findViewById(R.id.textView3);

                            tv1.setText("" + obj.get("year"));

                            tv2.setText("" + obj.get("demand"));
                            tv3.setText("" + obj.get("collection"));
                            tv4.setText("" + obj.get("balance"));

                            ll.addView(view1);
                     //   }
                    }
                }catch(Exception s){s.printStackTrace();}

                getpenalty(pid);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getpenalty(String index){

        String url = "http://114.79.182.179:8080/APIcalll/PenaltyDetails?index_number="+index;
        Log.d("-url-->", url);
        final ProgressDialog pDialog1 = new ProgressDialog(TaxDetails.this);
        pDialog1.setMessage("Loading...");
        pDialog1.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("-respo-->", response.toString());
                        pDialog1.hide();
//                    {"penaltyamount":"0"}

//04000140A001
//                        due=6762
//                            penalty=545-----------1005
//                    tot=7307-----------------------7767

                        try{
                            String penalty=response.optString("penaltyamount");

                            tvPenalty.setText(""+penalty);
                            int pen=Integer.parseInt(penalty.toString());
                            int tot=payble+pen;
//                        tvTotPAmt.setText(""+Jobj.optInt("totalpayableamount"));
                            tvTotPAmt.setText(""+tot);


                        }catch(NumberFormatException e){e.printStackTrace();}


//                    if (response.optString("Status").equalsIgnoreCase("success")) {
//
//                    }else{
//                        Toast.makeText(TaxDetails.this,"Invalid login details",Toast.LENGTH_SHORT).show();//value
//                    }
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
//                            Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
                pDialog1.hide();
            }
        });

// Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "");

    }
}




