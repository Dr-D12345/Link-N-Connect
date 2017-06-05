package com.google.sample.cloudvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.security.spec.ECField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AnalyzeActivity extends AppCompatActivity {
private RotateLoading rl;
    public String FilePath;
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
        FilePath = intent.getStringExtra("file");
        final Uri file = Uri.fromFile(new File(FilePath));





        StorageReference tempImg = storageRef.child("temp/");
        UploadTask up = tempImg.putFile(file);
      up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
         System.out.println("Succesful");
              @SuppressWarnings("VisibleForTests")

             Uri a = taskSnapshot.getDownloadUrl();
              String url="";
              try{
              url = "https://derekcardscanner.herokuapp.com/pic/" + URLEncoder.encode(a.toString(), "UTF-8");
              }catch (Exception e){

                  System.out.println(e);
              }
                  System.out.println("URL ENCODED: " + url);
                  final RequestQueue queue = Volley.newRequestQueue(AnalyzeActivity.this);

                  StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          try{rl.stop();}catch (Error e){
                          }
                          setContentView(R.layout.activity_analyze);

                          response.replaceAll("\"\']","");

                          String res[] = new String[]{};
                          res =response.split(",");
                          String whole =res[0];
                          Toast.makeText(AnalyzeActivity.this,whole,Toast.LENGTH_LONG).show();
                          System.out.println("Whole: "  +whole);
                          String Email="Not Found";
                          String emailkeys[]= {"gmail","hotmail","outlook","inbox","@"};
                          for(int i=0;i<res.length;i++) {
                              for (int l = 0; l < emailkeys.length; l++) {
                                  if (res[i].contains(emailkeys[l]) ) {
                                      Email = res[i];
                                      System.out.println(res[i] + ": is an email. ");
                                  } else {
                                      System.out.println(res[i] + ": is not an email. :(");
                                  }
                              }
                          }
                        Result(res, Email);

                      queue.stop();
                      }
                  }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          System.out.println("Server Returned an error: " + error.networkResponse.statusCode+ " data : "+error.networkResponse.data+" headers: "+ error.networkResponse.headers);
                      }
                  }
                  );


                  queue.add(stringRequest);

            System.out.println(a.toString());
          }
      });
        up.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
        up.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful())
                System.out.println("Success!");


            }
        });
    }
    void Result(String a[], String Email){

        Bitmap myBitmap = BitmapFactory.decodeFile(FilePath);
        Button contacts = (Button) findViewById(R.id.addtocontacts);
        Button emailBack = (Button) findViewById(R.id.emailback);


        String test[] = a[0].split("\n");
        ImageView img =(ImageView) findViewById(R.id.image);

        img.setImageBitmap(myBitmap);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,test);

        final AutoCompleteTextView EmailInput = (AutoCompleteTextView) findViewById(R.id.EMAIL);
        final AutoCompleteTextView FNAMEInput = (AutoCompleteTextView) findViewById(R.id.FNAME);
        final AutoCompleteTextView LNAMEInput = (AutoCompleteTextView) findViewById(R.id.FNAME);
        EmailInput.setText("derekjonp@gmail.com");
        FNAMEInput.setText("Derek");
        FNAMEInput.setText("Pastor");

        EmailInput.setThreshold(1);
        EmailInput.setAdapter(adapter);
        EmailInput.setText(Email);
        FNAMEInput.setThreshold(1);
        FNAMEInput.setAdapter(adapter);
        LNAMEInput.setThreshold(1);
        LNAMEInput.setAdapter(adapter);

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);

                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME,FNAMEInput.getText().toString()  + " " + LNAMEInput.getText().toString() );
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL,EmailInput.getText().toString());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        emailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,  new String[] {EmailInput.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT,"So nice metting you!");
                intent.putExtra(Intent.EXTRA_TEXT, "Thank you so much for taking the time to talk yesterday. It was a pleasure to learn even more about \n [Your Company]\n and how you approach [Industry] with innovative \n [strategy]. I’m very excited about the opportunity to explore a potential career with the \n[specific team].");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

}

