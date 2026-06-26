package com.firstbank.db;

import com.firstbank.model.Account;
import java.sql.*;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:FirstBankUG.db";
    private Connection connection;

    public DatabaseHelper() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found", e);
        }
        connection = DriverManager.getConnection(DB_URL);
        createSchema();
    }

    private void createSchema() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Accounts (" +
                "AccountNumber TEXT PRIMARY KEY, " +
                "FirstName     TEXT, " +
                "LastName      TEXT, " +
                "Nin           TEXT, " +
                "Email         TEXT, " +
                "PhoneNumber   TEXT, " +
                "AccountType   TEXT, " +
                "Branch        TEXT, " +
                "DateOfBirth   TEXT, " +
                "OpeningDeposit REAL, " +
                "CreatedAt     TEXT)";
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        }
    }

    public synchronized String generateAccountNumber(String branch, int year) throws SQLException {
        String prefix = branchCode(branch) + "-" + year + "-";
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM Accounts WHERE AccountNumber LIKE ?")) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                int count = rs.next() ? rs.getInt(1) : 0;
                return prefix + String.format("%06d", count + 1);
            }
        }
    }

    public void saveAccount(Account a) throws SQLException {
        String sql = "INSERT INTO Accounts VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getFirstName());
            ps.setString(3, a.getLastName());
            ps.setString(4, a.getNin());
            ps.setString(5, a.getEmail());
            ps.setString(6, a.getPhoneNumber());
            ps.setString(7, a.accountTypeName());
            ps.setString(8, a.getBranch());
            ps.setString(9, a.getDateOfBirth().toString());
            ps.setDouble(10, a.getOpeningDeposit());
            ps.setString(11, java.time.LocalDateTime.now().toString());
            ps.executeUpdate();
        }
    }

    private String branchCode(String branch) {
        return switch (branch) {
            case "Kampala" -> "KLA";
            case "Gulu"    -> "GUL";
            case "Mbarara" -> "MBR";
            case "Jinja"   -> "JJA";
            case "Mbale"   -> "MBL";
            default        -> "GEN";
        };
    }

    public void close() {
        try { if (connection != null && !connection.isClosed()) connection.close(); }
        catch (SQLException e) { e.printStackTrace(); }
    }
}