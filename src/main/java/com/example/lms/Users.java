package com.example.lms;

public class Users {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String gender;
    private String birthDate;

    public Users(int userId, String username, String password, String fullName, String gender, String birthDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
    }


    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }
}