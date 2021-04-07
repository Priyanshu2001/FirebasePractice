package com.prbansal.firebasepractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;

public class TransactionsActicity extends AppCompatActivity {
    Employee emp1 = new Employee("A",20000,false);
    Employee emp2 = new Employee("B",50000,true);
    Employee emp3 = new Employee("C",40000,true);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_acticity);
        emp1.id= getIntOfBool(emp1.isActive) + emp1.empName+ emp1.salary ;
        emp2.id= getIntOfBool(emp2.isActive) + emp2.empName+ emp2.salary ;
        emp3.id= getIntOfBool(emp3.isActive) + emp3.empName+ emp3.salary ;

        // saveAcc();
        // txn(emp3.id,emp2.id,emp1.salary);
        // queries();
        // realtimeListener();
        paginate();

    }

    private void paginate() {
        Task<QuerySnapshot> queryFirst = db.collection("Employee").orderBy("salary", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("queryFirst", documentSnapshot.getId() + " " + documentSnapshot.get("salary"));
                }
                DocumentSnapshot lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() -1);
                Task<QuerySnapshot> queryNext = db.collection("Employee").orderBy("salary", Query.Direction.DESCENDING).startAfter(lastVisible).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.e("queryNext", documentSnapshot.getId() + " " + documentSnapshot.get("salary"));
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void realtimeListener() {
        DocumentReference docRef = db.collection("Employee").document(emp2.id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Realtime Listener", "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Log.e("Realtime Listener", "Current data: " + value.getData());
                } else {
                    Log.e("Realtime Listener", "Current data: null");
                }
            }
        });
    }

    private void queries() {
        CollectionReference collectionReference = db.collection("Employee");
        Task<QuerySnapshot> query1 = collectionReference.whereEqualTo("empName","A").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("query1", documentSnapshot.getId() + " " + documentSnapshot.get("empName"));
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
        Task<QuerySnapshot> queryAndOrder = db.collection("Employee").orderBy("salary", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("queryAndOrder", documentSnapshot.getId() + " " + documentSnapshot.get("salary"));
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
        Task<QuerySnapshot> query2 = collectionReference.whereNotEqualTo("isActive",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("query2", documentSnapshot.getId() + " " + documentSnapshot.get("isActive"));
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
        Task<QuerySnapshot> query3 = collectionReference.whereLessThan("salary",25000).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("query3", documentSnapshot.getId() + " " + documentSnapshot.get("salary"));
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
        Task<QuerySnapshot> query4 = collectionReference.whereGreaterThan("salary",20000).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("query4", documentSnapshot.getId() + " " + documentSnapshot.get("salary"));
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void txn(String from, String To, int txnAmt) {
        db.runTransaction(new Transaction.Function<Integer>() {
            @Nullable
            @Override
            public Integer apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Accounts fromAccOf = transaction.get(db.collection("accounts").document(from)).toObject(Accounts.class);
                if (fromAccOf.balance < txnAmt)
                    return -1;
                transaction.update(db.collection("accounts").document(from), "balance", FieldValue.increment(-txnAmt));
                transaction.update(db.collection("accounts").document(To), "balance", FieldValue.increment(txnAmt));
                return fromAccOf.balance - txnAmt;
            }
        })  .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        if(integer==-1){
                            new AlertDialog.Builder(TransactionsActicity.this)
                                    .setTitle("Insufficient balance")
                                    .setMessage("The from account has insufficient balance.")
                                    .show();
                        }
                        else
                            Toast.makeText(TransactionsActicity.this, "Done", Toast.LENGTH_SHORT).show();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TransactionsActicity.this, "Failure!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int getIntOfBool(Boolean bool) {
        return bool ? 1:0;
    }
    private void saveAcc() {
        WriteBatch batch = db.batch();

        batch.set(db.collection("Employee").document(emp1.id), emp1);
        batch.set(db.collection("Employee").document(emp2.id), emp2);
        batch.set(db.collection("Employee").document(emp3.id), emp3);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TransactionsActicity.this, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TransactionsActicity.this, "Failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}