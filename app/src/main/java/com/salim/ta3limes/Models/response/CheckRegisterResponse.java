package com.salim.ta3limes.Models.response;

import java.util.List;

public class CheckRegisterResponse {
    public class Data{
        public boolean enable;
    }

    public class Root{
        public CheckRegisterResponse.Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }
}

