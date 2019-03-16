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

public class SPLoginActivity extends AppCompatActivity {

    EditText email,password;
    Button btn_login;
    Button btn_newAccount;
    View view;
    FirebaseAuth mAuth;
    ProgressDialog pDialog;
    String currentuid;
    DatabaseReference spRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splogin);
        view = findViewById(R.id.sp_login_container);
        spRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers");

        btn_newAccount = (Button) findViewById(R.id.sp_btn_create);
        email = (EditText) findViewById(R.id.sp_login_email);
        password = (EditText) findViewById(R.id.sp_login_password);
        btn_login = (Button) findViewById(R.id.sp_btn_login);

        mAuth = FirebaseAuth.getInstance();

        pDialog = new ProgressDialog(this);
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPLoginActivity.this,SPSignUpActivity.class);
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

        final String mail = email.getText().toString();
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
                            spRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(currentuid))
                                    {
                                        Intent intent = new Intent(SPLoginActivity.this,SPHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(SPLoginActivity.this, "No Such Customer found", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
