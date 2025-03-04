package com.salim.ta3limes.Models.response;

import java.util.List;

public class TermsModelResponse extends ModelBaseResponse {

    /**
     * data : {"terms":{"content":"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\n            tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n            quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo\n            consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n            cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n            proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}}
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
         * terms : {"content":"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\n            tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n            quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo\n            consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n            cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n            proident, sunt in culpa qui officia deserunt mollit anim id est laborum."}
         */

        private TermsBean terms;

        public TermsBean getTerms() {
            return terms;
        }

        public void setTerms(TermsBean terms) {
            this.terms = terms;
        }

        public static class TermsBean {
            /**
             * content : Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
             tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
             quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
             consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
             cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
             proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
             */

            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
