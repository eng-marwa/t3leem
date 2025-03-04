package com.salim.ta3limes.Models.response;

import java.util.List;

public class CourseMp3ListFiles {

    public class Lessone{
        public int id;
        public String name;
        public String voice_id;
        public String create;
        public String type;
    }

    public class Lessons{
        public int id;
        public String course;
        public List<Lessone> lessone;
    }

    public class Data{
        public Lessons lessons;
    }

    public class Root{
        public Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }
}
