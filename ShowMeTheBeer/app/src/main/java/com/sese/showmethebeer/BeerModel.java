package com.sese.showmethebeer;

public class BeerModel {
    public Integer iconId;
    public String name;

    public BeerModel(Integer iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }

    public Integer getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }
}
