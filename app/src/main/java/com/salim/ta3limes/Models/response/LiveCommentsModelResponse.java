package com.salim.ta3limes.Models.response;

import java.util.List;

public class LiveCommentsModelResponse extends ModelBaseResponse {

    /**
     * data : {"comments":[{"id":1,"user_id":2,"video_live_id":1,"comment":"sfdhtrjtgntgfjytkyfdherjn","created_at":"2020-11-07T22:52:22.000000Z","updated_at":"2020-11-07T22:52:22.000000Z","user":{"id":2,"name":"ahmed sami","phone":"01159800211","stuednt_id":"45887412512","password":"$2y$10$q/CtH4ksIbUFYGBmEZvKVOzGU28e4lJXdFFaZv59Efl.y.GPZNdEG","notes":"طالب مجتهد","address":"منصوره توريل الامام محمد عبده","national_id":"012457789965214578","center_id":1,"faculty_id":2,"department_id":3,"year":1,"user_id":1,"firebase_token":"4185296789456123925814986543219877","platform":"ios","picture":"slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg","created_at":"2020-09-09T05:48:15.000000Z","updated_at":"2020-11-07T22:49:15.000000Z","email":"ahmedsamigeek@gmaill.com","allow_to_login":"0"}}]}
     * message : تمت العملية بنجاح
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
        private List<CommentsBean> comments;

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * id : 1
             * user_id : 2
             * video_live_id : 1
             * comment : sfdhtrjtgntgfjytkyfdherjn
             * created_at : 2020-11-07T22:52:22.000000Z
             * updated_at : 2020-11-07T22:52:22.000000Z
             * user : {"id":2,"name":"ahmed sami","phone":"01159800211","stuednt_id":"45887412512","password":"$2y$10$q/CtH4ksIbUFYGBmEZvKVOzGU28e4lJXdFFaZv59Efl.y.GPZNdEG","notes":"طالب مجتهد","address":"منصوره توريل الامام محمد عبده","national_id":"012457789965214578","center_id":1,"faculty_id":2,"department_id":3,"year":1,"user_id":1,"firebase_token":"4185296789456123925814986543219877","platform":"ios","picture":"slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg","created_at":"2020-09-09T05:48:15.000000Z","updated_at":"2020-11-07T22:49:15.000000Z","email":"ahmedsamigeek@gmaill.com","allow_to_login":"0"}
             */

            private int id;
            private int user_id;
            private int video_live_id;
            private String comment;
            private String created_at;
            private String updated_at;
            private UserBean user;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getVideo_live_id() {
                return video_live_id;
            }

            public void setVideo_live_id(int video_live_id) {
                this.video_live_id = video_live_id;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public static class UserBean {
                /**
                 * id : 2
                 * name : ahmed sami
                 * phone : 01159800211
                 * stuednt_id : 45887412512
                 * password : $2y$10$q/CtH4ksIbUFYGBmEZvKVOzGU28e4lJXdFFaZv59Efl.y.GPZNdEG
                 * notes : طالب مجتهد
                 * address : منصوره توريل الامام محمد عبده
                 * national_id : 012457789965214578
                 * center_id : 1
                 * faculty_id : 2
                 * department_id : 3
                 * year : 1
                 * user_id : 1
                 * firebase_token : 4185296789456123925814986543219877
                 * platform : ios
                 * picture : slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg
                 * created_at : 2020-09-09T05:48:15.000000Z
                 * updated_at : 2020-11-07T22:49:15.000000Z
                 * email : ahmedsamigeek@gmaill.com
                 * allow_to_login : 0
                 */

                private int id;
                private String name;
                private String phone;
                private String stuednt_id;
                private String password;
                private String notes;
                private String address;
                private String national_id;
                private int center_id;
                private int faculty_id;
                private int department_id;
                private int year;
                private int user_id;
                private String firebase_token;
                private String platform;
                private String picture;
                private String image;
                private String created_at;
                private String updated_at;
                private String email;
                private String allow_to_login;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getStuednt_id() {
                    return stuednt_id;
                }

                public void setStuednt_id(String stuednt_id) {
                    this.stuednt_id = stuednt_id;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getNotes() {
                    return notes;
                }

                public void setNotes(String notes) {
                    this.notes = notes;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getNational_id() {
                    return national_id;
                }

                public void setNational_id(String national_id) {
                    this.national_id = national_id;
                }

                public int getCenter_id() {
                    return center_id;
                }

                public void setCenter_id(int center_id) {
                    this.center_id = center_id;
                }

                public int getFaculty_id() {
                    return faculty_id;
                }

                public void setFaculty_id(int faculty_id) {
                    this.faculty_id = faculty_id;
                }

                public int getDepartment_id() {
                    return department_id;
                }

                public void setDepartment_id(int department_id) {
                    this.department_id = department_id;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public int getUser_id() {
                    return user_id;
                }

                public void setUser_id(int user_id) {
                    this.user_id = user_id;
                }

                public String getFirebase_token() {
                    return firebase_token;
                }

                public void setFirebase_token(String firebase_token) {
                    this.firebase_token = firebase_token;
                }

                public String getPlatform() {
                    return platform;
                }

                public void setPlatform(String platform) {
                    this.platform = platform;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getAllow_to_login() {
                    return allow_to_login;
                }

                public void setAllow_to_login(String allow_to_login) {
                    this.allow_to_login = allow_to_login;
                }
            }
        }
    }
}
