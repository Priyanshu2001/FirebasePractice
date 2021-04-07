package com.prbansal.firebasepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.prbansal.firebasepractice.databinding.ActivityTopicNotifBinding;

public class TopicNotifActivity extends AppCompatActivity {

    private ActivityTopicNotifBinding activityTopicNotifBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_notif);
         activityTopicNotifBinding = ActivityTopicNotifBinding.inflate(getLayoutInflater());
        setContentView(activityTopicNotifBinding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("agent");
        Toast.makeText(TopicNotifActivity.this, "Subscribed to Admin", Toast.LENGTH_SHORT).show();
        setupUserBtn();
        setupAdminBtn();

    }



    private void setupAdminBtn() {
        activityTopicNotifBinding.adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("admin");
                Toast.makeText(TopicNotifActivity.this, "Subscribed to Admin", Toast.LENGTH_SHORT).show();
                activityTopicNotifBinding.adminBtn.setVisibility(View.GONE);
                activityTopicNotifBinding.unSubAdminBtn.setVisibility(View.VISIBLE);
            }
        });
        activityTopicNotifBinding.unSubAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
                Toast.makeText(TopicNotifActivity.this, "UnSubscribed From Admin", Toast.LENGTH_SHORT).show();
                activityTopicNotifBinding.adminBtn.setVisibility(View.VISIBLE);
                activityTopicNotifBinding.unSubAdminBtn.setVisibility(View.GONE);
            }
        });

    }

    private void setupUserBtn() {
        activityTopicNotifBinding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("users");
                Toast.makeText(TopicNotifActivity.this, "Subscribed to User", Toast.LENGTH_SHORT).show();
                activityTopicNotifBinding.userBtn.setVisibility(View.GONE);
                activityTopicNotifBinding.unSubUserBtn.setVisibility(View.VISIBLE);
            }
        });
        activityTopicNotifBinding.unSubUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("users");
                Toast.makeText(TopicNotifActivity.this, "UnSubscribed From User", Toast.LENGTH_SHORT).show();
                activityTopicNotifBinding.userBtn.setVisibility(View.VISIBLE);
                activityTopicNotifBinding.unSubUserBtn.setVisibility(View.GONE);
            }
        });

    }
}