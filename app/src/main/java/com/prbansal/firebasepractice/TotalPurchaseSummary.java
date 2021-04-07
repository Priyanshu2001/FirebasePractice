package com.prbansal.firebasepractice;

import java.io.Serializable;

public class TotalPurchaseSummary implements Serializable {
    public int totalPurchase;
    public int totalPurchaseDue;

    public TotalPurchaseSummary() {
    }

    public TotalPurchaseSummary(int totalPurchase, int totalPurchaseDue) {
        this.totalPurchase = totalPurchase;
        this.totalPurchaseDue = totalPurchaseDue;
    }
}
