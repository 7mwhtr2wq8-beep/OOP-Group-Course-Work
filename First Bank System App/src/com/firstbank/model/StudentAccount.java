// StudentAccount.java
package com.firstbank.model;

public class StudentAccount extends Account {
    @Override public double minimumDeposit() { return 10_000; }
    @Override public String accountTypeName() { return "Student"; }
}