package com.example.lms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AfterLogin {
    @FXML
    private AnchorPane available_books;

    @FXML
    private Button mybooks;

    @FXML
    private Button newbook;
    @FXML
    private ImageView icon;

    private int userID;

    @FXML
    private Button buttonMyAccount;

    Image myImage = new Image(getClass().getResourceAsStream("afterlogin.jpeg"));

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

    FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("afterLogin.fxml"));
    Scene scene = new Scene(fxmlLoader.load()); // scene

    // Set the userID in the new controller
    AfterLogin controller = fxmlLoader.getController();
    controller.initData(userId);

    Stage stage = LibraryManagement.getPrimaryStage();
    stage.hide();
    stage.setTitle("Library Managemet System");
    stage.setScene(scene);
    stage.show();
}

    @FXML
    protected void getData() throws IOException {
        MyBooks.changeScene(userID);
    }

    @FXML
    protected void getData_1() throws IOException {
        NewBook.changeScene(userID);
    }


    @FXML
    protected void getData_2() throws IOException {
        Myaccount.changeScene(userID);
    }

}
