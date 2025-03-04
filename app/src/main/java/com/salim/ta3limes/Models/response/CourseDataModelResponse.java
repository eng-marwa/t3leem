package com.salim.ta3limes.Models.response;

import java.util.List;

public class CourseDataModelResponse extends ModelBaseResponse {

    /**
     * data : {"course":{"info":{"id":1,"name":"مقدمه فى علم النفس","starts_at":"Oct 7, 2020","desc":"لوريم إيبسوم(Lorem Ipsum) هو ببساطة نص شكلي (بمعنى أن الغاية هي الشكل وليس المحتوى) ويُستخدم في صناعات المطابع ودور النشر. كان لوريم إيبسوم ولايزال المعيار للنص الشكلي منذ القرن الخامس عشر عندما قامت مطبعة مجهولة برص مجموعة من الأحرف بشكل عشوائي أخذتها من نص، لتكوّن كتيّب بمثابة دليل أو مرجع شكلي لهذه الأحرف. خمسة قرون من الزمن لم تقضي على هذا النص، بل انه حتى صار مستخدماً وبشكله الأصلي في الطباعة والتنضيد الإلكتروني. انتشر بشكل كبير في ستينيّات هذا القرن مع إصدار رقائق \"ليتراسيت\" (Letraset) البلاستيكية تحوي مقاطع من هذا النص، وعاد لينتشر مرة أخرى مؤخراَ مع ظهور برامج النشر الإلكتروني مثل \"ألدوس بايج مايكر\" (Aldus PageMaker) والتي حوت أيضاً على نسخ من نص لوريم إيبسوم.\r\n","teacher":{"id":2,"name":"دكتور عماد مشالى ","picture":"https://t3lim-es.com/storage/uploads/teachers/748373urydh3625etdgsr533.png"},"center":{"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","course_count":1}}}}
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
         * course : {"info":{"id":1,"name":"مقدمه فى علم النفس","starts_at":"Oct 7, 2020","desc":"لوريم إيبسوم(Lorem Ipsum) هو ببساطة نص شكلي (بمعنى أن الغاية هي الشكل وليس المحتوى) ويُستخدم في صناعات المطابع ودور النشر. كان لوريم إيبسوم ولايزال المعيار للنص الشكلي منذ القرن الخامس عشر عندما قامت مطبعة مجهولة برص مجموعة من الأحرف بشكل عشوائي أخذتها من نص، لتكوّن كتيّب بمثابة دليل أو مرجع شكلي لهذه الأحرف. خمسة قرون من الزمن لم تقضي على هذا النص، بل انه حتى صار مستخدماً وبشكله الأصلي في الطباعة والتنضيد الإلكتروني. انتشر بشكل كبير في ستينيّات هذا القرن مع إصدار رقائق \"ليتراسيت\" (Letraset) البلاستيكية تحوي مقاطع من هذا النص، وعاد لينتشر مرة أخرى مؤخراَ مع ظهور برامج النشر الإلكتروني مثل \"ألدوس بايج مايكر\" (Aldus PageMaker) والتي حوت أيضاً على نسخ من نص لوريم إيبسوم.\r\n","teacher":{"id":2,"name":"دكتور عماد مشالى ","picture":"https://t3lim-es.com/storage/uploads/teachers/748373urydh3625etdgsr533.png"},"center":{"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","course_count":1}}}
         */

        private CourseBean course;

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public static class CourseBean {
            /**
             * info : {"id":1,"name":"مقدمه فى علم النفس","starts_at":"Oct 7, 2020","desc":"لوريم إيبسوم(Lorem Ipsum) هو ببساطة نص شكلي (بمعنى أن الغاية هي الشكل وليس المحتوى) ويُستخدم في صناعات المطابع ودور النشر. كان لوريم إيبسوم ولايزال المعيار للنص الشكلي منذ القرن الخامس عشر عندما قامت مطبعة مجهولة برص مجموعة من الأحرف بشكل عشوائي أخذتها من نص، لتكوّن كتيّب بمثابة دليل أو مرجع شكلي لهذه الأحرف. خمسة قرون من الزمن لم تقضي على هذا النص، بل انه حتى صار مستخدماً وبشكله الأصلي في الطباعة والتنضيد الإلكتروني. انتشر بشكل كبير في ستينيّات هذا القرن مع إصدار رقائق \"ليتراسيت\" (Letraset) البلاستيكية تحوي مقاطع من هذا النص، وعاد لينتشر مرة أخرى مؤخراَ مع ظهور برامج النشر الإلكتروني مثل \"ألدوس بايج مايكر\" (Aldus PageMaker) والتي حوت أيضاً على نسخ من نص لوريم إيبسوم.\r\n","teacher":{"id":2,"name":"دكتور عماد مشالى ","picture":"https://t3lim-es.com/storage/uploads/teachers/748373urydh3625etdgsr533.png"},"center":{"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","course_count":1}}
             */

            private InfoBean info;

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * id : 1
                 * name : مقدمه فى علم النفس
                 * starts_at : Oct 7, 2020
                 * desc : لوريم إيبسوم(Lorem Ipsum) هو ببساطة نص شكلي (بمعنى أن الغاية هي الشكل وليس المحتوى) ويُستخدم في صناعات المطابع ودور النشر. كان لوريم إيبسوم ولايزال المعيار للنص الشكلي منذ القرن الخامس عشر عندما قامت مطبعة مجهولة برص مجموعة من الأحرف بشكل عشوائي أخذتها من نص، لتكوّن كتيّب بمثابة دليل أو مرجع شكلي لهذه الأحرف. خمسة قرون من الزمن لم تقضي على هذا النص، بل انه حتى صار مستخدماً وبشكله الأصلي في الطباعة والتنضيد الإلكتروني. انتشر بشكل كبير في ستينيّات هذا القرن مع إصدار رقائق "ليتراسيت" (Letraset) البلاستيكية تحوي مقاطع من هذا النص، وعاد لينتشر مرة أخرى مؤخراَ مع ظهور برامج النشر الإلكتروني مثل "ألدوس بايج مايكر" (Aldus PageMaker) والتي حوت أيضاً على نسخ من نص لوريم إيبسوم.
                 * teacher : {"id":2,"name":"دكتور عماد مشالى ","picture":"https://t3lim-es.com/storage/uploads/teachers/748373urydh3625etdgsr533.png"}
                 * center : {"id":1,"name":"everts","logo":"https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg","course_count":1}
                 */

                private int id;
                private String name;
                private String starts_at;
                private String desc;
                private TeacherBean teacher;
                private CenterBean center;

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

                public String getStarts_at() {
                    return starts_at;
                }

                public void setStarts_at(String starts_at) {
                    this.starts_at = starts_at;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public TeacherBean getTeacher() {
                    return teacher;
                }

                public void setTeacher(TeacherBean teacher) {
                    this.teacher = teacher;
                }

                public CenterBean getCenter() {
                    return center;
                }

                public void setCenter(CenterBean center) {
                    this.center = center;
                }

                public static class TeacherBean {
                    /**
                     * id : 2
                     * name : دكتور عماد مشالى
                     * picture : https://t3lim-es.com/storage/uploads/teachers/748373urydh3625etdgsr533.png
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

                public static class CenterBean {
                    /**
                     * id : 1
                     * name : everts
                     * logo : https://t3lim-es.com/storage/uploads/centers/5cc459b0510a5.jpg
                     * course_count : 1
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
    }
}
