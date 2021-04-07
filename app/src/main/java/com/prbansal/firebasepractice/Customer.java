package com.prbansal.firebasepractice;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.HashMap;

public class Customer implements Serializable {
    public String name, mobileNo, address, remark;
    public Timestamp timestamp;
    public int dueAmount, totalAmount;
    public HashMap<String,Integer> txnMap = new HashMap<>();

    public Customer() {
    }

    public Customer(String name, String mobileNo, String address, String remark, int dueAmount, int totalAmount) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.address = address;
        this.remark = remark;
        this.dueAmount = dueAmount;
        this.totalAmount = totalAmount;
        timestamp = Timestamp.now();
    }
}
