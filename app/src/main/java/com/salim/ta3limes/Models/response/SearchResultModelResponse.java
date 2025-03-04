package com.salim.ta3limes.Models.response;

import java.util.Date;
import java.util.List;

public class SearchResultModelResponse {

    public class Videos{
        public int id;
        public String name;
        public String description;
        public String image;
        public int lesson_id;
        public int maximum_views_allowed;
        public String video_id;
        public String duration;
        public Date created_at;
        public Date updated_at;
        public String imagee;
        public int views;
    }

    public class Result{
        public int id;
        public String name;
        public String phone;
        public String whatsapp;
        public int student_count;
        public String file;
        public String voice_id;
        public String download;
        public Videos videos;
    }

    public class Root{
        public List<Result> results;
    }

}
