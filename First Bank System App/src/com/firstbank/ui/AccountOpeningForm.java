package com.firstbank.ui;

import com.firstbank.model.*;
import com.firstbank.util.Validator;
import com.firstbank.db.DatabaseHelper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class AccountOpeningForm extends VBox {

    private final DatabaseHelper db;

    private TextField txtFirstName, txtLastName, txtNIN, txtSecondaryNIN;
    private TextField txtEmail, txtConfirmEmail, txtPhone, txtDeposit;
    private PasswordField txtPin, txtConfirmPin;
    private ComboBox<Integer> cmbYear, cmbDay;
    private ComboBox<String> cmbMonth, cmbAccountType, cmbBranch;
    private TextArea txtAccountSummary;
    private Label lblSecondaryNIN;

    public AccountOpeningForm(DatabaseHelper db) {
        this.db = db;
        setSpacing(10);
        setPadding(new Insets(15));
        setAlignment(Pos.TOP_CENTER);

        Label lblHeader = new Label("FIRST BANK UGANDA");
        lblHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #003366;");
        Label lblSubHeader = new Label("New Account Opening Form");
        lblSubHeader.setStyle("-fx-font-style: italic; -fx-padding: 0 0 10 0;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.TOP_CENTER);

        txtFirstName = new TextField(); txtLastName = new TextField();
        txtNIN = new TextField(); txtSecondaryNIN = new TextField();
        txtEmail = new TextField(); txtConfirmEmail = new TextField();
        txtPhone = new TextField(); txtPhone.setPromptText("+256XXXXXXXXX");
        txtPin = new PasswordField(); txtConfirmPin = new PasswordField();
        txtDeposit = new TextField();

        cmbBranch = new ComboBox<>();
        cmbBranch.getItems().addAll("Kampala", "Gulu", "Mbarara", "Jinja", "Mbale");
        cmbBranch.setMaxWidth(Double.MAX_VALUE);

        cmbAccountType = new ComboBox<>();
        cmbAccountType.getItems().addAll("Savings", "Current", "Fixed Deposit", "Student", "Joint");
        cmbAccountType.setMaxWidth(Double.MAX_VALUE);

        HBox dobBox = createDOBControls();

        grid.add(new Label("First Name:"),        0, 0); grid.add(txtFirstName,    1, 0);
        grid.add(new Label("Last Name:"),         2, 0); grid.add(txtLastName,     3, 0);
        grid.add(new Label("National ID (NIN):"), 0, 1); grid.add(txtNIN,          1, 1);
        grid.add(new Label("Date of Birth:"),     2, 1); grid.add(dobBox,          3, 1);
        grid.add(new Label("Email:"),             0, 2); grid.add(txtEmail,        1, 2);
        grid.add(new Label("Confirm Email:"),     2, 2); grid.add(txtConfirmEmail, 3, 2);
        grid.add(new Label("Phone Number:"),      0, 3); grid.add(txtPhone,        1, 3);
        grid.add(new Label("Branch:"),            2, 3); grid.add(cmbBranch,       3, 3);
        grid.add(new Label("PIN Code (4-6 d):"),  0, 4); grid.add(txtPin,          1, 4);
        grid.add(new Label("Confirm PIN:"),       2, 4); grid.add(txtConfirmPin,   3, 4);
        grid.add(new Label("Account Type:"),      0, 5); grid.add(cmbAccountType,  1, 5);
        grid.add(new Label("Opening Deposit:"),   2, 5); grid.add(txtDeposit,      3, 5);

        lblSecondaryNIN = new Label("Secondary NIN:");
        grid.add(lblSecondaryNIN, 0, 6);
        grid.add(txtSecondaryNIN, 1, 6);
        toggleSecondaryNINVisibility(false);

        cmbAccountType.getSelectionModel().selectedItemProperty().addListener(
            (obs, o, n) -> toggleSecondaryNINVisibility("Joint".equals(n)));

        Button btnSubmit = new Button("Submit Application");
        btnSubmit.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-weight: bold;");
        Button btnReset = new Button("Reset Form");

        HBox buttonBox = new HBox(15, btnSubmit, btnReset);
        buttonBox.setAlignment(Pos.CENTER);

        Label lblSummary = new Label("Account Summary Is Below:");
        lblSummary.setStyle("-fx-font-weight: bold;");
        txtAccountSummary = new TextArea();
        txtAccountSummary.setEditable(false);
        txtAccountSummary.setPrefHeight(100);

        btnReset.setOnAction(e -> clearFormFields());
        btnSubmit.setOnAction(e -> handleSubmission());

        getChildren().addAll(lblHeader, lblSubHeader, grid, buttonBox, lblSummary, txtAccountSummary);
    }

    private HBox createDOBControls() {
        cmbYear  = new ComboBox<>();
        cmbMonth = new ComboBox<>();
        cmbDay   = new ComboBox<>();

        for (int y = LocalDate.now().getYear(); y >= 1940; y--) cmbYear.getItems().add(y);
        cmbMonth.getItems().addAll("January","February","March","April","May",
                "June","July","August","September","October","November","December");

        cmbYear.setOnAction(e -> updateDays());
        cmbMonth.setOnAction(e -> updateDays());
        cmbYear.setPromptText("YY");
        cmbMonth.setPromptText("MM");
        cmbDay.setPromptText("DD");

        return new HBox(5, cmbYear, cmbMonth, cmbDay);
    }

    private void updateDays() {
        Integer y = cmbYear.getValue();
        String m  = cmbMonth.getValue();
        if (m == null) return;
        int year  = (y != null) ? y : 2001;
        int mIdx  = cmbMonth.getItems().indexOf(m) + 1;
        int days  = LocalDate.of(year, mIdx, 1).lengthOfMonth();
        Integer currDay = cmbDay.getValue();
        cmbDay.getItems().clear();
        for (int d = 1; d <= days; d++) cmbDay.getItems().add(d);
        if (currDay != null && currDay <= days) cmbDay.setValue(currDay);
    }

    private void toggleSecondaryNINVisibility(boolean v) {
        lblSecondaryNIN.setVisible(v); lblSecondaryNIN.setManaged(v);
        txtSecondaryNIN.setVisible(v); txtSecondaryNIN.setManaged(v);
    }

    private void handleSubmission() {
        String accountType = cmbAccountType.getValue();
        String branch      = cmbBranch.getValue();
        Integer year       = cmbYear.getValue();
        String month       = cmbMonth.getValue();
        Integer day        = cmbDay.getValue();

        if (accountType == null) { showError("Please select an Account Type."); return; }
        if (branch == null)      { showError("Please select a Branch.");        return; }
        if (year == null || month == null || day == null) { showError("Please select Date of Birth."); return; }

        double deposit;
        try {
            deposit = Double.parseDouble(txtDeposit.getText().trim());
        } catch (NumberFormatException e) {
            showError("Opening Deposit must be a valid number.");
            return;
        }

        // Create account subtype for minimum deposit check
        Account account = createAccount(accountType);

        String errorLog = Validator.validateInputs(
                txtFirstName.getText(), txtLastName.getText(),
                txtNIN.getText(), txtSecondaryNIN.getText(),
                txtEmail.getText(), txtConfirmEmail.getText(),
                txtPhone.getText(), txtPin.getText(), txtConfirmPin.getText(),
                year, month, day,
                accountType, branch,
                txtDeposit.getText());

        if (!errorLog.isEmpty()) {
            showError(errorLog);
            return;
        }

        // Build account
        String    f   = txtFirstName.getText().trim();
        String    l   = txtLastName.getText().trim();
        String    nin = txtNIN.getText().trim().toUpperCase();
        String    em  = txtEmail.getText().trim();
        String    ph  = txtPhone.getText().trim();
        String    pin = txtPin.getText();
        LocalDate dob = LocalDate.of(year, cmbMonth.getItems().indexOf(month) + 1, day);

        account.setFirstName(f);
        account.setLastName(l);
        account.setNin(nin);
        account.setEmail(em);
        account.setPhoneNumber(ph);
        account.setPin(pin);
        account.setDateOfBirth(dob);
        account.setBranch(branch);
        account.setOpeningDeposit(deposit);

        try {
            String accNum = db.generateAccountNumber(branch, LocalDate.now().getYear());
            account.setAccountNumber(accNum);
            db.saveAccount(account);

            String summary = String.format(
                    "ACC: %s | %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                    accNum, account.getFullName(), account.accountTypeName(),
                    branch, dob, ph, deposit, em);

            txtAccountSummary.setText(summary);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Success");
            ok.setHeaderText("Account Created Successfully!");
            ok.setContentText("Account Number: " + accNum);
            ok.showAndWait();

        } catch (Exception ex) {
            showError("Failed to save account: " + ex.getMessage());
        }
    }

    private Account createAccount(String type) {
        return switch (type) {
            case "Savings"       -> new SavingsAccount();
            case "Current"       -> new CurrentAccount();
            case "Fixed Deposit" -> new FixedDepositAccount();
            case "Student"       -> new StudentAccount();
            default              -> new JointAccount();
        };
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please fix the following:");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFormFields() {
        txtFirstName.clear(); txtLastName.clear();
        txtNIN.clear(); txtSecondaryNIN.clear();
        txtEmail.clear(); txtConfirmEmail.clear();
        txtPhone.clear(); txtPin.clear();
        txtConfirmPin.clear(); txtDeposit.clear();
        cmbYear.setValue(null); cmbMonth.setValue(null);
        cmbDay.getItems().clear();
        cmbAccountType.setValue(null); cmbBranch.setValue(null);
        txtAccountSummary.clear();
    }
}