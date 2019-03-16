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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerLoginActivity extends AppCompatActivity {

    EditText email,password;
    Button btn_login;
    Button btn_newAccount;
    View view;
    FirebaseAuth mAuth;
    ProgressDialog pDialog;
    String currentuid;
    DatabaseReference customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        view = findViewById(R.id.customer_login_container);
        btn_newAccount = (Button) findViewById(R.id.customer_btn_create);
        email = (EditText) findViewById(R.id.customer_login_email);
        password = (EditText) findViewById(R.id.customer_login_password);
        btn_login = (Button) findViewById(R.id.customer_btn_login);

        mAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(this);
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLoginActivity.this,CustomerSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignin();

            }
        });
    }

    private void startSignin() {

        String mail = email.getText().toString();
        String pass = password.getText().toString();
        pDialog.setTitle("Signing In");
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
            Snackbar.make(view, "Please provide all details", Snackbar.LENGTH_LONG).show();
            pDialog.dismiss();
        }
        else{
            mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            pDialog.dismiss();

                            currentuid = mAuth.getCurrentUser().getUid();
                            customerRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(currentuid))
                                    {
                                        Intent intent = new Intent(CustomerLoginActivity.this,CustomerHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(CustomerLoginActivity.this, "No Such Customer found", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }); }
                        else{
                            Snackbar.make(view, "Please verify your email address", Snackbar.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }
                    }
                    else{
                        pDialog.dismiss();
                        Snackbar.make(view, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}
