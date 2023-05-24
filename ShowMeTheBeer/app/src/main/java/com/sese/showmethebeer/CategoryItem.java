package com.sese.showmethebeer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class DetailCategory{
    String id;
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class CategoryItem {
    String id;
    String name;
    ArrayList<DetailCategory> detailCategorys;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(DetailCategory detailCategory) {
        detailCategorys.add(detailCategory);
    }
}
