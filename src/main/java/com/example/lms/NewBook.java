package com.example.lms;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class NewBook {

    @FXML
    private TableColumn<Book, String> authorNameColumn;

    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> categoryNameColumn;

    @FXML
    private TableColumn<Book, String> bookIdColumn;

    @FXML
    private TableView<Book> tableView;


    @FXML
    private TextField isbn;

    @FXML
    private Button enter;

    @FXML
    private TextField author_name;

    @FXML
    private TextField book_name;

    @FXML
    public Button go_back;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Button seeReviewsButton;

    int userID;


@FXML
    public static void changeScene(int userId) throws IOException {
    System.out.println("Current User ID: " + userId);

    FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("new_book.fxml"));
    Scene scene = new Scene(fxmlLoader.load()); // scene

    // Set the userID in the new controller
    NewBook controller = fxmlLoader.getController();
    controller.initData(userId);

    Stage stage = LibraryManagement.getPrimaryStage();
    stage.hide();
    stage.setTitle("Add books");
    stage.setScene(scene);
    stage.show();
}
    public void initData(int userID) {
        this.userID = userID;
        System.out.println("Current User ID: " + userID);
        initialize1();
        initialize2();
    }

    @FXML
    protected void getData() throws IOException {
        AfterLogin.changeScene(userID);
    }



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


    @FXML
    private void handleSearch() {
        String authorName = author_name.getText();
        String bookName = book_name.getText();
        String isbnText = isbn.getText();

        // Perform the search operation based on the entered criteria
        ObservableList<Book> searchResults = JavaPostgresSql.searchBooksByAuthor(authorName, bookName, isbnText);

        // Display search results in the TableView
        tableView.setItems(searchResults);
    }

    @FXML
    private void handleSearch1() {
        String authorName = author_name.getText();
        String bookName = book_name.getText();
        String isbnText = isbn.getText();

        ObservableList<Book> searchResults = JavaPostgresSql.searchBooksByTitle(authorName, bookName, isbnText);

        tableView.setItems(searchResults);
    }

    @FXML
    private void handleSearch2() {
        String isbnText = isbn.getText().trim();

        if (isbnText.isEmpty()) {
            reloadInitialData(); // Reload the initial data if search text is empty
        } else {
            try {
                int bookId = Integer.parseInt(isbnText);
                ObservableList<Book> searchResults = JavaPostgresSql.searchBooksById(bookId);
                tableView.setItems(searchResults); // Set search results to TableView
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void reloadInitialData() {
        ObservableList<Book> initialData = JavaPostgresSql.getBookDataFromDatabase();
        tableView.setItems(initialData);
    }

    @FXML
    protected void addItem5() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            int bookId = selectedBook.getBook_id();
            JavaPostgresSql.addBookToUser(userID, bookId);

            author_name.setText("");
            book_name.setText("");
            isbn.setText("");
            reloadInitialData(); // Refresh data in the TableView
        } else {
            System.out.println("Please select a book from the table.");
        }
    }

    @FXML
    public void initialize1() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "No Filter",
                "Dystopian",
                "Romance",
                "Adventure",
                "Magical Realism",
                "Fantasy"
        );

        categoryFilter.setItems(categories);
    }

@FXML
private void handleCategoryFilter() {
    String selectedCategory = categoryFilter.getValue();
    if (selectedCategory != null) {
        if (selectedCategory.equals("No Filter")) {
            // If "No Filter" is selected, load all books (remove the filter)
            ObservableList<Book> allBooks = JavaPostgresSql.getBookDataFromDatabase();
            tableView.setItems(allBooks);
        } else {
            // Filter books based on the selected category
            ObservableList<Book> filteredBooks = JavaPostgresSql.getBooksByCategory(selectedCategory);
            tableView.setItems(filteredBooks);
        }
    } else {
        System.out.println("Please select a category.");
    }
}


    @FXML
    public void initialize2() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "A-Z",
                "Z-A"
        );

        sortComboBox.setItems(categories);
    }


    @FXML
    private void handleSortOrderChange() {
        String selectedSortOrder = sortComboBox.getValue();
        if (selectedSortOrder != null) {
            // Get the current items from the table
            ObservableList<Book> currentItems = tableView.getItems();

            // Sort the items based on the selected sort order
            switch (selectedSortOrder) {
                case "A-Z":
                    currentItems.sort((book1, book2) -> book1.getTitle().compareToIgnoreCase(book2.getTitle()));
                    break;
                case "Z-A":
                    currentItems.sort((book1, book2) -> book2.getTitle().compareToIgnoreCase(book1.getTitle()));
                    break;
            }

            tableView.setItems(currentItems);
        }
    }



    private void openBookReviewsDialog(int bookId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryManagement.class.getResource("seeReviewsPerBook.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            // Pass the book ID to the dialog box controller
            SeeReviewsPerBook controller = fxmlLoader.getController();
            controller.initData(bookId);

            // Create a new stage (window) for the custom dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Book Reviews");
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




    @FXML
    private void seeReviewsButtonClicked(ActionEvent event) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = selectedBook.getBook_id();
            openBookReviewsDialog(bookId);
        }
    }


}
