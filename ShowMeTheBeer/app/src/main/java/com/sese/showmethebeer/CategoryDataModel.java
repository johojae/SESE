package com.sese.showmethebeer;

import java.util.List;

class NestedList{
    String nestedName;
    String id;

    NestedList(String nestedName, String id){
        this.nestedName = nestedName;
        this.id = id;
    }
}

public class CategoryDataModel {

    private List<NestedList> nestedList;

    private String itemText;
    private boolean isExpandable;

    public CategoryDataModel(List<NestedList> itemList, String itemText) {
        this.nestedList = itemList;
        this.itemText = itemText;
        isExpandable = false;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<NestedList> getNestedList() {
        return nestedList;
    }

    public String getItemText() {
        return itemText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }
}