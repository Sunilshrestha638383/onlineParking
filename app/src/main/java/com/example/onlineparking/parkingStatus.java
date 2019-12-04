package com.example.onlineparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class parkingStatus extends AppCompatActivity {
    private TextView textViewData;
    private Button btn_status;
    private static  final String FIRE_LOG="Fire_log";


    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_status);

        mFirestore=FirebaseFirestore.getInstance();
        textViewData=(TextView)findViewById(R.id.ViewStatus);
        btn_status=(Button)findViewById(R.id.showStatus);

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirestore.collection("guides").document("\n" +
                        "IO3p6JwVKQK8ZyM7LHjJ\n").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists() && documentSnapshot != null) {
                                String name = documentSnapshot.getString("content");
                                textViewData.setText(("ParkingStaus" + name));
                            }

                        } else {
                            Log.d(FIRE_LOG, "Error:" + task.getException().getMessage());
                        }
                    }
                });

            }
        });
    }


}
