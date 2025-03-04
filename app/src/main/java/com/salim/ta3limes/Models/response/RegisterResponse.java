package com.salim.ta3limes.Models.response;

import java.util.List;

public class RegisterResponse {
    private String message;
    private String status;
     public Error errors;
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    public class Error{
      public List<String> phone;
      public List<String> password;
      public List<String> faculty;
        public List<String> governorate;
      public List<String> specialization;
      public List<String> facebook;

    }
}
