package com.sese.showmethebeer.data;

public class UserBeerInfo {

    private String beerId;

    private int rating;


    public UserBeerInfo(String beerId, int rating) {
        this.beerId = beerId;
        this.rating = rating;
    }

    public String getBeerId() {
        return beerId;
    }

    public int getRating() {
        return rating;
    }

}
