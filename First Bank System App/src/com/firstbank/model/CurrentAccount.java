// CurrentAccount.java
package com.firstbank.model;

public class CurrentAccount extends Account {
    @Override public double minimumDeposit() { return 200_000; }
    @Override public String accountTypeName() { return "Current"; }
}