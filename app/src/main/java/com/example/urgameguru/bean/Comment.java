package com.example.urgameguru.bean;

import java.util.Date;

public class Comment {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentRate() {
        return commentRate;
    }

    public void setCommentRate(String commentRate) {
        this.commentRate = commentRate;
    }

    private String userName;
    private String commentText;
    private String commentDate;
    private String commentRate;

    private Comment(){}

    public Comment(String name, String text, String date, String rate) {
        userName = name;
        commentText = text;
        commentDate = date;
        commentRate = rate;
    }


}
