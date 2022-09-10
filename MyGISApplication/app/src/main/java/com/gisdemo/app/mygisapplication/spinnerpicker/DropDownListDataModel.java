package com.gisdemo.app.mygisapplication.spinnerpicker;

import java.io.Serializable;

public class DropDownListDataModel implements Serializable {
    String id;
    String name;
    boolean isSelected = false;

    public DropDownListDataModel(){

    }

    public DropDownListDataModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DropDownListDataModel(String id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
