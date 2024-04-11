package at.aau.serg.websocketdemoserver.domains;

import java.util.UUID;

public class User {
    private String id;
    private String username;

    private String password;

    private double points;

    //private static long counter = 1;

    public User() {
        //default
    }

    public User(String username) {
        //this.id = Long.toString(counter++);
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.points = 0;
    }


    //constructor for checking login
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
