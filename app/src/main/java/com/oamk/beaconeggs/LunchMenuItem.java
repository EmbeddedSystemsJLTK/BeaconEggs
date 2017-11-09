package com.oamk.beaconeggs;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuItem {

    private String title;
    private String name;
    private String info;

    public LunchMenuItem(String title, String name, String info){
        this.title = title;
        this.name = name;
        this.info = info;
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

}
