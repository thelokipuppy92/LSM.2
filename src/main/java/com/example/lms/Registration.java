package com.example.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class Registration {
    @FXML
    private Button back_button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Button enter_button;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TextField fullName;

    @FXML
    private ComboBox<String> genderComboBox;

    int userID;

    @FXML
    private ImageView icon;

    Image myImage = new Image(getClass().getResourceAsStream("registration.jpg"));

    public void displayImage() {
        icon.setImage(myImage);
    }

    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
    }

    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        Registration controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = LibraryManagement.getPrimaryStage();
        stage.hide();
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void getData5() throws IOException {
        HelloController.changeScene(userID);
    }

    private void initializeGenderComboBox() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female");
        genderComboBox.setItems(genderOptions);
        genderComboBox.setPromptText("Select Gender");
    }

    @FXML
    private void initialize() {
        initializeGenderComboBox();
    }


    @FXML
    protected void handleRegistration() {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        String confirmedPassword = confirmPassword.getText();
        String enteredFullName = fullName.getText();
        String enteredGender = genderComboBox.getValue();
        LocalDate enteredBirthDate = dateOfBirth.getValue();

        if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {
            if (!enteredPassword.equals(confirmedPassword)) {
                System.out.println("Passwords do not match");
            } else {
                if (enteredFullName.isEmpty() || enteredGender == null || enteredBirthDate == null) {
                    System.out.println("Please enter all required information");
                } else {
                    // Convert LocalDate to java.sql.Date
                    Date sqlBirthDate = Date.valueOf(enteredBirthDate);

                    int userID = JavaPostgresSql.writeToDatabase2(enteredUsername, enteredPassword, enteredFullName, enteredGender, sqlBirthDate);

                    if (userID != -1) {
                        System.out.println("User already exists.");
                    } else {
                        System.out.println("Registration successful!");
                    }
                }
            }
        } else {
            System.out.println("Please enter both username and password");
        }
    }


}


