package com.salim.ta3limes.Models.response;

import java.util.List;

public class CenterCoursesModelResponse extends ModelBaseResponse {

    /**
     * data : {"course":[{"id":1,"name":"مقدمه فى علم النفس","starts_at":"Oct 7, 2020","image":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","teacher":"دكتور عماد مشالى ","videos_count":90,"student_count":209,"has_new_videos":false,"new_vides_count":0}],"course_count":1}
     * message :
     * errors : []
     * status : success
     */

    private DataBean data;
    private String message;
    private String status;
    private List<?> errors;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        /**
         * course : [{"id":1,"name":"مقدمه فى علم النفس","starts_at":"Oct 7, 2020","image":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","teacher":"دكتور عماد مشالى ","videos_count":90,"student_count":209,"has_new_videos":false,"new_vides_count":0}]
         * course_count : 1
         */

        private int course_count;
        private List<CourseBean> course;

        public int getCourse_count() {
            return course_count;
        }

        public void setCourse_count(int course_count) {
            this.course_count = course_count;
        }

        public List<CourseBean> getCourse() {
            return course;
        }

        public void setCourse(List<CourseBean> course) {
            this.course = course;
        }

        public static class CourseBean {
            /**
             * id : 1
             * name : مقدمه فى علم النفس
             * starts_at : Oct 7, 2020
             * image : https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg
             * teacher : دكتور عماد مشالى
             * videos_count : 90
             * student_count : 209
             * has_new_videos : false
             * new_vides_count : 0
             */

            private int id;
            private String name;
            private String starts_at;
            private String image;
            private String teacher;
            private int videos_count;
            private int student_count=-1;
            private boolean has_new_videos;
            private int new_vides_count;
             private int airbods;

            public int getAirbods() {
                return airbods;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStarts_at() {
                return starts_at;
            }

            public void setStarts_at(String starts_at) {
                this.starts_at = starts_at;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public int getVideos_count() {
                return videos_count;
            }

            public void setVideos_count(int videos_count) {
                this.videos_count = videos_count;
            }

            public int getStudent_count() {
                return student_count;
            }

            public void setStudent_count(int student_count) {
                this.student_count = student_count;
            }

            public boolean isHas_new_videos() {
                return has_new_videos;
            }

            public void setHas_new_videos(boolean has_new_videos) {
                this.has_new_videos = has_new_videos;
            }

            public int getNew_vides_count() {
                return new_vides_count;
            }

            public void setNew_vides_count(int new_vides_count) {
                this.new_vides_count = new_vides_count;
            }
        }
    }
}
