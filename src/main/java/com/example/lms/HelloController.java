package com.example.lms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloController {

    @FXML
    private Button register_button;

    @FXML
    private Button enter_button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wronglogin;

    private static int userID=-1;

    @FXML
    private ImageView icon;

    @FXML
    private ImageView left;

    @FXML
    private ImageView right;

    Image myImage = new Image(getClass().getResourceAsStream("book.jpg"));

    Image myImage2 = new Image(getClass().getResourceAsStream("right.jpg"));

    Image myImage1 = new Image(getClass().getResourceAsStream("left.jpg"));

    public void displayImage() {
        left.setImage(myImage1);
        icon.setImage(myImage);
        right.setImage(myImage2);
    }


    @FXML
    protected void getData() throws IOException {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            int userID = JavaPostgresSql.retrieveUserID(username.getText(), password.getText());

            // Check if the login belongs to an admin
            boolean isAdmin = JavaPostgresSql.isAdmin(username.getText(), password.getText());

            if (isAdmin) {
                // Load the admin scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) enter_button.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Admin dashboard");
                stage.setScene(scene);
                stage.show();
            } else if (userID != -1) {
                // Load the regular user scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("afterLogin.fxml"));
                Parent root = loader.load();

                AfterLogin afterLoginController = loader.getController();
                afterLoginController.initData(userID); // Pass userID to the next controller

                Stage stage = (Stage) enter_button.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Library Management System");
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Failed to get user ID.");
                // Handle the case where user ID retrieval failed
            }
        } else {
            System.out.println("Please enter both username and password");
        }
    }


    @FXML
    protected void getData6() throws IOException {
        Registration.changeScene(userID);

    }

    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
    }

    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        HelloController controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = LibraryManagement.getPrimaryStage();
        stage.hide();
        stage.setTitle("Library Managemet System");
        stage.setScene(scene);
        stage.show();
    }


}