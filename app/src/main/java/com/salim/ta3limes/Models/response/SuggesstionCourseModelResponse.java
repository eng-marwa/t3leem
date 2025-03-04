package com.salim.ta3limes.Models.response;

import java.util.List;

public class SuggesstionCourseModelResponse {

    public class Course{
        public int id;
        public String name;
        public String description;
        public String teacher;
        public String image;
        public String video_id;
        public String organisation;
        public String course;
        public String phone;
        public String whatsapp;
    }

    public class Data{
        public Course course;
    }

    public class Root{
        public Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }

}
