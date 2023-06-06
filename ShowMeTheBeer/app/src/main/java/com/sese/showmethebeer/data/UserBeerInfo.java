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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final UserBeerInfo other = (UserBeerInfo) obj;
        if ((this.beerId == null) ? (other.beerId != null) : !this.beerId.equals(other.beerId)) {
            return false;
        }

        if (this.rating != other.rating) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.beerId != null ? this.beerId.hashCode() : 0);
        hash = 53 * hash + this.rating;
        return hash;
    }
}
