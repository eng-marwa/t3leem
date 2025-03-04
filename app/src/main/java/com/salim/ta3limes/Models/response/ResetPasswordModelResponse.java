package com.salim.ta3limes.Models.response;

import java.util.List;

public class ResetPasswordModelResponse extends ModelBaseResponse {

    /**
     * data : {}
     * message : تم اعداه تعيين كلمة المرور بنجاح.....قم بتسجيل الدخول الان
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
    }
}
