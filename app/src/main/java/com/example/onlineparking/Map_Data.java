package com.example.onlineparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Map_Data extends AppCompatActivity implements View.OnClickListener {
    //private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText map_Name;
    private EditText map_Latitude;
    private EditText map_Longitude;
    private Button btn_save;
    private Button btn_proceed;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__data);
        map_Name=(EditText)findViewById(R.id.Map_Name);
        map_Latitude=(EditText)findViewById(R.id.Map_Latitude);
        map_Longitude=(EditText)findViewById(R.id.Map_Longitude);
        btn_save=(Button) findViewById(R.id.btn_save);
        btn_proceed=(Button) findViewById(R.id.btn_proceed);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Map_Marker");
        progressDialog=new ProgressDialog(this);
        btn_save.setOnClickListener(this);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startActivity(new Intent(getApplicationContext(), Map.class));
           progressDialog.show();
           progressDialog.setMessage("Proceeding");
            }
        });
    }
    private void saveMarkers(){
        String name=map_Name.getText().toString().trim();
        double latitude = Double.parseDouble(map_Latitude.getText().toString().trim());
        double longitude = Double.parseDouble(map_Longitude.getText().toString().trim());
        Map_Markers map_markers=new Map_Markers(name,latitude,longitude);
        databaseReference.push().setValue(map_markers);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {
        if(view==btn_proceed) {
            finish();
        }
        if(view==btn_save){
            saveMarkers();
            map_Name.getText().clear();
            map_Latitude.getText().clear();
            map_Longitude.getText().clear();
            progressDialog.show();
            progressDialog.setMessage("Saving");
            progressDialog.cancel();


        }

    }
}
