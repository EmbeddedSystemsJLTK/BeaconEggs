package com.oamk.beaconeggs;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuItem {

    private String title;
    private String name;
    private String info;
    private boolean isFavourite;

    public LunchMenuItem(String title, String name, String info, boolean isFavourite){
        this.title = title;
        this.name = name;
        this.info = info;
        this.isFavourite = isFavourite;
    }

    public String getTitle() {
        return title;
    }

    public String getName(){
        return name;
    }

    public String getInfo() {
        return info;
    }

    public boolean getIsFavourite() { return isFavourite; }

}
