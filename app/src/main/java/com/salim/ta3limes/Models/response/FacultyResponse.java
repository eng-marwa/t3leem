package com.salim.ta3limes.Models.response;

import java.util.List;

public class FacultyResponse {

    private FacultyList data;

    public FacultyList getData() {
        return data;
    }

    public class FacultyList{
        private List<Faculties> Faculties;


        public List<Faculties> getFaculties() {
            return Faculties;
        }
    }
    public class Faculties{
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
