
package com.example.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaPostgresSql {


    public static int writeToDatabase2(String username, String password, String fullName, String gender, Date birthDate) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password_1 = "marcela2003";

        String name = username;
        String pass = password;

        String checkQuery = "SELECT user_id FROM multiusers WHERE username = ?";
        String insertQuery = "INSERT INTO multiusers (username, password, full_name, gender, birth_date) VALUES (?, ?, ?, ?, ?)";

        int userId = -1;

        try (Connection con = DriverManager.getConnection(url, user, password_1);
             PreparedStatement checkStatement = con.prepareStatement(checkQuery);
             PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {

            // Check if the user already exists in the database
            checkStatement.setString(1, name);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // User already exists, do not insert again
                userId = resultSet.getInt("user_id");
            } else {
                // User doesn't exist, insert new user
                insertStatement.setString(1, name);
                insertStatement.setString(2, pass);
                insertStatement.setString(3, fullName);
                insertStatement.setString(4, gender);
                insertStatement.setDate(5, birthDate);
                insertStatement.executeUpdate();

                // Retrieve the generated user ID
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }

            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgresSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userId;
    }


    public static int retrieveUserID(String username, String password) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password_1 = "marcela2003";

        String query = "SELECT user_id FROM multiusers WHERE username = ? AND password = ?";
        int userID = -1;

        try (Connection con = DriverManager.getConnection(url, user, password_1);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("user_id");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgresSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userID;
    }

    /*
    public static void removeFromDatabase(String isbn) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password_1 = "marcela2003";

        String query = "DELETE FROM books WHERE id = ?";

        try (Connection con = DriverManager.getConnection(url, user, password_1);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, isbn);
            pst.executeUpdate();
            System.out.println("Book deleted successfully from database");

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgresSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


     */
    public static boolean isAdmin(String username, String password) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password_1 = "marcela2003";

        String checkQuery = "SELECT user_id FROM multiusers WHERE username = ? AND password = ?";

        try (Connection con = DriverManager.getConnection(url, user, password_1);
             PreparedStatement checkStatement = con.prepareStatement(checkQuery)) {

            // Check if the provided credentials match the admin credentials in the database
            checkStatement.setString(1, username);
            checkStatement.setString(2, password);
            ResultSet resultSet = checkStatement.executeQuery();

            return resultSet.next() && resultSet.getInt("user_id") == 0;
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgresSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }


    public static ObservableList<Book> getBookDataFromDatabase() {
        ObservableList<Book> data = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name " +
                    "FROM books b " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                String categoryName = rs.getString("category_name");
                data.add(new Book(authorName, bookName, categoryName, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


    public static ObservableList<Book> searchBooksByAuthor(String authorName, String bookName, String isbn) {
        ObservableList<Book> searchResults = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name FROM books b " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id " +
                    "WHERE LOWER(author_name) LIKE LOWER(?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + authorName.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String fetchedBookName = rs.getString("book_name");
                String fetchedAuthorName = rs.getString("author_name");
                String fetchedCategory = rs.getString("category_name");

                // Create Book objects and add them to the searchResults list
                searchResults.add(new Book(fetchedAuthorName, fetchedBookName, fetchedCategory, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }


    public static ObservableList<Book> searchBooksByTitle(String authorName, String bookName, String isbn) {
        ObservableList<Book> searchResults = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name FROM books b " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id " +
                    "WHERE LOWER(book_name) LIKE LOWER(?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + bookName.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String fetchedBookName = rs.getString("book_name");
                String fetchedAuthorName = rs.getString("author_name");
                String fetchedCategory = rs.getString("category_name");

                // Create Book objects and add them to the searchResults list
                searchResults.add(new Book(fetchedAuthorName, fetchedBookName, fetchedCategory, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }


    public static ObservableList<Book> searchBooksById(int bookId) {
        ObservableList<Book> searchResults = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name " +
                    "FROM books b " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id " +
                    "WHERE b.book_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, bookId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int fetchedBookId = rs.getInt("book_id");
                String fetchedBookName = rs.getString("book_name");
                String fetchedAuthorName = rs.getString("author_name");
                String fetchedCategory = rs.getString("category_name");

                // Create Book objects and add them to the searchResults list
                searchResults.add(new Book(fetchedAuthorName, fetchedBookName, fetchedCategory, fetchedBookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }


    public static void addBookToUser(int userId, int bookId) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        String insertQuery = "INSERT INTO userbooks (user_id, book_id, borrow_date) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {

            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, bookId);
            insertStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Set the borrow date

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book added successfully to user with borrow date!");
            } else {
                System.out.println("Failed to add book to user!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error occurred while adding book to user: " + ex.getMessage());
        }
    }


    public static ObservableList<Book> getBooksFromUserBookTable(int userId) {
        ObservableList<Book> data = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name " +
                    "FROM userbooks ub " +
                    "JOIN books b ON ub.book_id = b.book_id " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id " +
                    "WHERE ub.user_id = ? AND ub.return_date IS NULL";  // Include only books with NULL return_date
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                String categoryName = rs.getString("category_name");
                data.add(new Book(authorName, bookName, categoryName, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void removeBookFromUser(int userId, int bookId) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE userbooks SET return_date = ? WHERE user_id = ? AND book_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Set the return date
            pstmt.setInt(2, userId);
            pstmt.setInt(3, bookId);
            pstmt.executeUpdate();
            System.out.println("Book with ID " + bookId + " returned successfully for user ID " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ObservableList<Book> getBooksByCategory(String category) {
        ObservableList<Book> data = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT b.book_id, b.book_name, a.author_name, c.category_name " +
                    "FROM books b " +
                    "JOIN bookauthor ba ON b.book_id = ba.book_id " +
                    "JOIN authors a ON ba.author_id = a.author_id " +
                    "LEFT JOIN BooksCategories bc ON b.book_id = bc.book_id " +
                    "LEFT JOIN category c ON bc.category_id = c.category_id " +
                    "WHERE c.category_name = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                String categoryName = rs.getString("category_name");
                data.add(new Book(authorName, bookName, categoryName, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void saveReviewToDatabase(int bookId, int userId, String reviewText) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        String insertQuery = "INSERT INTO reviews (book_id, user_id, review_text) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {

            insertStatement.setInt(1, bookId);
            insertStatement.setInt(2, userId);
            insertStatement.setString(3, reviewText);

            // Execute the insert query
            insertStatement.executeUpdate();

            System.out.println("Review saved to the database!");
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MyBooks.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    public static ObservableList<Review> getReviewsByBookId(int bookId) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password = "marcela2003";

        String query = "SELECT * FROM reviews WHERE book_id = ?";
        ObservableList<Review> reviewsData = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int reviewId = resultSet.getInt("review_id");
                int userId = resultSet.getInt("user_id");
                String reviewText = resultSet.getString("review_text");


                Review review = new Review(reviewId, userId, reviewText);
                reviewsData.add(review);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgresSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return reviewsData;
    }


    public static ObservableList<Users> getUsers(int userId) {
        ObservableList<Users> data = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password1 = "marcela2003";

        try (Connection con = DriverManager.getConnection(url, user, password1)) {
            String sql = "SELECT * FROM multiusers WHERE user_id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int retrievedUserId = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String fullName = rs.getString("full_name");
                    String gender = rs.getString("gender");
                    String birthDate = rs.getString("birth_date");

                    data.add(new Users(retrievedUserId, username, password, fullName, gender, birthDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static boolean updatePassword(int userId, String newPassword) {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password1 = "marcela2003";

        String sql = "UPDATE multiusers SET password = ? WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password1);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void deleteUser(int userId) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/javafx";
        String user = "postgres";
        String password1 = "marcela2003";

        String deleteQuery = "DELETE FROM multiusers WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password1);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


}



