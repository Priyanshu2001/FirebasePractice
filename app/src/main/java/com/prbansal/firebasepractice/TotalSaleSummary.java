package com.prbansal.firebasepractice;

import java.io.Serializable;

public class TotalSaleSummary implements Serializable{
    public int totalSalesDue;
    public int totalSale;

    public TotalSaleSummary(int totalSale, int totalSalesDue) {
        this.totalSale = totalSale;
        this.totalSalesDue = totalSalesDue;
    }

    public TotalSaleSummary() {
    }
}
