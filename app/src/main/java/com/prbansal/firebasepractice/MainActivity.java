package com.prbansal.firebasepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prbansal.firebasepractice.databinding.ActivityMainBinding;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
FirebaseFirestore db = FirebaseFirestore.getInstance();
    Employee newEmpDetails = new Employee("ABC",50000,true);
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        newEmpDetails.id = "ABCFT01";
        //binding.searchView.setIconifiedByDefault(false);
      //  runTxn();


//        addData();
//        setData();
//        setCustomObject();
//        setOBject();
//        getData();
//        updateDocument();
//        getCustomObject();
//        getObject();
//        deleteFeild();
//        deleteDoc();
         // batchWrites();
    }

    private void runTxn() {
        db.runTransaction(new Transaction.Function<Employee[]>() {
            @Override
            public Employee[] apply(Transaction transaction) throws FirebaseFirestoreException {
                StorageReference reference = FirebaseStorage.getInstance().getReference().child("Screenshot (1).png");
                final DocumentReference sfDocRef = db.collection("Employee").document("0A20000");
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                Employee employee = snapshot.toObject(Employee.class);

                final DocumentReference sfDocRef1 = db.collection("Employee").document("1B50000");
                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                DocumentSnapshot snapshot2 = transaction.get(sfDocRef);
                Employee employee2 = snapshot2.toObject(Employee.class);
                Employee[] array = new Employee[2];
                array[0] = employee;
                array[1] = employee2;

                // Success
                return array;
            }
        }).addOnSuccessListener(new OnSuccessListener<Employee[]>() {
            @Override
            public void onSuccess(Employee[] array) {
                Log.e("status", "Transaction success!" + "\n" + array[0].toString() + "\n" + array[1].toString());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("failed", "Transaction failure.", e);
                    }
                });
    }

    private void batchWrites() {
        WriteBatch batch = db.batch();
        DocumentReference docRef = db.collection("Employee New Data").document(newEmpDetails.id);
        batch.set(docRef,newEmpDetails);
        batch.update(docRef,"salary",60000);
        batch.delete(docRef);
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "writes Done", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteFeild() {
        db.collection("Employee Data").document("id")
                .update("salary", FieldValue.delete());
    }

    private void deleteDoc() {
        db.collection("Employee Data").document("id")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Doc deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCustomObject() {
        db.collection("Employee Data").document("id")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Toast.makeText(MainActivity.this, "Object:"+ documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to get Object", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setOBject() {

        db.collection("Employee New Data").document(newEmpDetails.id)
                .set(newEmpDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Object added to doc", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to add Object", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getObject() {
        db.collection("Employee New Data").document(newEmpDetails.id)
                .get()
              .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                      Toast.makeText(MainActivity.this, "Object:"+ documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                  }
              })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to get Object", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getData() {
        db.collection("Data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(MainActivity.this
                                        , "my Data Added on : " + document.getId() + "Data is :" + document.getData()
                                        , Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Failure to add data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateDocument() {
        db.collection("Data").document("MY data")
                .update("city","Silvassa","country","INDIA");
    }

    private void setCustomObject() {
        Employee empDetails = new Employee("XYZ",50000,true);

        db.collection("Employee Data").document("id")
                .set(empDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Object added to doc", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to add Object", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setData() {
        Map<String,Object> map = new HashMap<>();
        map.put("name", "Priyanshu");
        map.put("age", 19);
        map.put("country", "india");

        db.collection("Data").document("MY data")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data added to doc", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to add data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addData() {
        Map<String,Object> map = new HashMap<>();
        map.put("name", "Priyanshu");
        map.put("age", 19);
        map.put("country", "india");

        db.collection("myData")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "my Data Added on : "+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure to add data", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}