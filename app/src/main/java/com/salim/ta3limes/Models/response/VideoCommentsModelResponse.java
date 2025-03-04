package com.salim.ta3limes.Models.response;

import java.util.List;

public class VideoCommentsModelResponse extends ModelBaseResponse {

    /**
     * data : {"video":{"id":3,"videoID":"478065365"},"comments":[{"id":8,"content":"https://t3lim-es.com/storage/uploads/voices/1605184085recording-2020-11-12-14-27-46.m4a","type":"voice","date":"منذ 9 ساعات","student":{"id":1,"name":"mrmarchone","picture":"https://t3lim-es.com/storage/students/pictures/GCzvmoZ8Yt6VRxYuPbMLi18VJGynLKFAdoEc5kNc.png"}},{"id":10,"content":"كومنت","type":"text","date":"منذ ساعتين","student":{"id":2,"name":"dofesan@mailinator.com","picture":"https://t3lim-es.com/storage/students/pictures/nBeV2IiAuCdfI9miwUigJweKpwpLjetkBsQLidVN.jpeg"}}]}
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
         * video : {"id":3,"videoID":"478065365"}
         * comments : [{"id":8,"content":"https://t3lim-es.com/storage/uploads/voices/1605184085recording-2020-11-12-14-27-46.m4a","type":"voice","date":"منذ 9 ساعات","student":{"id":1,"name":"mrmarchone","picture":"https://t3lim-es.com/storage/students/pictures/GCzvmoZ8Yt6VRxYuPbMLi18VJGynLKFAdoEc5kNc.png"}},{"id":10,"content":"كومنت","type":"text","date":"منذ ساعتين","student":{"id":2,"name":"dofesan@mailinator.com","picture":"https://t3lim-es.com/storage/students/pictures/nBeV2IiAuCdfI9miwUigJweKpwpLjetkBsQLidVN.jpeg"}}]
         */

        private VideoBean video;
        private List<CommentsBean> comments;

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class VideoBean {
            /**
             * id : 3
             * videoID : 478065365
             */

            private int id;
            private String videoID;
            private String max_views;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getVideoID() {
                return videoID;
            }

            public void setVideoID(String videoID) {
                this.videoID = videoID;
            }

            public String getMax_views() {
                return max_views;
            }

            public void setMax_views(String max_views) {
                this.max_views = max_views;
            }
        }

        public static class CommentsBean {
            /**
             * id : 8
             * content : https://t3lim-es.com/storage/uploads/voices/1605184085recording-2020-11-12-14-27-46.m4a
             * type : voice
             * date : منذ 9 ساعات
             * student : {"id":1,"name":"mrmarchone","picture":"https://t3lim-es.com/storage/students/pictures/GCzvmoZ8Yt6VRxYuPbMLi18VJGynLKFAdoEc5kNc.png"}
             */

            private int id;
            private String content;
            private String type;
            private String date;
            private StudentBean student;
            private int status;

            public int getStatus() {
                return status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public StudentBean getStudent() {
                return student;
            }

            public void setStudent(StudentBean student) {
                this.student = student;
            }

            public static class StudentBean {
                /**
                 * id : 1
                 * name : mrmarchone
                 * picture : https://t3lim-es.com/storage/students/pictures/GCzvmoZ8Yt6VRxYuPbMLi18VJGynLKFAdoEc5kNc.png
                 */

                private int id;
                private String name;
                private String picture;

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

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }
            }
        }
    }
}
