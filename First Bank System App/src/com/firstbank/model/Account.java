package com.firstbank.model;

import java.time.LocalDate;

public abstract class Account {

    private String accountNumber;
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phoneNumber;
    private String pin;
    private LocalDate dateOfBirth;
    private String branch;
    private double openingDeposit;

    public abstract double minimumDeposit();
    public abstract String accountTypeName();

    // Account Number
    public String getAccountNumber()         { return accountNumber; }
    public void setAccountNumber(String v)   { this.accountNumber = v; }

    // Names
    public String getFirstName()             { return firstName; }
    public void setFirstName(String v)       { this.firstName = v == null ? null : v.trim(); }
    public String getLastName()              { return lastName; }
    public void setLastName(String v)        { this.lastName = v == null ? null : v.trim(); }
    public String getFullName()              { return firstName + " " + lastName; }

    // NIN
    public String getNin()                   { return nin; }
    public void setNin(String v)             { this.nin = v == null ? null : v.trim().toUpperCase(); }

    // Contact
    public String getEmail()                 { return email; }
    public void setEmail(String v)           { this.email = v == null ? null : v.trim(); }
    public String getPhoneNumber()           { return phoneNumber; }
    public void setPhoneNumber(String v)     { this.phoneNumber = v == null ? null : v.trim(); }

    // PIN
    public String getPin()                   { return pin; }
    public void setPin(String v)             { this.pin = v; }

    // DOB
    public LocalDate getDateOfBirth()        { return dateOfBirth; }
    public void setDateOfBirth(LocalDate v)  { this.dateOfBirth = v; }

    // Branch
    public String getBranch()                { return branch; }
    public void setBranch(String v)          { this.branch = v; }

    // Deposit
    public double getOpeningDeposit()        { return openingDeposit; }
    public void setOpeningDeposit(double v)  { this.openingDeposit = v; }
}