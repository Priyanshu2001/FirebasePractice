package com.prbansal.firebasepractice;

import java.io.Serializable;

public class Accounts implements Serializable {
    public  String name;
    public  int balance;

    public Accounts(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public Accounts() {
    }
}
