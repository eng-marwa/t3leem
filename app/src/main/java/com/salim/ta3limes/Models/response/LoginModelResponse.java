package com.salim.ta3limes.Models.response;

import java.util.List;

public class LoginModelResponse extends ModelBaseResponse {

    /**
     * data : {"user":{"name":"ahmed sami","picture":"https://t3lim-es.com/storage/uploads/students/slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg","phone":"01014340346","address":"منصوره توريل الامام محمد عبده","stuedntID":"458874125","faculty":{"id":2,"name":"كليه تجاره"},"department":{"id":3,"name":"محاسبه"},"year":1},"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvdDNsaW0tZXMuY29tXC9hcGlcL3YxXC9zdHVkZW50XC9sb2dpbiIsImlhdCI6MTYwMjQyNTUxOCwiZXhwIjo1MjAyNDI1NTE4LCJuYmYiOjE2MDI0MjU1MTgsImp0aSI6InRoTzF2RkplUFFXZFQySm8iLCJzdWIiOjEsInBydiI6IjljNDI5ZTZhNjBjZDUyODU0NzNmMmM4YmM3MDFlYzA5NDhkZjRkOGMifQ.1BuckUw-9s8HNzBfyjFS4ssCyb7Tc5GtsVAtxAmvsMM","centers":[{"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/530270-8I7aa-1556371888-5cc459b0510a5.jpg","course_count":2},{"id":2,"name":"سنتر اجيال","logo":"https://t3lim-es.com/storage/uploads/centers/7854125445998.png","course_count":4}]}
     * message : تم تسجيل الدخول بنجاح
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
         * user : {"name":"ahmed sami","picture":"https://t3lim-es.com/storage/uploads/students/slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg","phone":"01014340346","address":"منصوره توريل الامام محمد عبده","stuedntID":"458874125","faculty":{"id":2,"name":"كليه تجاره"},"department":{"id":3,"name":"محاسبه"},"year":1}
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvdDNsaW0tZXMuY29tXC9hcGlcL3YxXC9zdHVkZW50XC9sb2dpbiIsImlhdCI6MTYwMjQyNTUxOCwiZXhwIjo1MjAyNDI1NTE4LCJuYmYiOjE2MDI0MjU1MTgsImp0aSI6InRoTzF2RkplUFFXZFQySm8iLCJzdWIiOjEsInBydiI6IjljNDI5ZTZhNjBjZDUyODU0NzNmMmM4YmM3MDFlYzA5NDhkZjRkOGMifQ.1BuckUw-9s8HNzBfyjFS4ssCyb7Tc5GtsVAtxAmvsMM
         * centers : [{"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/530270-8I7aa-1556371888-5cc459b0510a5.jpg","course_count":2},{"id":2,"name":"سنتر اجيال","logo":"https://t3lim-es.com/storage/uploads/centers/7854125445998.png","course_count":4}]
         */

        private UserBean user;
        private String token;
        private List<CentersBean> centers;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<CentersBean> getCenters() {
            return centers;
        }

        public void setCenters(List<CentersBean> centers) {
            this.centers = centers;
        }

        public static class UserBean {
            /**
             * name : ahmed sami
             * picture : https://t3lim-es.com/storage/uploads/students/slkqg95v6ncuari23bkbzm140yo8hd7txef16015618935.jpg
             * phone : 01014340346
             * address : منصوره توريل الامام محمد عبده
             * stuedntID : 458874125
             * faculty : {"id":2,"name":"كليه تجاره"}
             * department : {"id":3,"name":"محاسبه"}
             * year : 1
             */

            private String name;
            private String picture;
            private String phone;
            private String password;
            private String address;
            private String stuedntID;
            private FacultyBean faculty;
            private DepartmentBean department;
            private int year;
            private int airbods;

            public int getAirbods() {
                return airbods;
            }
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStuedntID() {
                return stuedntID;
            }

            public void setStuedntID(String stuedntID) {
                this.stuedntID = stuedntID;
            }

            public FacultyBean getFaculty() {
                return faculty;
            }

            public void setFaculty(FacultyBean faculty) {
                this.faculty = faculty;
            }

            public DepartmentBean getDepartment() {
                return department;
            }

            public void setDepartment(DepartmentBean department) {
                this.department = department;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public static class FacultyBean {
                /**
                 * id : 2
                 * name : كليه تجاره
                 */

                private int id;
                private String name;

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
            }

            public static class DepartmentBean {
                /**
                 * id : 3
                 * name : محاسبه
                 */

                private int id;
                private String name;

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
            }
        }

        public static class CentersBean {
            /**
             * id : 1
             * name : everts
             * logo : https://t3lim-es.com/storage/uploads/centers/530270-8I7aa-1556371888-5cc459b0510a5.jpg
             * course_count : 2
             */

            private int id;
            private String name;
            private String logo;
            private int course_count;

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

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getCourse_count() {
                return course_count;
            }

            public void setCourse_count(int course_count) {
                this.course_count = course_count;
            }
        }
    }
}
