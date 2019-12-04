package com.example.onlineparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Form extends AppCompatActivity {
    EditText firstname,lastname,username,email,password;
    Button btn_register;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);
        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        btn_register=(Button)findViewById(R.id.btnregister);
        progressDialog=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth=FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sfirstname=firstname.getText().toString().trim();
                final String slastname=lastname.getText().toString().trim();
                final String suername=username.getText().toString().trim();
                final String semail=email.getText().toString().trim();
                String spassword=password.getText().toString().trim();
                if(TextUtils.isEmpty(sfirstname)){
                    firstname.setError("First Name can't be Empty");

                    return;
                }
                if(TextUtils.isEmpty(slastname)){
                    lastname.setError("Last Name can't be empty");

                    return;
                }
                if(TextUtils.isEmpty(suername)){
                    username.setError("Username can't be Empty");

                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches()){
                    email.setError("Enter valid email");
               return;
                }
                if(TextUtils.isEmpty(spassword)){
                    password.setError("Password can't be empty");
                    return;
                }
                if(spassword.length()<8){
                    password.setError("Enter at Least 8 character");

                    return;
                }
                progressDialog.setMessage("Registering...");
                progressDialog.show();


                firebaseAuth.createUserWithEmailAndPassword(semail, spassword)
                        .addOnCompleteListener(Signup_Form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User information=new User(
                                            sfirstname,
                                            slastname,
                                           suername,
                                            semail

                                    );
                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Signup_Form.this,"User Registration Successfull",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(),Login_Form.class));
                                            finish();

                                        }
                                    });

                                } else {
                                    Toast.makeText(Signup_Form.this,"User Registration Failed!!! Try using different Email ",Toast.LENGTH_LONG).show();
                                    progressDialog.cancel();
                                    finish();



                                }

                                // ...
                            }
                        });

            }
        });


    }

    public void Register(View view) {
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check=!task.getResult().getSignInMethods().isEmpty();
                        if(!check)
                        {
                            Toast.makeText(getApplicationContext(),"Registered",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Signup_Form.this,"Email Id already Exist",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void btn_Signup(View view) {
        startActivity(new Intent(getApplicationContext(),Login_Form.class));
        finish();
    }
}
