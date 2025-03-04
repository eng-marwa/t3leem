package com.salim.ta3limes.Models.error;

import com.salim.ta3limes.Models.response.ModelBaseResponse;

import java.util.List;

public class ErrorModelResponse extends ModelBaseResponse {

    /**
     * data : {}
     * message : بيانات الدخول غير صحيحة .. يرجي التأكد من البيانات
     * errors : []
     * status : error
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
