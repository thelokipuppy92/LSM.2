package com.example.lms;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Admin {
    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> authorNameColumn;

    @FXML
    private TableColumn<Book, String> categoryNameColumn;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> bookIdColumn;


    public void initialize() {
        setupColumns();

        ObservableList<Book> data = JavaPostgresSql.getBookDataFromDatabase();

        tableView.setItems(data);
    }

    private void setupColumns() {
        bookIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBook_id())));
        bookNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        categoryNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
    }
}