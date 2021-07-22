package com.prbansal.firebasepractice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prbansal.firebasepractice.databinding.ActivityMain2Binding;
import com.prbansal.firebasepractice.databinding.AddFloorBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Completable;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.internal.util.ObserverSubscriber;
import rx.schedulers.Schedulers;

public class MainActivity2 extends AppCompatActivity {

   ActivityMain2Binding binding;
   MyApplication application;
   int count;
   int floorSize = 6;
   HashMap<Integer, RadioButton> floorBindingMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        application =  (MyApplication) getApplicationContext();
        //binding.searchView.setIconifiedByDefault(false);
        binding.floorCount.setText(floorSize + "");
        int searchBarId = binding.searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        LinearLayout searchBar = (LinearLayout) binding.searchView.findViewById(searchBarId);

        searchBar.setLayoutTransition(new LayoutTransition());
        binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    binding.imageView7.animate().translationXBy(binding.imageView7.getRight());
                    binding.imageView8.animate().translationXBy(-binding.imageView8.getLeft());
                   // binding.imageView8.animate().alpha(1.0f);
                    binding.imageView7.setVisibility(View.VISIBLE);
                    binding.imageView8.setVisibility(View.VISIBLE);
                }
                else {
                    binding.imageView7.animate().translationXBy(-binding.imageView7.getRight());
                    binding.imageView8.animate().translationXBy(binding.imageView8.getLeft());
                    //binding.imageView8.animate().alpha(0.0f);
                    binding.imageView7.setVisibility(View.GONE);
                    binding.imageView8.setVisibility(View.GONE);
                }
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0 || newText.isEmpty() ){
                  //  binding.searchView.setQuery("", false);
                    binding.searchView.clearFocus();
                }
                return false;
            }
        });
        int searchCloseButtonId = binding.searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView clearButton = (ImageView) binding.searchView.findViewById(searchCloseButtonId);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchView.setQuery("", false);
                binding.searchView.clearFocus();
            }
        });
       // setupFloorSelector();
        setupRadioFloorSelector();
        //usingRxJava();
        firebaseGetUser();
        firebaseGetAppOpens();
        downloadImage();
        application.showLoadingDialog(MainActivity2.this);

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.floorCount.getText().toString().trim().isEmpty()) {
                    floorSize = Integer.parseInt(binding.floorCount.getText().toString().trim());
                    setupRadioFloorSelector();
                }
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }

    private void setupRadioFloorSelector() {
        binding.floorGrp2.removeAllViews();
        for(int i = 0; i < floorSize; i++){
            AddFloorBinding floorBinding = AddFloorBinding.inflate(getLayoutInflater());
            floorBindingMap.put(i,floorBinding.radioButton4);
            floorBinding.radioButton4.setText(floorSize -1 - i + "");
            floorBinding.radioButton4.setId(i);
            Log.e("button id :", floorBinding.radioButton4.getId() + "");
            if(i==0){
                floorBinding.radioButton4.setBackgroundResource(R.drawable.floor_selector_top_bg);
            }
            if (i == floorSize -1){
                floorBinding.radioButton4.setBackgroundResource(R.drawable.selected_floor_bottom_bg);
                floorBinding.radioButton4.setText("GF");
                floorBinding.radioButton4.setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.white));
            }
            binding.floorGrp2.addView(floorBinding.getRoot(),i);
        }
        binding.floorGrp2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 0){
                    floorBindingMap.get(checkedId).setBackgroundResource(R.drawable.selected_floor_top_bg);
                }
                else if (checkedId == floorSize -1){
                    floorBindingMap.get(checkedId).setBackgroundResource(R.drawable.selected_floor_bottom_bg);
                }
                else{
                    floorBindingMap.get(checkedId).setBackgroundResource(R.drawable.selected_floor_bg);
                }
                floorBindingMap.get(checkedId).setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.white));
                deselectRest(checkedId);
            }
        });

        binding.floorGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        selectFloor(binding.radioButton1, binding.radioButton4, binding.radioButton5);
                        break;
                    case R.id.radioButton4:
                        selectFloor(binding.radioButton4, binding.radioButton1, binding.radioButton5);
                        break;
                    case R.id.radioButton5:
                        selectFloor(binding.radioButton5, binding.radioButton4, binding.radioButton1);
                        break;
                }
            }
        });
    }

    private void deselectRest(int checkedId) {
        HashMap<Integer, RadioButton> deselectThese = new HashMap<>();
        for (Map.Entry<Integer, RadioButton> radioButton : floorBindingMap.entrySet()){
            deselectThese.put(radioButton.getKey(), radioButton.getValue());
        }
        deselectThese.remove(checkedId);
        if(checkedId == 0){
            floorBindingMap.get(floorSize - 1).setBackgroundResource(R.drawable.floor_selector_bottom_bg);
            floorBindingMap.get(floorSize - 1).setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
            deselectThese.remove(floorSize -1);

        }
        else if(checkedId == floorSize -1){
            floorBindingMap.get(0).setBackgroundResource(R.drawable.floor_selector_top_bg);
            floorBindingMap.get(0).setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
            deselectThese.remove(0);
        }
        else {
            floorBindingMap.get(floorSize - 1).setBackgroundResource(R.drawable.floor_selector_bottom_bg);
            floorBindingMap.get(floorSize - 1).setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
            floorBindingMap.get(0).setBackgroundResource(R.drawable.floor_selector_top_bg);
            floorBindingMap.get(0).setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
            deselectThese.remove(floorSize -1);
            deselectThese.remove(0);
        }

        for (Map.Entry<Integer, RadioButton> radioButton : deselectThese.entrySet()){
            radioButton.getValue().setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
            radioButton.getValue().setBackgroundColor(ContextCompat.getColor(MainActivity2.this,R.color.white));
        }
    }

    private void selectFloor(RadioButton radioButton, RadioButton radioButton1, RadioButton radioButton2) {
        radioButton.setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.white));
        radioButton.setBackgroundResource(R.drawable.selected_floor_bg);

        deselectFloor(radioButton1);
        deselectFloor(radioButton2);

    }
    private void deselectFloor(RadioButton radioButton) {
        radioButton.setTextColor(ContextCompat.getColor(MainActivity2.this,R.color.black));
        radioButton.setBackgroundColor(ContextCompat.getColor(MainActivity2.this,R.color.white));
    }

    public void firebaseGetUser(){
        application.database.collection("stats").document("MyUsers").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String users = documentSnapshot.getString("users");
                        binding.loader2.setVisibility(View.GONE);
                        binding.statsUser.setText(users);
                        binding.statsUser.setVisibility(View.VISIBLE);
                        binding.userStatsImage.setVisibility(View.VISIBLE);
                        binding.userStatsTxt.setVisibility(View.VISIBLE);
                        afterCallback();
                    }
                });
    }

    public void firebaseGetAppOpens(){
        Task<DocumentSnapshot> documentSnapshot = application.database.collection("stats").document("AppOpens").get();
        application.database.collection("stats").document("AppOpens").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String users = documentSnapshot.getString("opens");
                        binding.loader1.setVisibility(View.GONE);
                        binding.appOpens.setText(users);
                        binding.appOpens.setVisibility(View.VISIBLE);
                        binding.imageAppOpens.setVisibility(View.VISIBLE);
                        binding.appOpensTxt.setVisibility(View.VISIBLE);
                        afterCallback();
                    }
                });
    }

    public void downloadImage(){
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("bg.png");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               // String url = uri.toString();
                Glide.with(MainActivity2.this).load(uri).into(binding.imageView12);
                afterCallback();
            }
        });
    }

    public void usingRxJava(){

        Completable completable1 = Completable.fromAction(this::firebaseGetAppOpens).observeOn(Schedulers.newThread());
        Completable completable2 = Completable.fromAction(this::firebaseGetUser).observeOn(Schedulers.newThread());
        Completable completable3 = Completable.fromAction(this::downloadImage).observeOn(Schedulers.newThread());

/*        Completable.merge(completable1, completable2, completable3).observeOn(AndroidSchedulers.mainThread()).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                application.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });*/

        Observable<Task<DocumentSnapshot>> observable1 = Observable.just(application.database.collection("stats").document("AppOpens").get()).subscribeOn(Schedulers.newThread());
        Observable<Task<DocumentSnapshot>> observable2 = Observable.just(application.database.collection("stats").document("MyUsers").get()).subscribeOn(Schedulers.newThread());
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("bg.png");
         Observable<Task<Uri>> observable3 = Observable.just(reference.getDownloadUrl()).subscribeOn(Schedulers.newThread());
       /* Observable.merge(observable1 , observable2, observable3).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Task<? extends Object>>() {
            @Override
            public void onCompleted() {
                application.hideLoadingDialog();
                new AlertDialog.Builder(MainActivity2.this).setMessage("ALL Complete").show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Task<?> task) {
                task.addOnSuccessListener(new OnSuccessListener<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(MainActivity2.this, "task Completed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });*/
      /* Observable.merge(observable1 , observable2).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Task<DocumentSnapshot>>() {
           @Override
           public void onCompleted() {


           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(Task<DocumentSnapshot> documentSnapshotTask) {

           }
       });*/
    }
    public synchronized void afterCallback(){
        if(++count ==3){
            Toast.makeText(MainActivity2.this, "All Complete", Toast.LENGTH_SHORT).show();
            application.hideLoadingDialog();
        }
    }
}