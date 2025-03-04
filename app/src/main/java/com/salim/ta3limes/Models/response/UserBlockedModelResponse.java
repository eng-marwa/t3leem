package com.salim.ta3limes.Models.response;

import java.util.List;

public class UserBlockedModelResponse {

    public class Data{
        public boolean blocked;
    }

    public class Root{
        public Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }
}
