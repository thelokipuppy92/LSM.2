package com.example.lms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class Dialogboxpassword {
    private int userID;
    @FXML
    private Button saveButton;
    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confrimPassword;

    private Myaccount myaccountController;

    public void setUserId(int userID, Myaccount myaccountController) {
        this.userID = userID;
        this.myaccountController = myaccountController;
    }



    @FXML
    protected void changePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confrimPassword.getText();

        if (!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {
                // Passwords match, update the password in the database
                boolean success = updatePassword(userID, newPassword);

                if (success) {
                    System.out.println("Password changed successfully");

                    // Update the password label in the Myaccount controller
                    myaccountController.updatePasswordLabel(newPassword);

                    // Close the dialog box
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();

                    // Optionally, clear the new password and confirm password fields after a successful change
                    newPasswordField.clear();
                    confrimPassword.clear();
                } else {
                    System.out.println("Failed to change password");
                }
            } else {
                System.out.println("New password and confirm password do not match");
            }
        } else {
            System.out.println("New password and confirm password cannot be empty");
        }
    }


    private boolean updatePassword(int userId, String newPassword) {
        return JavaPostgresSql.updatePassword(userId, newPassword);
    }

}
