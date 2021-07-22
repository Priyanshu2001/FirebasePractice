package com.prbansal.firebasepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, Integer> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Accounts accounts = new Accounts("hello", 10);
        Accounts accounts2 = new Accounts("bye", 80);
        map.put(accounts.name, accounts.balance);
        map.put(accounts2.name, accounts2.balance);

        OnlyMap onlyMap = new OnlyMap(map);

        db.collection("Try Map").document("testing").set(onlyMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity4.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("Try Map").document("testing").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(MainActivity4.this, documentSnapshot.hashCode() +"", Toast.LENGTH_SHORT).show();
            }
        });


        String timeSettings = android.provider.Settings.System.getString(
                this.getContentResolver(),
                Settings.Global.AUTO_TIME);
        if(timeSettings.contentEquals("1")){
            Toast.makeText(this, "All Ok", Toast.LENGTH_SHORT).show();
        }
    }


}