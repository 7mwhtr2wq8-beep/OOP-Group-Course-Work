Project Description
The First Bank Uganda Account Opening Application is a desktop JavaFX application that models the account opening process of a banking institution. It gathers client personal information, performs thorough validation checks, creates an account number, and saves the entry in a MS Access database. The program showcases basic Object-Oriented Programming concepts: abstraction, inheritance, polymorphism, and encapsulation.
Requirements
To use this application, make sure that the following software components are installed on your machine:
•	Java Development Kit (JDK) 17+.
•	Visual Studio Code (or any other Java IDE) with the Java Extension Pack.
•	JavaFX SDK 26.0.1 (or any other stable version) – download at [gluonhq.com](https://gluonhq.com/products/javafx/)
•	UCanAccess JDBC driver and dependencies (described further down in this document).
 Setup Instructions
 Clone the repository
Open terminal, enter the following command and open the resulting directory in VS Code:
“““bash
git clone https://github.com/Andrew-ze/First-Bank-Uganda.git
“““
 Configure JavaFX
- Download JavaFX SDK package and unzip it to a known directory (for example “C:\javafx-sdk-26.0.1”)
- Update “.vscode/settings.json” and “.vscode/launch.json” files to point to the “lib” folder in your JavaFX installation. In “launch.json” the updated section should look like this:
“““json
"vmArgs": "--module-path \"C:\\javafx-sdk-26.0.1\\lib\" --add-modules javafx.controls,javafx.fxml"
“““
Add UCanAccess Dependencies
The program utilizes UCanAccess for connecting to the MS Access database. Put these libraries into the project”s “lib/” directory:
- “ucanaccess-5.0.1.jar”
- “hsqldb-2.5.0.jar”
- “jackcess-3.0.1.jar”
- “commons-lang3-3.8.1.jar”
- “commons-logging-1.2.jar”
The dependencies can be found on Maven Central or at the official UCanAccess website.
Compilation and Execution
Compile
Use the following command from the project directory:
“““bash
javac -d bin -cp "lib/" src/com/firstbank//.java
“““
Run
Invoke this command (configure the JavaFX path to match your installation):
“““bash
java --module-path "C:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib/" com.firstbank.Main
“““
Upon the first execution, the program will create the “firstbank.accdb” MS Access database file in the “resources/” directory (or the project root) and initialize the “Accounts” table. There”s no need to configure the database manually.
 User Interface
The form includes:
•	Text Fields for the First Name, Last Name, NIN, Email, Confirm Email, Phone Number and Opening Deposit.
•	Password fields for the PIN and Confirm PIN.
•	Combos for the Date of Birth (Year, Month, Day, with dynamic day count based on month length and leap year detection), Account Type (Savings, Current, Fixed Deposit, Student, Joint) and Branch (Kampala, Gulu, Mbarara, Jinja, Mbale).
•	The Submit and Reset buttons.
•	An Account Summary area which shows the formatted record in case of successful submission.

 Object-Oriented Design
The entire design revolves around an abstract “Account” class that holds common attributes (name, NIN, contacts, PIN, DOB, branch, deposit) and declares abstract methods “minimumDeposit()” and “accountTypeName()”. There are five subclasses namely, “SavingsAccount”, “CurrentAccount”, “FixedDepositAccount”, “StudentAccount” and “JointAccount” implementing these two methods with specific account values. When the user selects an account type, the program uses polymorphism to validate opening deposit.

Validation Rules
All fields are required and trimmed. The “Validator” class follows the below validation rules:
Field                	Rule
First / Last Name    	Letters only, 2–30 characters each.
National ID (NIN)    	Exactly 14 alphanumeric characters; auto‑converted to uppercase.
Email / Confirm Email	Valid email format; both fields must match exactly.
Phone Number         	Ugandan format: “+256” followed by 9 digits (total 12 digits after “+”).
PIN / Confirm PIN	4 numeric digits; must match; cannot be all identical (e.g., “0000”).
Date of Birth        	Complete date selected from combos; day count updates dynamically.
Age (derived)        	Between 18 and 75 years (18‑25 for Student accounts).
Account Type / Branch	Exactly one option must be selected.
Opening Deposit      	Must be a valid number and meet or exceed the minimum for the chosen type (Savings: 50,000; Current: 200,000; Fixed: 1,000,000; Student: 10,000; Joint: 100,000 UGX).

 Account Number Generation
After successful validation, the application generates a unique account number in the form “BRANCHCODE–YYYY–xxxxxx”.
 Branch Codes: Kampala–KLA, Gulu–GUL, Mbarara–MBR, Jinja–JJA, Mbale–MBL.
 Year: current year.
 Sequential number: 6 digit zero-padded counter that is reset yearly at each branch, retrieved from database query where the account number starts with the same prefix.
 Data Persistence
The account is stored in the “Accounts” table whose structure is as follows “(AccountNumber, FirstName, LastName, Nin, Email, PhoneNumber, AccountType, Branch, DateOfBirth, OpeningDeposit, CreatedAt)”. This is done using “DatabaseHelper” class that handles database connection, schema creation, number generation, and insertion.

Flow of Usage
1. Fill all fields. The Date of Birth dropdowns will auto-update based on the number of days.
2. Choose Account Type and Branch.
3. Input a valid Opening Deposit amount.
4. Click “Open Account.”
•	Validation failed: A pop-up message with all errors is shown.
•	All inputs are valid: The summary section will display the record, and the success dialog box will show the generated account number.
5. Click “Reset Form” to reset all fields.
