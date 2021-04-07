package com.prbansal.firebasepractice;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee implements Serializable {
    public String empName;
    public int salary;
    public boolean isActive;
    public String id;

    public Employee() {
    }

    public Employee(String empName,int salary, boolean isActive) {
        this.empName = empName;
        this.salary = salary;
        this.isActive = isActive;

    }

    @Override
    public String toString() {
        return "Employee{" +
                "empName='" + empName + '\'' +
                ", salary='" + salary + '\'' +
                ", isActive=" + isActive +
                ", id='" + id + '\'' +
                '}';
    }
}
