package com.google.sample.cloudvision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.testing.http.apache.MockHttpClient;
import com.victor.loading.rotate.RotateLoading;


public class AnalyzeActivity extends AppCompatActivity {
private RotateLoading rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        try{
        rl = (RotateLoading) findViewById(R.id.rotateloading);
        rl.start();
        }catch(Error e){

        }
        /*send pic to database */
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = " https://derekcardscanner.herokuapp.com/pic";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            System.out.println(response);
                try{rl.stop();}catch (Error e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }
    );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);

    }
}
