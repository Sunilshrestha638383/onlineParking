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
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class Dashboard extends FragmentActivity implements OnMapReadyCallback {
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private static final int Request_Code = 101;
    private ProgressDialog progressDialog;
    List<MarkerModel> markerModels;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        GetlastLocation();


        progressDialog = new ProgressDialog(this);

    }


    private void GetlastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_Code);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlocation = location;
                    Toast.makeText(getApplication(), mlocation.getLatitude() + "" + mlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(Dashboard.this);
                }
            }
        });

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        CollectionReference pointsRef = rootRef.collection("markers");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
        googleMap.addMarker(markerOptions);

        pointsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    markerModels = Objects.requireNonNull(task.getResult()).toObjects(MarkerModel.class);
                    for (MarkerModel markerModel : markerModels) {
                        GeoPoint points = markerModel.getGeo();
                        double lat = points.getLatitude();
                        double lon = points.getLongitude();
                        LatLng newMarker = new LatLng(lat, lon);
                        googleMap.addMarker(new MarkerOptions().position(newMarker)
                                .title(markerModel.getParkingArea()).draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marke)));
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetlastLocation();
                }
                break;
        }
    }


    public void btn_logout(View view) {
        firebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login_Form.class));
        finish();
        progressDialog.setMessage("SigningOut");
        progressDialog.show();

    }

    public void ParkingStatus(View view) {
        startActivity(new Intent(getApplicationContext(), parkingStatus.class));
    }
}
//main activity
//map implement
//logout buuton
//markers custom,current location