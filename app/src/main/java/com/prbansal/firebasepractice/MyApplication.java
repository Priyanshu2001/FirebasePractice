package com.prbansal.firebasepractice;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prbansal.firebasepractice.databinding.BlockingLoaderBinding;
import com.prbansal.firebasepractice.databinding.CircularLoadingLottieBinding;
import com.prbansal.firebasepractice.databinding.NoInternetLoaderBinding;

import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class MyApplication extends Application {
    public FirebaseFirestore database;
    public Dialog dialog;
    Button retryBtn;
    LinearLayout offlineView;
    StorageReference firebaseStorage;
    MyApplication myApplication;
    private ConnectivityManager connectivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        assignVariables();

    }

    public void showLoadingDialog(Context context) {

        BlockingLoaderBinding binding = BlockingLoaderBinding.inflate(LayoutInflater.from(context));

        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(context, R.style.LoaderStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() == null)
            return;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showNoInternetDialog(Context context) {

        NoInternetLoaderBinding binding = NoInternetLoaderBinding.inflate(LayoutInflater.from(context));
        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(context, R.style.LoaderStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() == null)
            return;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    public void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void assignVariables() {
        database = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isOffline() {
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected());
    }


    public boolean isConnected() {
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected();
    }
}
