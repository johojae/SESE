package com.sese.showmethebeer;

public class BeerListCategory {
    int id;
    String name;

    public BeerListCategory(int id, String name){
        super();
        this.id = id;
        this. name = name;
    }

    public BeerListCategory(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
