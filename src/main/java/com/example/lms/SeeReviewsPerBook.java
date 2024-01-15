package com.example.lms;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SeeReviewsPerBook {

    @FXML
    private TableView<Review> reviewsTableView;

    @FXML
    private TableColumn<Review, String> reviewIdColumn;

    @FXML
    private TableColumn<Review, String> userIdColumn;

    @FXML
    private TableColumn<Review, String> reviewTextColumn;

    private int bookId;

    public void initData(int bookId) {
        this.bookId = bookId;
        initializeReviewsTableView(bookId);
    }

    private void setupReviewsColumns() {
        reviewIdColumn.setCellValueFactory(cellData -> cellData.getValue().reviewIdProperty().asString());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asString());
        reviewTextColumn.setCellValueFactory(cellData -> cellData.getValue().reviewTextProperty());
    }

    private void initializeReviewsTableView(int bookId) {
        setupReviewsColumns();
        ObservableList<Review> reviewsData = JavaPostgresSql.getReviewsByBookId(bookId);
        reviewsTableView.setItems(reviewsData);
    }


}
