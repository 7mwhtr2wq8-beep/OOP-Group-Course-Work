package com.firstbank.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,30}$");
    private static final Pattern NIN_PATTERN = Pattern.compile("^[A-Za-z0-9]{14}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+256\\d{9}$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{4,6}$");

    public static String validateInputs(
            String fName, String lName, String nin, String secNin, String email, String confEmail,
            String phone, String pin, String confPin, Integer year, String month, Integer day,
            String accType, String branch, String depositStr) {

        StringBuilder errors = new StringBuilder();

        if (!NAME_PATTERN.matcher(fName.trim()).matches()) errors.append("- First Name must be letters only (2-30 chars).\n");
        if (!NAME_PATTERN.matcher(lName.trim()).matches()) errors.append("- Last Name must be letters only (2-30 chars).\n");
        if (!NIN_PATTERN.matcher(nin.trim()).matches()) errors.append("- National ID (NIN) must be exactly 14 alphanumeric chars.\n");
        
        if ("Joint".equals(accType) && !NIN_PATTERN.matcher(secNin.trim()).matches()) {
            errors.append("- Joint accounts require a valid 14-character Secondary NIN.\n");
        }

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) errors.append("- Invalid Email address format.\n");
        if (!email.trim().equalsIgnoreCase(confEmail.trim())) errors.append("- Email fields do not match.\n");
        if (!PHONE_PATTERN.matcher(phone.trim()).matches()) errors.append("- Phone must match Ugandan format (+256XXXXXXXXX).\n");

        if (!PIN_PATTERN.matcher(pin).matches()) errors.append("- PIN must be numeric (4-6 digits).\n");
        if (!pin.equals(confPin)) errors.append("- PIN fields do not match.\n");
        if (pin.chars().distinct().count() == 1) errors.append("- PIN cannot contain all-identical digits (e.g. 0000).\n");

        if (year == null || month == null || day == null) {
            errors.append("- Date of Birth dropdown parameters are incomplete.\n");
        } else {
            int monthIdx = getMonthIndex(month);
            LocalDate dob = LocalDate.of(year, monthIdx, day);
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18 || age > 75) errors.append("- Applicant must be aged 18 to 75.\n");
            if ("Student".equals(accType) && (age < 18 || age > 25)) errors.append("- Student account eligibility is limited to ages 18-25.\n");
        }

        if (branch == null) errors.append("- Please select a processing Branch.\n");
        if (accType == null) errors.append("- Please select an Account Type.\n");

        try {
            double dep = Double.parseDouble(depositStr.trim());
            double min = getMinDeposit(accType);
            if (dep < min) errors.append("- Opening Deposit is below minimum for this type (Min: ").append(min).append(" UGX).\n");
        } catch (NumberFormatException ex) {
            errors.append("- Opening Deposit field must be a valid numeric amount.\n");
        }

        return errors.toString();
    }

    private static int getMonthIndex(String m) {
        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for (int i = 0; i < 12; i++) if (months[i].equals(m)) return i + 1;
        return 1;
    }

    private static double getMinDeposit(String type) {
        if ("Savings".equals(type)) return 50000;
        if ("Current".equals(type)) return 200000;
        if ("Fixed Deposit".equals(type)) return 1000000;
        if ("Student".equals(type)) return 10000;
        if ("Joint".equals(type)) return 100000;
        return 0;
    }
}
