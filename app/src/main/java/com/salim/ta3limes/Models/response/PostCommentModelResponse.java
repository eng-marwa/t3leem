package com.salim.ta3limes.Models.response;

public class PostCommentModelResponse extends ModelBaseResponse {

    /**
     * status : success
     */
   private VideoCommentsModelResponse.DataBean data;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
