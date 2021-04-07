package com.prbansal.firebasepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.prbansal.firebasepractice.databinding.ActivitySummaryMainBinding;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SummaryMainActivity extends AppCompatActivity implements Serializable {
    ActivitySummaryMainBinding binding;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySummaryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        application = (MyApplication) getApplicationContext();
        totalSalesSummary();
        totalPurchaseSummary();
        setupCustomBtn();
      /*  makeDummyCustomers();*/

    }

    private void makeDummyCustomers() {
        /*ArrayList<Customer> customers = new ArrayList<>();
        Customer newCustomer1 = new Customer("priyanshu","9328561813","silvassa","",
                3000,10000);
        Customer newCustomer2 = new Customer("swapnil","8949828208","jaipur","",
                3000,10000);

        newCustomer1.txnMap.put(Timestamp.now().,2000);
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCustomer1.txnMap.put(String.valueOf(Calendar.getInstance().getTimeInMillis()),-500);
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCustomer2.txnMap.put(String.valueOf(Calendar.getInstance().getTimeInMillis()),5000);
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCustomer2.txnMap.put(String.valueOf(Calendar.getInstance().getTimeInMillis()),-1000);
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Customer customer : customers) {
                    application.database.collection("sales").document(customer.mobileNo)
                            .set(customer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SummaryMainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SummaryMainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        customers.add(newCustomer1);
        customers.add(newCustomer2);*/

        }

    private void setupCustomBtn() {
        binding.customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryMainActivity.this, SummaryQueryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void totalPurchaseSummary() {
        application.database.collection("summary").document("purchase").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        TotalPurchaseSummary totalPurchaseSummary = documentSnapshot.toObject(TotalPurchaseSummary.class);
                        binding.totalAmtPurchase.setText("Rs. " + totalPurchaseSummary.totalPurchase+"/-");
                        binding.totalAmtDuePurchase.setText("Rs. " + totalPurchaseSummary.totalPurchaseDue +"/-");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SummaryMainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void totalSalesSummary() {
       application.database.collection("summary").document("sales").get()
               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                     TotalSaleSummary totalSaleSummary = documentSnapshot.toObject(TotalSaleSummary.class);
                     binding.totalAmtSales.setText("Rs. " + totalSaleSummary.totalSale+"/-");
                     binding.totalAmtDueSales.setText("Rs. " + totalSaleSummary.totalSalesDue +"/-");
                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(SummaryMainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
           }
       });
    }

}