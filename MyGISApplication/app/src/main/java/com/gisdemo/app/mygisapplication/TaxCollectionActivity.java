package com.gisdemo.app.mygisapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TaxCollectionActivity extends Activity {
    JSONObject Jobj=null;
    String pid="";
    LinearLayout ll,llcheque;
    String mode="";
    RadioGroup radioGroup;
    RadioButton cash,cheque,upi;
    Button b;
    TextView tvamt;
    EditText etAmt,etChequeNo,etContact;
    String loginid="";
    String tot="";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.taxcollection);
        mContext = this;

        ll=findViewById(R.id.upiview);
        ll.setVisibility(View.GONE);

        llcheque=findViewById(R.id.llcheque);
        llcheque.setVisibility(View.GONE);

        tvamt=findViewById(R.id.tv_amt);
        etAmt=findViewById(R.id.edt_amt);
        etChequeNo=findViewById(R.id.edt_chkno);
        etContact=findViewById(R.id.edt_contact);
        b=findViewById(R.id.bsubmit);


        SharedPreferences prefs = getSharedPreferences("AppData", MODE_PRIVATE);
        String restoredText = prefs.getString("userid", null);
        if (restoredText != null) {
            loginid= prefs.getString("userid", "");//"No name defined" is the default value.
        }


       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("json");
            tot = extras.getString("tot");
            //The key argument here must match that used in the other activity
            tvamt.setText(tot);
            try {
                Jobj=new JSONObject(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(TaxDetails.this,""+value,Toast.LENGTH_SHORT).show();//value
        }
*/

        radioGroup = (RadioGroup) findViewById(R.id.rgroup);
        cash = (RadioButton) findViewById(R.id.radiocash);
        cheque = (RadioButton) findViewById(R.id.radiocheque);
        upi = (RadioButton) findViewById(R.id.radioupi);
        cash.setChecked(true);
        mode="CASH";
//        textView = (TextView) findViewById(R.id.text);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radiocash) {
                    ll.setVisibility(View.GONE);
                    llcheque.setVisibility(View.GONE);
                    mode="CASH";
                } else if(checkedId == R.id.radiocheque) {
                    ll.setVisibility(View.GONE);
                    llcheque.setVisibility(View.VISIBLE);
                    mode="CHEQUE";
                } else if(checkedId == R.id.radioupi) {
                    ll.setVisibility(View.VISIBLE);
                    llcheque.setVisibility(View.GONE);
                    mode="UPI";
                }
            }

        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name="";
                String amount = etAmt.getText().toString().trim();
                String mobile = etContact.getText().toString().trim();
                if (amount.isEmpty()){
                    Toast.makeText(TaxCollectionActivity.this,"Enter Amount",Toast.LENGTH_LONG).show();
                }else if (mobile.isEmpty()){
                    Toast.makeText(TaxCollectionActivity.this,"Enter Your Mobile Number",Toast.LENGTH_LONG).show();
                }else if (mobile.length()<10){
                    Toast.makeText(TaxCollectionActivity.this,"Enter a Valid 10 Digit Mobile Number",Toast.LENGTH_LONG).show();
                }else {
                    if (Jobj != null) {
                        try {
                            JSONObject jPropDetail = Jobj.getJSONObject("PropDetail");
                            pid = "" + jPropDetail.optString("INDEX_NUMBER");
                            name = "" + jPropDetail.optString("OWNERSNAME");

                            JSONObject j = new JSONObject();
                            j.put("username", "" + loginid);
//                            j.put("contact",""+etContact.getText().toString());
                            if (etChequeNo.getText().toString().length() != 0)
                                j.put("chequeNo", "" + etChequeNo.getText().toString());
                            else
                                j.put("chequeNo", "null");

                            j.put("chequeDate", "");

                            j.put("amount", Integer.parseInt(etAmt.getText().toString()));  // int

                            j.put("payMode", "" + mode);
                            j.put("bankId", 10);  //int
                            j.put("branch", "DHANTOLI");
                            j.put("propertyId", "" + pid);
                            j.put("paidBy", "" + name);

                            callCollectAPI(j);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }

    public void callCollectAPI(JSONObject j) {

//        String url = "http://114.79.182.179:8080/APIcalll/PenaltyDetails?index_number=";
        String url ="http://114.79.182.178:9180/ptis/rest/receipt";
        Log.e("-url-->", url);
        Log.d("-json-->", j.toString());
        Log.e("-json-->", j.toString());
        final ProgressDialog pDialog1 = new ProgressDialog(TaxCollectionActivity.this);
        pDialog1.setMessage("Loading...");
        pDialog1.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, j,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("-respo-->", response.toString());
                        Log.e("Payment","response --> " + response.toString());
                        System.out.println("response --> " + response.toString());
                        pDialog1.hide();
//                        {"status":"SUCCESS","receiptnumber":"12\/2019-20\/0258442"}

                        if (response.optString("status").equalsIgnoreCase("SUCCESS")) {
                            try {
                                String receiptNumber = response.getString("receiptnumber");
                                String amount = etAmt.getText().toString().trim();
                                sendSms(amount,receiptNumber);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showPaymentSuccessDialog();
                            }
                        }else{
                            Toast.makeText(TaxCollectionActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();//value
                        }
                        /*try{
                            String penalty=response.optString("penaltyamount");
                            int pen=Integer.parseInt(penalty.toString());
                        }catch(NumberFormatException e){e.printStackTrace();}*/
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("--->>", "Error: " + error.getMessage());
                Toast.makeText(TaxCollectionActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();//value
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

    private void showPaymentSuccessDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(TaxCollectionActivity.this).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Payment Successful");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TaxCollectionActivity.this.finish();
                    }
                });
        alertDialog.show();
    }

    private void sendSms(String amount,String receiptNumber){
        if (Jobj != null) {
            try {
                JSONObject jPropDetail  =    Jobj.getJSONObject("PropDetail");
                String indexNo          =   jPropDetail.getString("INDEX_NUMBER");
                String zone             =   jPropDetail.getString("ZONE");
                String ward             =   jPropDetail.getString("WARD");
                setSMSText(amount,receiptNumber,indexNo,zone,ward);
            } catch (JSONException e) {
                e.printStackTrace();
                showPaymentSuccessDialog();
            }
        }
    }

    private void setSMSText(String amount, String receiptNumber, String indexNo, String zone, String ward){
        String consumerCode = indexNo + "(" + "Zone:" + zone + "%20Ward:" + ward + ")";
        try {
            String encodedUrl = URLEncoder.encode("http://114.79.182.178:9180/collection/citizen/onlineReceipt!viewReceipt.action?" +
                    "receiptNumber=" + receiptNumber +
                    "&consumerCode=" + consumerCode +
                    "&serviceCode=PT","UTF-8");
            System.out.println("encodedUrl ---> " + encodedUrl);

            String smsText = "Dear Citizen tax with amount " + amount + " have been paid successfully please go through following link to download or print receipt \n\n" +
                    encodedUrl;

            System.out.println("sms text ---> " + smsText);
            String mobileNo = etContact.getText().toString();
            System.out.println("mobileNo ---> " + mobileNo);
            callSmsApi(mobileNo,smsText);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showPaymentSuccessDialog();
        }
    }

    private void callSmsApi(String mobileNumber,String message){
        String url = "http://weberleads.in/http-tokenkeyapi.php?" +
                "authentic-key=33304d41525354454c45434f4d3938371563947925" +
                "&senderid=NMCGOV" +
                "&route=2" +
                "&number=" + mobileNumber +
                "&message=" + message;
        Log.e("Analytics"," url--> " + url);

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
                        Log.e("Payment","response --> " + response.toString());
                        pDialog.hide();
                        showPaymentSuccessDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("--->>", "Error: " + error.getMessage());
                Log.e("Error","VolleyError == " + error.getMessage());
                pDialog.hide();
                showPaymentSuccessDialog();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "");
    }
}
