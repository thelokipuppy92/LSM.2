package com.example.lms;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;



public class MyBooks {

    @FXML
    private TableColumn<Book, String> authorNameColumn;

    @FXML
    private TableColumn<Book, String> bookIdColumn;

    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> categoryNameColumn;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private Button return_book;

    @FXML
    private Button writeReviewButton;

    @FXML
    public Button go_go_back;

    int userID;


    @FXML
    public static void changeScene(int userId) throws IOException {
        System.out.println("Current User ID: " + userId);

        FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("my_books.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        // Set the userID in the new controller
        MyBooks controller = fxmlLoader.getController();
        controller.initData(userId);

        Stage stage = LibraryManagement.getPrimaryStage();
        stage.hide();
        stage.setTitle("My books");
        stage.setScene(scene);
        stage.show();
    }


    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
        initialize();
    }


    @FXML
    protected void getData() throws IOException {
        AfterLogin.changeScene(userID);
    }


    public void initialize() {
        setupColumns();

        ObservableList<Book> data = JavaPostgresSql.getBooksFromUserBookTable(userID);

        tableView.setItems(data);
    }


    private void setupColumns() {
        bookIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBook_id())));
        bookNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        categoryNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
    }



    @FXML
    protected void returnBook() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = selectedBook.getBook_id();
            JavaPostgresSql.removeBookFromUser(userID, bookId);
            tableView.getItems().remove(selectedBook); // Remove the book from the table
        } else {
            System.out.println("No book selected!");
        }
    }



    @FXML
    protected void writeReview() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = selectedBook.getBook_id(); // Fetch the book ID
            openReviewDialog(bookId, userID); // Pass both book ID and user ID to the dialog box
        } else {
            System.out.println("No book selected!");
        }
    }

    private void openReviewDialog(int bookId, int userId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("dialogbox_reviews.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            // Pass both book ID and user ID to the dialog box controller
            DialogboxReviews controller = fxmlLoader.getController();
            controller.setBookId(bookId);
            controller.setUserId(userId);

            // Create a new stage (window) for the custom dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Write Review");
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



}
