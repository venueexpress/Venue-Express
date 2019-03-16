package com.tech12.venueexpress;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddVenue extends AppCompatActivity {

    private ImageView image;
    private EditText name;
    private EditText capacity,price;
    private Button button;
    private Uri imguri;
    private String link;
    private ProgressDialog progressDialog;

    private DatabaseReference mDatabase;
    private StorageReference storage;
    private FirebaseAuth mAuth;

    private static final int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);
        image = (ImageView)findViewById(R.id.image);
        name = (EditText)findViewById(R.id.name);
        capacity = (EditText)findViewById(R.id.capacity);
        price = (EditText)findViewById(R.id.price);
        button =(Button)findViewById(R.id.btn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers").child(id).child("Venue");
        storage = FirebaseStorage.getInstance().getReference().child("Venue");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery= new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String c = capacity.getText().toString();
                String p = price.getText().toString();
                if(n.equals("")||c.equals("")||p.equals(""))
                {
                    Toast.makeText(AddVenue.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Adding Venue..");
                progressDialog.show();
                final DatabaseReference key = mDatabase.push();
                key.child("Name").setValue(n);
                key.child("Capacity").setValue(c);
                key.child("Price").setValue(p);
                final StorageReference path = storage.child("Venue").child(System.currentTimeMillis() + ".jpg");
                path.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // link = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        link = taskSnapshot.getDownloadUrl().toString();
                        key.child("image").setValue(link);
                        progressDialog.dismiss();
                        onBackPressed();

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imguri = data.getData();
            image.setImageURI(imguri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
