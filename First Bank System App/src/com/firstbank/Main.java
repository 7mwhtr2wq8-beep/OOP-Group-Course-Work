package com.firstbank;

import com.firstbank.ui.AccountOpeningForm;
import com.firstbank.db.DatabaseHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private DatabaseHelper db;

    @Override
    public void start(Stage primaryStage) throws Exception {
        db = new DatabaseHelper();
        AccountOpeningForm form = new AccountOpeningForm(db);
        Scene scene = new Scene(form, 720, 620);
        primaryStage.setTitle("First Bank Uganda Core Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (db != null) db.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}