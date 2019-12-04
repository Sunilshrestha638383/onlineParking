package com.example.onlineparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Form extends AppCompatActivity {
    EditText email,password;
    Button btn_Signup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        btn_Signup=(Button) findViewById(R.id.btn_login);
        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String semail=email.getText().toString().trim();
                String spassword=password.getText().toString().trim();

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
                progressDialog.setMessage("Signing In");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(semail, spassword)
                        .addOnCompleteListener(Login_Form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                    finish();

                                } else {
                                    Toast.makeText(Login_Form.this,"Login Failed!! Check Email and Password",Toast.LENGTH_LONG).show();
                                    progressDialog.cancel();


                                }

                                // ...
                            }
                        });
            }
        });
    }

    public void btn_Signup(View view) {
        startActivity(new Intent(getApplicationContext(),Signup_Form.class));
        finish();




    }

    public void bnt_admin(View view) {
        startActivity(new Intent(getApplicationContext(),Map_Data.class));
        finish();
    }


//    public void Admin(View view) {
//        startActivity(new Intent(getApplicationContext(),Map_Data.class));
//        finish();
//
//
//    }
}
