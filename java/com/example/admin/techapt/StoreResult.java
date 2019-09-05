package com.example.admin.techapt;

public class StoreResult {

    String test_name;
    String date;
    int marks;

    public StoreResult() {
    }

    int total_questions;

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(int total_questions) {
        this.total_questions = total_questions;
    }

    public StoreResult(String test_name, String date, int marks, int total_questions) {

        this.test_name = test_name;
        this.date = date;
        this.marks = marks;
        this.total_questions = total_questions;
    }
}
