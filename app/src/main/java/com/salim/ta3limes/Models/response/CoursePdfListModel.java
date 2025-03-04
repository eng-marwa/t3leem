package com.salim.ta3limes.Models.response;

import java.util.List;

public class CoursePdfListModel {

    public class Lessone{
        public int id;
        public String file;
        public String name;
        public String download;
        public String voice_id;
        public String create;
        public String type;
    }

    public class Course{
        public int id;
        public String course;
        public List<Lessone> lessone;
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
