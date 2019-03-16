package com.tech12.venueexpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerSignUpActivity extends AppCompatActivity {

    EditText email,password,name;
    Button btn_register;
    ProgressDialog pDialog;
    Snackbar snackbar;
    View view;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_up);

        view = findViewById(R.id.customer_signUp_container);
        email = (EditText) findViewById(R.id.customer_signup_email);
        name= (EditText) findViewById(R.id.customer_signup_username);
        password = (EditText) findViewById(R.id.customer_signup_password);
        btn_register = (Button) findViewById(R.id.customer_btn_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignup();
            }

        });
    }

    private void startSignup() {
        final String mail = email.getText().toString();
        final String username = name.getText().toString();
        final String pass = password.getText().toString();
        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)){
            Snackbar.make(view, "Please provide all details", Snackbar.LENGTH_LONG).show();
        }
        else{
            pDialog = new ProgressDialog(this);
            pDialog.setTitle("Creating your account");
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    pDialog.dismiss();
                                    //Snackbar.make(view, "Please check your email for verification.", Snackbar.LENGTH_LONG).show();
                                    Toast.makeText(CustomerSignUpActivity.this, "Please check your email for verification", Toast.LENGTH_LONG).show();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUser = databaseReference.child(userId);
                                    currentUser.child("Email").setValue(mail);
                                    currentUser.child("Name").setValue(username);
                                    currentUser.child("Password").setValue(pass);

                                    Intent intent = new Intent(CustomerSignUpActivity.this,CustomerLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    pDialog.dismiss();
                                }
                            }
                        });

                    }
                    else{
                        Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }
            });
        }

    }

}
