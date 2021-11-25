package com.example.urgameguru.my_expo;

public class Game {
    public String developer;
    public String publisher;
    public String release;
    public String mode;
    public int num_users;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Game(String developer, String publisher, String release, String mode) {
        this.release = release;
        this.developer = developer;
        this.publisher = publisher;
        this.mode = mode;
        this.num_users = 0;
    }

}
