package com.tech12.venueexpress;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddDateTime extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener

{
        Button b_pick;
    private ProgressDialog progressDialog;
        int day,month,year,hour,minute;
        int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_time);
        b_pick=(Button) findViewById(R.id.b_pick);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers").child(id).child("DateTime");

        b_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(AddDateTime.this,AddDateTime.this,year,month,day);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal=year;
        monthFinal=month+1;
        dayFinal=dayOfMonth;
        Calendar c=Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,this,hour,minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal=hourOfDay;
        minuteFinal=minute;
        progressDialog.setMessage("Adding Date and Time..");
        progressDialog.show();
        mDatabase.child("Year").setValue(yearFinal);
        mDatabase.child("Month").setValue(monthFinal);
        mDatabase.child("Hour").setValue(hourFinal);
        mDatabase.child("Minute").setValue(minuteFinal);
        mDatabase.child("Day").setValue(dayFinal);


        progressDialog.dismiss();
        onBackPressed();

    }
}
