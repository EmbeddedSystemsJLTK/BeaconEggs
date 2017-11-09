package com.oamk.beaconeggs;

import java.util.ArrayList;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuFetcher {
    public ArrayList<LunchMenuItem> fetchLunchMenu(String link){
        ArrayList<LunchMenuItem> mockuplist = new ArrayList<>();
        mockuplist.add(new LunchMenuItem("VEGETABLE LUNCH", "Cauliflower and manchego patties", "Yoghurt and honey sauce"));
        return mockuplist;
    }
}
