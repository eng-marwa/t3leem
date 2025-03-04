package com.salim.ta3limes.Models.response;

import java.util.List;

public class VideoPDFModlResponse extends ModelBaseResponse {

    /**
     * data : {"file":"https://t3lim-es.com/storage/uploads/courses/files/public/lessons/files/HOgdHhunPeaLgJ6s48co7kSEWQoCwXuFooXTtXU9.pdf"}
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
         * file : https://t3lim-es.com/storage/uploads/courses/files/public/lessons/files/HOgdHhunPeaLgJ6s48co7kSEWQoCwXuFooXTtXU9.pdf
         */

        private String file;

        private boolean download;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public boolean getDownload() {
            return download;
        }

        public void setDownload(boolean download) {
            this.download = download;
        }
    }
}
