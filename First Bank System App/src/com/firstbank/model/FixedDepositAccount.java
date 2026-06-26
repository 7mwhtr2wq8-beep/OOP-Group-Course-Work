// FixedDepositAccount.java
package com.firstbank.model;

public class FixedDepositAccount extends Account {
    @Override public double minimumDeposit() { return 1_000_000; }
    @Override public String accountTypeName() { return "Fixed Deposit"; }
}