package com.salim.ta3limes.Models.response;

import java.util.List;

public class NotificationModelResponse {

    /**
     * data : {"notifications":[{"id":"1f577085-ada3-42f3-9b53-8cb4654518b7","type":"App\\Notifications\\LessonFilesNotification","notifiable_type":"App\\Models\\Student","notifiable_id":2,"data":{"id":5,"title":"Ut aperiam tempora p","file":"public/lessons/files/9pi22h3Z2u9VcWuPWAOwuEBWS4H2sfXAyK83sEXA.pdf","lesson_id":"16","notes":"A ut fugit nihil at","duration":"2020 Nov 12","type":"pdf","token":"","channel_name":"","description":"","name":"","video_id":"","maximum_views_allowed":"","course_id":""},"read_at":null,"created_at":"2020-11-12T18:55:39.000000Z","updated_at":"2020-11-12T18:55:39.000000Z"},{"id":"08523c5d-df06-4ef0-9cfc-a8bff26ad7bf","type":"App\\Notifications\\LessonVideoNotification","notifiable_type":"App\\Models\\Student","notifiable_id":2,"data":{"id":7,"name":"Emmanuel Bowers","video_id":"478607410","description":"Repellendus Neque a","lesson_id":"1","duration":"2020 Nov 12","maximum_views_allowed":"20","type":"video","token":"","channel_name":"","file":"","notes":"","title":"","course_id":""},"read_at":null,"created_at":"2020-11-12T18:55:09.000000Z","updated_at":"2020-11-12T18:55:09.000000Z"}]}
     * message :
     * errors : []
     * status : success
     */

    private DataBeanX data;
    private String message;
    private String status;
    private List<?> errors;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
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

    public static class DataBeanX {
        private List<NotificationsBean> notifications;

        public List<NotificationsBean> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<NotificationsBean> notifications) {
            this.notifications = notifications;
        }

        public static class NotificationsBean {
            /**
             * id : 1f577085-ada3-42f3-9b53-8cb4654518b7
             * type : App\Notifications\LessonFilesNotification
             * notifiable_type : App\Models\Student
             * notifiable_id : 2
             * data : {"id":5,"title":"Ut aperiam tempora p","file":"public/lessons/files/9pi22h3Z2u9VcWuPWAOwuEBWS4H2sfXAyK83sEXA.pdf","lesson_id":"16","notes":"A ut fugit nihil at","duration":"2020 Nov 12","type":"pdf","token":"","channel_name":"","description":"","name":"","video_id":"","maximum_views_allowed":"","course_id":""}
             * read_at : null
             * created_at : 2020-11-12T18:55:39.000000Z
             * updated_at : 2020-11-12T18:55:39.000000Z
             */

            private String id;
            private String type;
            private String notifiable_type;
            private int notifiable_id;
            private DataBean data;
            private Object read_at;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNotifiable_type() {
                return notifiable_type;
            }

            public void setNotifiable_type(String notifiable_type) {
                this.notifiable_type = notifiable_type;
            }

            public int getNotifiable_id() {
                return notifiable_id;
            }

            public void setNotifiable_id(int notifiable_id) {
                this.notifiable_id = notifiable_id;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public Object getRead_at() {
                return read_at;
            }

            public void setRead_at(Object read_at) {
                this.read_at = read_at;
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

            public static class DataBean {
                /**
                 * id : 5
                 * title : Ut aperiam tempora p
                 * file : public/lessons/files/9pi22h3Z2u9VcWuPWAOwuEBWS4H2sfXAyK83sEXA.pdf
                 * lesson_id : 16
                 * notes : A ut fugit nihil at
                 * duration : 2020 Nov 12
                 * type : pdf
                 * token :
                 * channel_name :
                 * description :
                 * name :
                 * video_id :
                 * maximum_views_allowed :
                 * course_id :
                 */

                private int id;
                private String title;
                private String file;
                private String lesson_id;
                private String notes;
                private String duration;
                private String type;
                private String token;
                private String channel_name;
                private String description;
                private String name;
                private String video_id;
                private String maximum_views_allowed;
                private String course_id;
                private String zoom_link;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public String getLesson_id() {
                    return lesson_id;
                }

                public void setLesson_id(String lesson_id) {
                    this.lesson_id = lesson_id;
                }

                public String getNotes() {
                    return notes;
                }

                public void setNotes(String notes) {
                    this.notes = notes;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getChannel_name() {
                    return channel_name;
                }

                public void setChannel_name(String channel_name) {
                    this.channel_name = channel_name;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }

                public String getMaximum_views_allowed() {
                    return maximum_views_allowed;
                }

                public void setMaximum_views_allowed(String maximum_views_allowed) {
                    this.maximum_views_allowed = maximum_views_allowed;
                }

                public String getCourse_id() {
                    return course_id;
                }

                public void setCourse_id(String course_id) {
                    this.course_id = course_id;
                }

                public String getZoom_link() {
                    return zoom_link;
                }

                public void setZoom_link(String zoom_link) {
                    this.zoom_link = zoom_link;
                }
            }
        }
    }
}
