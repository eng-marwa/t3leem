package com.salim.ta3limes.Models.response;

import java.util.List;

public class MP3FilesModelResponse {

    public class Course{
        public int id;
        public String name;
        public String teacher;
        public String image;
        public String create;
        public int voice_count;
        public String phone;
        public String whatsapp;
        private int airbods;

        public int getAirbods() {
            return airbods;
        }

    }

    public class Data{
        public List<Course> course;

    }

    public class Root{
        public Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }

}
