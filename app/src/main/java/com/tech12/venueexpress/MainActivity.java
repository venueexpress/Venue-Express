package com.tech12.venueexpress;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button customer, serviceProvider;
    FirebaseAuth mAuth;
    String currentuid;
    DatabaseReference customerRef;
    DatabaseReference spRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        spRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser() != null && firebaseUser.isEmailVerified()){
            currentuid = mAuth.getCurrentUser().getUid();
            customerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(currentuid))
                    {
                        Intent intent = new Intent(MainActivity.this,CustomerHomeActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            spRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(currentuid))
                    {
                        Intent intent = new Intent(MainActivity.this,SPHomeActivity.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        customer = (Button) findViewById(R.id.customer);
        serviceProvider = (Button) findViewById(R.id.serviceProvider);
        
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,CustomerLoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        serviceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,SPLoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
