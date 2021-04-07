package com.prbansal.firebasepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prbansal.firebasepractice.databinding.ActivitySummaryQueryBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SummaryQueryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivitySummaryQueryBinding activitySummaryQueryBinding;
    long fromDate;
    long toDate;
    long sinceDate;
    long quickQuery;
    MyApplication application;
    int saleAmt;
    int saleDueAmt;
    int purchaseAmt;
    int purchaseDueAmt;
    String sales ="sales";
    String purchase = "purchase";
    ArrayList<Customer> salesCustomers = new ArrayList<>();
    ArrayList<Customer> purchaseCustomers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySummaryQueryBinding = ActivitySummaryQueryBinding.inflate(getLayoutInflater());
        setContentView(activitySummaryQueryBinding.getRoot());
        application = (MyApplication) getApplicationContext();
        setQueryDetails();
        setupSpinner();
        setSalesCustomerList();
        setPurchaseCustomerList();
        activitySummaryQueryBinding.summaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataOfQuery();
            }
        });

    }

    private void setSalesCustomerList() {
        application.database.collection("sales").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Customer customer = document.toObject(Customer.class);
                        salesCustomers.add(customer);
                    }
                }
                else {
                    Toast.makeText(SummaryQueryActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setPurchaseCustomerList() {
        application.database.collection("purchase").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Customer customer = document.toObject(Customer.class);
                        purchaseCustomers.add(customer);
                    }
                }
                else {
                    Toast.makeText(SummaryQueryActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Calendar setCalender() {
        Calendar myCalender= Calendar.getInstance();
        myCalender.set(Calendar.MILLISECOND, 0);
        myCalender.set(Calendar.SECOND, 0);
        myCalender.set(Calendar.MINUTE, 0);
        myCalender.set(Calendar.HOUR_OF_DAY,0);
        return myCalender;
    }

    private void purchase(long date) {
        for (Customer customer : purchaseCustomers){
                        for (Map.Entry<String,Integer> entry : customer.txnMap.entrySet()){
                            Long timestamp = Long.parseLong(entry.getKey());
                            if(timestamp>=date);
                            {
                                if (entry.getValue()>=0){
                                    purchaseAmt+=entry.getValue();
                                }
                                purchaseDueAmt+=entry.getValue();
                            }
                        }
                    }
                    activitySummaryQueryBinding.amtPurchase.setText("Rs. " + purchaseAmt + "/-");
                    activitySummaryQueryBinding.amtDuePurchase.setText("Rs. " + purchaseDueAmt + "/-");
                }

    private void sales(long date) {
        for (Customer customer : salesCustomers){
                        for (Map.Entry<String,Integer> entry : customer.txnMap.entrySet()){
                            Long timestamp = Long.parseLong(entry.getKey());
                            if(timestamp>=date);
                            {
                                if (entry.getValue()>=0){
                                    saleAmt+=entry.getValue();
                                }
                                saleDueAmt+=entry.getValue();
                            }
                        }
                    }
                    activitySummaryQueryBinding.amtSales.setText("Rs. " + saleAmt + "/-");
                    activitySummaryQueryBinding.amtDueSales.setText("Rs. " + saleDueAmt + "/-");
                }


    private void purchaseBtw() {
       for (Customer customer : purchaseCustomers){
                        for (Map.Entry<String,Integer> entry : customer.txnMap.entrySet()){
                            Long timestamp = Long.parseLong(entry.getKey());
                            if(timestamp>=fromDate && timestamp<= toDate);
                            {
                                if (entry.getValue()>=0){
                                    purchaseAmt+=entry.getValue();
                                }
                                purchaseDueAmt+=entry.getValue();
                            }
                        }
                    }
                    activitySummaryQueryBinding.amtPurchase.setText("Rs. " + purchaseAmt + "/-");
                    activitySummaryQueryBinding.amtDuePurchase.setText("Rs. " + purchaseDueAmt + "/-");
                }


    private void salesBtw() {

        application.database.collection("sales").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Customer customer = document.toObject(Customer.class);
                        for (Map.Entry<String,Integer> entry : customer.txnMap.entrySet()){
                            Long timestamp = Long.parseLong(entry.getKey());
                            if(timestamp>=fromDate && timestamp<= toDate);
                            {
                                if (entry.getValue()>=0){
                                    saleAmt+=entry.getValue();
                                }
                                saleDueAmt+=entry.getValue();
                            }
                        }
                    }

                }
                else {
                    Toast.makeText(SummaryQueryActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setQueryDetails() {
      activitySummaryQueryBinding.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Calendar mCalender = Calendar.getInstance();
                int year = mCalender.get(Calendar.YEAR);
                int month = mCalender.get(Calendar.MONTH);
                int dayOfMonth = mCalender.get(Calendar.DAY_OF_MONTH);
                switch (checkedId){
                    case R.id.btwDates_btn:
                        activitySummaryQueryBinding.betweenDatesGrp.setVisibility(View.VISIBLE);
                        activitySummaryQueryBinding.spinner.setVisibility(View.GONE);
                        activitySummaryQueryBinding.sinceGrp.setVisibility(View.GONE);
            ;
                        activitySummaryQueryBinding.fromCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(SummaryQueryActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        Calendar mCalender = setCalender();
                                        mCalender.set(Calendar.YEAR, year);
                                        mCalender.set(Calendar.MONTH, month);
                                        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        fromDate = mCalender.getTimeInMillis();
                                        String selectedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(mCalender.getTime());
                                        activitySummaryQueryBinding.fromDate.setText(selectedDate);
                                    }
                                }, year, month, dayOfMonth).show();
                            }
                        });
                        activitySummaryQueryBinding.toCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(SummaryQueryActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        Calendar mCalender = setCalender();
                                        mCalender.set(Calendar.YEAR, year);
                                        mCalender.set(Calendar.MONTH, month);
                                        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        toDate =mCalender.getTimeInMillis();
                                        Log.e(" okk ",toDate+"");
                                        String selectedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(mCalender.getTime());
                                        activitySummaryQueryBinding.toDate.setText(selectedDate);
                                    }
                                }, year, month, dayOfMonth).show();
                            }
                        });
                       break;
                    case R.id.since_btn:
                        activitySummaryQueryBinding.sinceGrp.setVisibility(View.VISIBLE);
                        activitySummaryQueryBinding.betweenDatesGrp.setVisibility(View.GONE);
                        activitySummaryQueryBinding.spinner.setVisibility(View.GONE);
                        activitySummaryQueryBinding.sincedateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(SummaryQueryActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        Calendar mCalender = setCalender();
                                        mCalender.set(Calendar.YEAR, year);
                                        mCalender.set(Calendar.MONTH, month);
                                        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        sinceDate =mCalender.getTimeInMillis();
                                        String selectedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(mCalender.getTime());
                                        activitySummaryQueryBinding.sinceDateTxt.setText(selectedDate);
                                    }
                                }, year, month, dayOfMonth).show();
                            }
                        });
                        break;
                    case R.id.quick_Btn:
                        activitySummaryQueryBinding.spinner.setVisibility(View.VISIBLE);
                        activitySummaryQueryBinding.betweenDatesGrp.setVisibility(View.GONE);
                        activitySummaryQueryBinding.sinceGrp.setVisibility(View.GONE);
                        break;
                }
                }
        });


    }

    private void setupSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("Yesterday");
        categories.add("This Week");
        categories.add("This Month");
        categories.add("This Year");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.summary_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }
    private void getDataOfQuery(){
        int checkedIdOfTimeQuery= activitySummaryQueryBinding.radioGroup2.getCheckedRadioButtonId();
        int checkedIdOfTypeQuery = activitySummaryQueryBinding.radioGroup.getCheckedRadioButtonId();
        switch (checkedIdOfTimeQuery){

            case R.id.btwDates_btn:
                     if(checkedIdOfTypeQuery == R.id.sales_btn){
                         salesBtw();
                     }
                     else if(checkedIdOfTypeQuery == R.id.purchase_btn){
                         purchaseBtw();
                     }
                     else {
                         salesBtw();
                         purchaseBtw();
                     }
                     break;
            case R.id.since_btn:

                if(checkedIdOfTypeQuery == R.id.sales_btn){
                    sales(sinceDate);
                }
                else if(checkedIdOfTypeQuery == R.id.purchase_btn){
                    purchase(sinceDate);
                }
                else {
                    sales(sinceDate);
                    purchase(sinceDate);
                }
                break;

            case R.id.quick_Btn:
                 if(checkedIdOfTypeQuery == R.id.quick_Btn){
                   sales(quickQuery);
                }
                else if(checkedIdOfTypeQuery == R.id.purchase_btn){
                   purchase(quickQuery);
                }
                else {
                   sales(quickQuery);
                  purchase(quickQuery);
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        switch (position){
            case 0:
                Calendar today= setCalender();
                quickQuery = today.getTimeInMillis();
                Log.e("ye hai",quickQuery+"");
                break;

            case 1:
                Calendar yesterday = setCalender();
                yesterday.add(Calendar.DATE,-1);
                quickQuery=yesterday.getTimeInMillis();
                Log.e("ye hai",quickQuery+"");
                break;

            case 2:
                Calendar week = setCalender();
                week.set(Calendar.DAY_OF_WEEK,2);
                quickQuery=week.getTimeInMillis();
                Log.e("ye hai",quickQuery+"");
                break;
            case 3:
                Calendar month = setCalender();
                month.set(Calendar.DAY_OF_MONTH,1);
                Log.e("ye hai",quickQuery+"");
                break;
            case 4:
                Calendar year = setCalender();
                year.set(Calendar.DAY_OF_YEAR,1);
                quickQuery=year.getTimeInMillis();
                Log.e("ye hai",quickQuery+"");
                break;

        }
        Toast.makeText(parent.getContext(), "Selected: " + item + "id="+id + "position = " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    }