package com.example.lms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Myaccount {
    @FXML
    private Button backButton;

    @FXML
    private Label birthDateLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label usernameLabel;

    private int userID;

    @FXML
    private Button deleteAccount;

    @FXML
    private Button changePasswordButton;

    @FXML
    private ImageView myAccountIcon;

    Image myImage = new Image(getClass().getResourceAsStream("myaccount.jpg"));

    public void displayImage() {
        myAccountIcon.setImage(myImage);
    }

    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
        initialize();
    }


    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("myaccount.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        Myaccount controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = LibraryManagement.getPrimaryStage();
        stage.hide();
        stage.setTitle("My account");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void getData() throws IOException {
        AfterLogin.changeScene(userID);

    }



    public void initialize() {

        ObservableList<Users> userData = JavaPostgresSql.getUsers(userID);

        if (!userData.isEmpty()) {
            Users currentUser = userData.get(0);

            // Populate user details from the provided User object
            usernameLabel.setText(currentUser.getUsername());
            passwordLabel.setText(currentUser.getPassword());
            fullNameLabel.setText(currentUser.getFullName());
            genderLabel.setText(currentUser.getGender());
            birthDateLabel.setText(currentUser.getBirthDate());
        } else {
            System.out.println("User not found");
        }
    }


    @FXML
    private void openDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Myaccount.class.getResource("dialogboxpassword.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            // Pass user ID to the dialog box controller
            Dialogboxpassword controller = fxmlLoader.getController();
            controller.setUserId(userID, this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Change Password");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(LibraryManagement.getPrimaryStage());

            // Set the content of the dialog
            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

            // Show the dialog and wait for it to be closed
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updatePasswordLabel(String newPassword) {
        passwordLabel.setText(newPassword);
    }

    @FXML
    protected void deleteAccount() {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to delete your account?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion, proceed to delete the account
            try {
                // Delete the account from the database
                JavaPostgresSql.deleteUser(userID);

                // Show a success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your account has been deleted successfully.");
                successAlert.showAndWait();

                // Navigate back to the login page
                HelloController.changeScene(userID);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while deleting your account. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }



}