package com.example.onlineparking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends FragmentActivity implements OnMapReadyCallback {
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth firebaseAuth;
    private static final int Request_Code=101;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth=FirebaseAuth.getInstance();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        GetlastLocation();


        progressDialog=new ProgressDialog(this);

    }


    private void GetlastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_Code);
            return;
        }
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){
            if(location !=null){
                mlocation=location;
               Toast.makeText(getApplication(),mlocation.getLatitude()+""+mlocation.getLongitude(),Toast.LENGTH_SHORT).show();
                SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(Dashboard.this);
            }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,6));
        googleMap.addMarker(markerOptions);

        LatLng patan = new LatLng(27.679, 85.321);
        googleMap.addMarker(new MarkerOptions().position(patan)
                .title("Parking_A").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.marke)));


        LatLng labim = new LatLng(27.677, 85.316);
        googleMap.addMarker(new MarkerOptions().position(labim)
                .title("parking_B").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.marke)));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_Code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    GetlastLocation();
                }
                break;
        }
    }


    public void btn_logout(View view) {
        firebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login_Form.class));
        finish();
        progressDialog.setMessage("SigningOut");
        progressDialog.show();

    }

    public void ParkingStatus(View view) {
       startActivity(new Intent(getApplicationContext(),parkingStatus.class));
    }
}
//main activity
//map implement
//logout buuton
//markers custom,current location