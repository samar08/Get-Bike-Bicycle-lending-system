package com.example.samar.getbike;

public class Availbikes {
    String name,id;
    public Availbikes(String name,String id){
        this.id=id;
        this.name=name;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
