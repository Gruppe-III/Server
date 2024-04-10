package at.aau.serg.websocketdemoserver.domains;

import java.util.UUID;

public class User {
    private String id;
    private String username;
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


    public String getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
