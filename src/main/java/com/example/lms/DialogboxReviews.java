package com.example.lms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;



public class DialogboxReviews {

    @FXML
    private Button saveButton;

    @FXML
    private TextArea reviewTextArea;

    private int bookId;
    private int userId;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    @FXML
    protected void saveReview() {
        String reviewText = reviewTextArea.getText();
        JavaPostgresSql.saveReviewToDatabase(bookId, userId, reviewText);

        Stage stage = (Stage) saveButton.getScene().getWindow();

        stage.close();
    }



}
