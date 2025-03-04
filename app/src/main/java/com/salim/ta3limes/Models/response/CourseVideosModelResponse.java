package com.salim.ta3limes.Models.response;

import java.util.Date;
import java.util.List;

public class CourseVideosModelResponse {

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
        public String url;
        public String webview_url;
        public int views;
    }

    public class Files{
        public int id;
        public String file;
        public String notes;
        public int user_id;
        public String title;
        public String download;
        public int lesson_id;
        public Date created_at;
        public Date updated_at;
        public String fileUrl;
    }

    public class Lesson{
        public int id;
        public String title;
        public int course_id;
        public String description;
        public int user_id;
        public Date created_at;
        public Date updated_at;
        public String position;
        public String createAtrForHuman;
        public Videos videos;
        public Files files;
    }

    public class Suggestlessone{
        public int id;
        public String name;
        public String image;
        public String teacher;
        public String phone;
        public String organisation;
    }

    public class Data{
        public List<Lesson> lessons;
        public List<Suggestlessone> suggestlessone;
    }

    public class Root{
        public Data data;
        public String message;
        public List<Object> errors;
        public String status;
    }
//    /**
//     * data : {"lessons":[{"id":1,"title":"Egypt","course_id":1,"description":"Cum voluptatum excep","user_id":1,"created_at":"2020-11-11T14:31:57.000000Z","updated_at":"2020-11-11T14:31:57.000000Z","CreateAtrForHuman":"منذ يوم","videos":{"id":3,"name":"Abdelrahman","description":"Cum voluptatum excep","image":"public/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png","lesson_id":1,"maximum_views_allowed":136,"video_id":"478065365","duration":"0:06","created_at":"2020-11-11T15:09:11.000000Z","updated_at":"2020-11-11T15:09:11.000000Z","imagee":"https://t3lim-es.com/storage/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png"}}]}
//     * message :
//     * errors : []
//     * status : success
//     */
//
//    private DataBean data;
//    private String message;
//    private String status;
//    private List<?> errors;
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public List<?> getErrors() {
//        return errors;
//    }
//
//    public void setErrors(List<?> errors) {
//        this.errors = errors;
//    }
//
//    public static class DataBean {
//        private List<LessonsBean> lessons;
//
//        public List<LessonsBean> getLessons() {
//            return lessons;
//        }
//
//        public void setLessons(List<LessonsBean> lessons) {
//            this.lessons = lessons;
//        }
//
//        public static class LessonsBean {
//            /**
//             * id : 1
//             * title : Egypt
//             * course_id : 1
//             * description : Cum voluptatum excep
//             * user_id : 1
//             * created_at : 2020-11-11T14:31:57.000000Z
//             * updated_at : 2020-11-11T14:31:57.000000Z
//             * CreateAtrForHuman : منذ يوم
//             * videos : {"id":3,"name":"Abdelrahman","description":"Cum voluptatum excep","image":"public/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png","lesson_id":1,"maximum_views_allowed":136,"video_id":"478065365","duration":"0:06","created_at":"2020-11-11T15:09:11.000000Z","updated_at":"2020-11-11T15:09:11.000000Z","imagee":"https://t3lim-es.com/storage/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png"}
//             * files : {"id": 20,"file": "public/lessons/files/JmWi0KFn6a1Hl5RiSSNRmBMiJACn0sXgaVIWIMu8.pdf","notes": "Dr.dalia","user_id": 9,"title": "Antipsychotic","download": "yes","lesson_id": 27,"created_at": "2020-11-24T22:00:41.000000Z","updated_at": "2020-11-24T22:00:41.000000Z","fileUrl": "https://t3lim-es.com/storage/uploads/courses/files/public/lessons/files/JmWi0KFn6a1Hl5RiSSNRmBMiJACn0sXgaVIWIMu8.pdf"}
//             */
//
//            private int id;
//            private String title;
//            private int course_id;
//            private String description;
//            private int user_id;
//            private String created_at;
//            private String updated_at;
//            private String CreateAtrForHuman;
//            private VideosBean videos;
//            private FilesBean files;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public int getCourse_id() {
//                return course_id;
//            }
//
//            public void setCourse_id(int course_id) {
//                this.course_id = course_id;
//            }
//
//            public String getDescription() {
//                return description;
//            }
//
//            public void setDescription(String description) {
//                this.description = description;
//            }
//
//            public int getUser_id() {
//                return user_id;
//            }
//
//            public void setUser_id(int user_id) {
//                this.user_id = user_id;
//            }
//
//            public String getCreated_at() {
//                return created_at;
//            }
//
//            public void setCreated_at(String created_at) {
//                this.created_at = created_at;
//            }
//
//            public String getUpdated_at() {
//                return updated_at;
//            }
//
//            public void setUpdated_at(String updated_at) {
//                this.updated_at = updated_at;
//            }
//
//            public String getCreateAtrForHuman() {
//                return CreateAtrForHuman;
//            }
//
//            public void setCreateAtrForHuman(String CreateAtrForHuman) {
//                this.CreateAtrForHuman = CreateAtrForHuman;
//            }
//
//            public VideosBean getVideos() {
//                return videos;
//            }
//
//            public void setVideos(VideosBean videos) {
//                this.videos = videos;
//            }
//
//            public static class VideosBean {
//                /**
//                 * id : 3
//                 * name : Abdelrahman
//                 * description : Cum voluptatum excep
//                 * image : public/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png
//                 * lesson_id : 1
//                 * maximum_views_allowed : 136
//                 * video_id : 478065365
//                 * duration : 0:06
//                 * created_at : 2020-11-11T15:09:11.000000Z
//                 * updated_at : 2020-11-11T15:09:11.000000Z
//                 * imagee : https://t3lim-es.com/storage/lessons_videos/images/nqAfbHiQARxLeSA7KzdVlPYtSRrX3LP4mJ46otZw.png
//                 */
//
//                private int id;
//                private String name;
//                private String description;
//                private String image;
//                private int lesson_id;
//                private int views;
//                private String video_id;
//                private String duration;
//                private String created_at;
//                private String updated_at;
//                private String imagee;
//
//                public int getId() {
//                    return id;
//                }
//
//                public void setId(int id) {
//                    this.id = id;
//                }
//
//                public String getName() {
//                    return name;
//                }
//
//                public void setName(String name) {
//                    this.name = name;
//                }
//
//                public String getDescription() {
//                    return description;
//                }
//
//                public void setDescription(String description) {
//                    this.description = description;
//                }
//
//                public String getImage() {
//                    return image;
//                }
//
//                public void setImage(String image) {
//                    this.image = image;
//                }
//
//                public int getLesson_id() {
//                    return lesson_id;
//                }
//
//                public void setLesson_id(int lesson_id) {
//                    this.lesson_id = lesson_id;
//                }
//
//                public int getViews() {
//                    return views;
//                }
//
//                public void setViews(int views) {
//                    this.views = views;
//                }
//
//                public String getVideo_id() {
//                    return video_id;
//                }
//
//                public void setVideo_id(String video_id) {
//                    this.video_id = video_id;
//                }
//
//                public String getDuration() {
//                    return duration;
//                }
//
//                public void setDuration(String duration) {
//                    this.duration = duration;
//                }
//
//                public String getCreated_at() {
//                    return created_at;
//                }
//
//                public void setCreated_at(String created_at) {
//                    this.created_at = created_at;
//                }
//
//                public String getUpdated_at() {
//                    return updated_at;
//                }
//
//                public void setUpdated_at(String updated_at) {
//                    this.updated_at = updated_at;
//                }
//
//                public String getImagee() {
//                    return imagee;
//                }
//
//                public void setImagee(String imagee) {
//                    this.imagee = imagee;
//                }
//            }
//
//            public FilesBean getFilesBean() {
//                return files;
//            }
//
//            public void setFilesBean(FilesBean files) {
//                this.files = files;
//            }
//
//            public static class FilesBean {
//
//                private int id;
//                private String file;
//                private String notes;
//                private int user_id;
//                private String title;
//                private String download;
//                private int lesson_id;
//                private String created_at;
//                private String updated_at;
//                private String fileUrl;
//
//                public int getId() {
//                    return id;
//                }
//
//                public void setId(int id) {
//                    this.id = id;
//                }
//
//                public String getFile() {
//                    return file;
//                }
//
//                public void setFile(String file) {
//                    this.file = file;
//                }
//
//                public String getNotes() {
//                    return notes;
//                }
//
//                public void setNotes(String notes) {
//                    this.notes = notes;
//                }
//
//                public String getTitle() {
//                    return title;
//                }
//
//                public void setTitle(String title) {
//                    this.title = title;
//                }
//
//                public int getUser_id() {
//                    return user_id;
//                }
//
//                public void setUser_id(int user_id) {
//                    this.user_id = user_id;
//                }
//
//                public int getLesson_id() {
//                    return lesson_id;
//                }
//
//                public void setLesson_id(int lesson_id) {
//                    this.lesson_id = lesson_id;
//                }
//
//                public String getDownload() {
//                    return download;
//                }
//
//                public void setDownload(String download) {
//                    this.download = download;
//                }
//
//                public String getCreated_at() {
//                    return created_at;
//                }
//
//                public void setCreated_at(String created_at) {
//                    this.created_at = created_at;
//                }
//
//                public String getUpdated_at() {
//                    return updated_at;
//                }
//
//                public void setUpdated_at(String updated_at) {
//                    this.updated_at = updated_at;
//                }
//
//                public String getFileUrl() {
//                    return fileUrl;
//                }
//
//                public void setFileUrl(String fileUrl) {
//                    this.fileUrl = fileUrl;
//                }
//            }
//        }
//    }
}
