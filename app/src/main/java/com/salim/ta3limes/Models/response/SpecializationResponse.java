package com.salim.ta3limes.Models.response;

import java.util.List;

public class SpecializationResponse {

    private SpecializationList data;

    public SpecializationList getData() {
        return data;
    }

    public class SpecializationList{
        private List<Specialization> specialization;


        public List<Specialization> getSpecialization() {
            return specialization;
        }
    }
    public class Specialization{
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

