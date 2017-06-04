package com.google.sample.cloudvision;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.testing.http.apache.MockHttpClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        Intent intent = getIntent();
        String FilePath = intent.getStringExtra("file");
        Uri file = Uri.fromFile(new File(FilePath));



        StorageReference tempImg = storageRef.child("temp/");
        UploadTask up = tempImg.putFile(file);
      up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
         System.out.println("Succesful");
              @SuppressWarnings("VisibleForTests")

             Uri a = taskSnapshot.getDownloadUrl();

          }
      });
        /*RequestQueue queue = Volley.newRequestQueue(this);
        String URL = " https://derekcardscanner.herokuapp.com/pic";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{rl.stop();}catch (Error e){


                }
                setContentView(R.layout.activity_analyze);
                String a[] = response.split(",");
                String Email="";
                for(int i=0;i<a.length;i++){
                    if(a[i].contains("@")){
                        Email  =a[i];
                        System.out.println(a[i]+": is an email. ");
                    }else{
                        System.out.println(a[i]+": is not an email. :(");
                    }

                }

                TextView res = (TextView) findViewById(R.id.Response);
                res.setText("Email: "+ Email);

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
    */
}
}
