// SavingsAccount.java
package com.firstbank.model;

public class SavingsAccount extends Account {
    @Override public double minimumDeposit() { return 50_000; }
    @Override public String accountTypeName() { return "Savings"; }
}