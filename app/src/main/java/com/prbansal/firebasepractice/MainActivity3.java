package com.prbansal.firebasepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.prbansal.firebasepractice.databinding.ActivityMain3Binding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    ActivityMain3Binding activityMain3Binding;
    List<String> buildingNameList = new ArrayList<>();
    List<String> buildingABVList = new ArrayList<>();
    List<String> floorNumberList = new ArrayList<>();
    List<String> floorNameList = new ArrayList<>();
    String setText = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain3Binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(activityMain3Binding.getRoot());
        getSupportActionBar().setTitle("Campus Creator");

        activityMain3Binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildingNameList = new ArrayList<>();
                buildingABVList = new ArrayList<>();
                floorNumberList = new ArrayList<>();
                floorNameList = new ArrayList<>();
                if(!activityMain3Binding.schema.getText().toString().isEmpty()){
                    String str = activityMain3Binding.schema.getText().toString();
                    String[] main = str.split("\n");

                    for (int i = 0; i < main.length; i++){
                        if(i%2 == 0){
                            String[] name = main[i].split(" - ");
                            buildingNameList.add(name[0]);
                            buildingABVList.add(name[1]);
                        }
                        else {
                            floorNameList.add(main[i]);
                            String[] number = main[i].split(",");
                            floorNumberList.add(number.length +"");
                        }
                    }
                    for(int i =0; i <= buildingNameList.size(); i++){
                        if(i == buildingNameList.size()){
                            activityMain3Binding.textView11.setText(setText);
                        }
                        else
                        setText += "BUILDING " + (i+1) + " : " + buildingNameList.get(i) + " - ABV : " + buildingABVList.get(i) + "\nNo of Floors : "
                                + floorNumberList.get(i) + " - Floors : " + floorNameList.get(i) + "\n";
                    }

                }

            }
        });
    }
}