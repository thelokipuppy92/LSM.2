package com.example.lms;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Review {
    private final SimpleIntegerProperty reviewId;
    private final SimpleIntegerProperty userId;
    private final SimpleStringProperty reviewText;

    public Review(int reviewId, int userId, String reviewText) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.userId = new SimpleIntegerProperty(userId);
        this.reviewText = new SimpleStringProperty(reviewText);
    }

    public int getReviewId() {
        return reviewId.get();
    }

    public SimpleIntegerProperty reviewIdProperty() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId.set(reviewId);
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getReviewText() {
        return reviewText.get();
    }

    public SimpleStringProperty reviewTextProperty() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText.set(reviewText);
    }
}