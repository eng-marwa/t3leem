package com.salim.ta3limes.Models.response;

import java.util.List;

public class PDFsModelResponse extends ModelBaseResponse {

    /**
     * data : {"files":[{"id":3,"title":"ملف المقدمه - مهم","date":"Oct 1, 2020"},{"id":4,"title":"اسئله مهمه فى الماده","date":"Oct 1, 2020"}]}
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
        private List<FilesBean> files;

        public List<FilesBean> getFiles() {
            return files;
        }

        public void setFiles(List<FilesBean> files) {
            this.files = files;
        }

        public static class FilesBean {
            /**
             * id : 3
             * title : ملف المقدمه - مهم
             * date : Oct 1, 2020
             */

            private int id;
            private String title;
            private String date;

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

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
