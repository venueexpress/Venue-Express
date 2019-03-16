package com.tech12.venueexpress;

import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SPHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);

        mAuth = FirebaseAuth.getInstance();
        String id =mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers").child(id).child("Venue");

        recyclerView =(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Venue,VenueViewHolder> firebaseRecyclerAdapter;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Venue, VenueViewHolder>(
                Venue.class,
                R.layout.show_venue,
                VenueViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(VenueViewHolder viewHolder, Venue model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setCapacity(model.getCapacity());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder
    {

        View mview;
        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;

        }
        public void setName(String n)
        {
            TextView name = (TextView)mview.findViewById(R.id.sname);
            name.setText(n);

        }
        public void setCapacity(String c)
        {
            TextView capacity = (TextView) mview.findViewById(R.id.scapacity);
            capacity.setText(c);

        }
        public void setPrice(String p)
        {
            TextView price = (TextView) mview.findViewById(R.id.sprice);
            price.setText(p);

        }
        public  void setImage(final Context cxt, final String img)
        {
            final ImageView image= (ImageView)mview.findViewById(R.id.simage);

            Picasso.with(cxt).load(img).into(image);

        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add)
        {
            startActivity(new Intent(SPHomeActivity.this, AddVenue.class));
            return true;
        }
        if(item.getItemId()==R.id.time)
        {
            startActivity(new Intent(SPHomeActivity.this, AddDateTime.class));
            return  true;
        }
        if(item.getItemId()==R.id.menu)
        {
            startActivity(new Intent(SPHomeActivity.this, MapsActivity.class));
            return  true;
        }
        if(item.getItemId()==R.id.logout)

        {
            mAuth.signOut();
            startActivity(new Intent(SPHomeActivity.this, SPLoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
