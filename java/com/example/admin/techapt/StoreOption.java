package com.example.admin.techapt;

public class StoreOption {
    int que_no;
    String option;

    public StoreOption() {
    }

    public int getQue_no() {

        return que_no;
    }

    public void setQue_no(int que_no) {
        this.que_no = que_no;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public StoreOption(int que_no, String option) {

        this.que_no = que_no;
        this.option = option;
    }
}
