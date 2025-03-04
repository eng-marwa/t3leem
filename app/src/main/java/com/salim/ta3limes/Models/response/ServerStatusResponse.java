package com.salim.ta3limes.Models.response;

import java.util.List;

public class ServerStatusResponse {
    public class Data{

        public String domain;
    }

    public class Root{
        public ServerStatusResponse.Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }
}

