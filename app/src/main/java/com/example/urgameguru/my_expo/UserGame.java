package com.example.urgameguru.my_expo;

public class UserGame {
    public int num_articles;
    public int num_screenshots;
    public int num_clips;

    public UserGame() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserGame(int num_articles, int num_screenshots, int num_clips) {
        this.num_articles = num_articles;
        this.num_screenshots = num_screenshots;
        this.num_clips = num_clips;
    }
}
