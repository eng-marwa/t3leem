package com.salim.ta3limes.Models.response;

import java.util.List;

public class GovernorateResponse {

    private GovernorateList data;

    public GovernorateList getData() {
        return data;
    }

    public class GovernorateList{
        private List<Governorate> governorate;


        public List<Governorate> getGovernorate() {
            return governorate;
        }
    }
    public class Governorate{
       private int id;
       private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
}
