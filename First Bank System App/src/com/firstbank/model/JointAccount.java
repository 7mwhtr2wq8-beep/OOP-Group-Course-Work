// JointAccount.java
package com.firstbank.model;

public class JointAccount extends Account {
    @Override public double minimumDeposit() { return 100_000; }
    @Override public String accountTypeName() { return "Joint"; }
}