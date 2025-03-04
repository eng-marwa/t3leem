package com.salim.ta3limes.Retrofit;

import com.salim.ta3limes.Models.request.EditCommentRequest;
import com.salim.ta3limes.Models.response.AboutUsModelResponse;
import com.salim.ta3limes.Models.response.CenterCoursesModelResponse;
import com.salim.ta3limes.Models.response.ChangePasswordModelResponse;
import com.salim.ta3limes.Models.response.CheckRegisterResponse;
import com.salim.ta3limes.Models.response.CourseDataModelResponse;
import com.salim.ta3limes.Models.response.CourseMp3ListFiles;
import com.salim.ta3limes.Models.response.CoursePdfListModel;
import com.salim.ta3limes.Models.response.CourseVideosModelResponse;
import com.salim.ta3limes.Models.response.DataListResponse;
import com.salim.ta3limes.Models.response.FacultyResponse;
import com.salim.ta3limes.Models.response.ForgetPasswordModelResponse;
import com.salim.ta3limes.Models.response.GovernorateResponse;
import com.salim.ta3limes.Models.response.LibraryModelResponse;
import com.salim.ta3limes.Models.response.LiveCommentsModelResponse;
import com.salim.ta3limes.Models.response.LogOutModelResponse;
import com.salim.ta3limes.Models.response.LoginModelResponse;
import com.salim.ta3limes.Models.response.MP3FilesModelResponse;
import com.salim.ta3limes.Models.response.ModelBaseResponse;
import com.salim.ta3limes.Models.response.NotificationModelResponse;
import com.salim.ta3limes.Models.response.PDFsModelResponse;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.Models.response.PostCommentModelResponse;
import com.salim.ta3limes.Models.response.PostLiveCommentModelResponse;
import com.salim.ta3limes.Models.response.RegisterResponse;
import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.Models.response.SearchResultModelResponse;
import com.salim.ta3limes.Models.response.SendComplainModelResponse;
import com.salim.ta3limes.Models.response.ServerStatusResponse;
import com.salim.ta3limes.Models.response.SpecializationResponse;
import com.salim.ta3limes.Models.response.StartResponseModel;
import com.salim.ta3limes.Models.response.SuggesstionCourseModelResponse;
import com.salim.ta3limes.Models.response.TermsModelResponse;
import com.salim.ta3limes.Models.response.UpdateFireBaseTokenModelResponse;
import com.salim.ta3limes.Models.response.UserBlockedModelResponse;
import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.Models.response.VideoPDFModlResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ServiceInterfaces {

    interface PostComment2 {
        @Multipart
        @POST("student/course/videos/{id}/comments")
        Call<VideoCommentsModelResponse> postComment2(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id
                , @Query("comment_type") String comment_type, @Query("comment") String comment, @Part MultipartBody.Part voiceFile);
    }
    interface Register{
        @Headers({"Accept: application/json"})
        @POST("student/register")
        Call<RegisterResponse> register(@Body RequestBody body);
    }
    interface getSpecialization{
        @GET("student/specialization/{id}")
        Call<SpecializationResponse> getSpecialization(@Path("id") int id);
    }
    interface getFaculties{
        @GET("student/faculty/{id}")
        Call<FacultyResponse> getFaculties(@Path("id") int id);
    }
  interface getGovernorate{
      @GET("student/governorate")
      Call<GovernorateResponse> getGovernorate();
  }
    interface Start{
        @GET("student/slider")
        Call<StartResponseModel> getSlider();
    }
interface EditComment{
    @POST("student/comment/update/{id}")
    Call<VideoCommentsModelResponse> editComment(@Header("Authorization") String authHeader,@Path("id") int id
    ,@Body EditCommentRequest ModelBaseResponse );

}
    interface DeleteComment{
        @GET("student/comment/delete/{id}")
        Call<VideoCommentsModelResponse> deleteComment(@Header("Authorization") String authHeader,@Path("id") int id);
    }

    interface LoginUser {
        @FormUrlEncoded
        @POST("student/login")
        Call<LoginModelResponse> loginUser(@Field("phone") String phone, @Field("password") String password, @Field("serial_number") String serial_number);
    }

    interface UpdateFireBaseToken {
        @POST("student/info/firebase")
        Call<UpdateFireBaseTokenModelResponse> updateToken(@Header("Authorization") String authHeader, @Query("firebase_token") String fireBaseToken,
                                                           @Query("platform") String platForm, @Query("device") String device);
    }

    interface LogOutUser {
        @POST("student/logout")
        Call<LogOutModelResponse> logOutUser(@Header("Authorization") String authHeader);
    }

    interface GetTerms {
        @GET("student/terms")
        Call<TermsModelResponse> getTerms(@Header("Authorization") String authHeader);
    }

    interface GetAboutUs {
        @GET("student/about")
        Call<AboutUsModelResponse> getAboutUs(@Header("Authorization") String authHeader);
    }

    interface GetInfo {
        @GET("student")
        Call<PersonalDataModelResponse> getInfo(@Header("Authorization") String authHeader);
    }

    interface EditData {
        @Multipart
        @POST("student/profile/edit")
        Call<ResetPasswordModelResponse> editData(@Header("Authorization") String authHeader, @Part MultipartBody.Part picture);
    }

    interface SendComplain {
        @FormUrlEncoded
        @POST("student/complaint")
        Call<SendComplainModelResponse> sendComlain(@Header("Authorization") String authHeader, @Field("content") String content);
    }

    interface GetNotification {
        @GET("student/notification/list")
        Call<NotificationModelResponse> getNotifcation(@Header("Authorization") String authHeader);
    }

    interface ChangePassword {
        @FormUrlEncoded
        @POST("student/password/change")
        Call<ChangePasswordModelResponse> changePassword(@Header("Authorization") String authHeader, @Field("current_password") String current_password
                , @Field("password") String password, @Field("password_confirmation") String password_confirmation);
    }

    interface GetCenterCourses {
        @GET("student/centers/{id}/courses")
        Call<CenterCoursesModelResponse> getCenterCourses(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id);
    }

    interface GetCourseVideos {
        @GET("student/courses/{id}/videosss")
        Call<CourseVideosModelResponse.Root> getCourseVideos(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id);
    }

    interface GetCourseData {
        @GET("student/courses/{id}/info")
        Call<CourseDataModelResponse> getCourseData(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id);
    }

    interface GetCoursePDFs {
        @GET("student/courses/{id}/PDFs")
        Call<PDFsModelResponse> getCoursePDFs(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id);
    }

    interface GetVideoDetails {
        @GET("student/course/videos/{id}")
        Call<VideoCommentsModelResponse> getVideoDetails(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id);
    }

    interface GetVideoPDF {
        @GET("student/course/files/{id}")
        Call<VideoPDFModlResponse> getVideoPDF(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) String id);
    }

    interface DownloadPDF {
        @Streaming
        @GET("student/course/files/{id}")
        Call<ResponseBody> downLoadPDF(@Url String fileUrl);
    }

    interface PostComment {
        @Multipart
        @POST("student/course/videos/{id}/comments")
        Call<PostCommentModelResponse> postComment(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) int id
                , @Query("comment_type") String comment_type, @Query("comment") String comment, @Part MultipartBody.Part voiceFile);
    }

    interface GetLiveComments {
        @GET("student/video_comments/{video_live_id}")
        Call<LiveCommentsModelResponse> getLiveComments(@Header("Authorization") String authHeader, @Path(value = "video_live_id", encoded = true) String id);
    }

    interface PostLiveComment {
        @FormUrlEncoded
        @POST("student/video_comments")
        Call<PostLiveCommentModelResponse> postLiveComment(@Header("Authorization") String authHeader, @Field("comment") String comment, @Field("video_live_id") String video_live_id);
    }

    interface ForgetPassword {
        @FormUrlEncoded
        @POST("student/password/forget")
        Call<ForgetPasswordModelResponse> forgetPassword(@Field("email") String email);
    }

    interface ResetPassword {
        @FormUrlEncoded
        @POST("student/password/forget")
        Call<ResetPasswordModelResponse> resetPassword(@Field("email") String email, @Field("code") String code
                , @Field("Password") String password, @Field("password_confirmation") String password_confirmation);
    }

    interface Search {
        @GET("student/search")
        Call<SearchResultModelResponse.Root> search(@Header("Authorization") String authHeader, @Query("query") String query
                , @Query("type") String type);
    }

    interface GetLibrary {
        @GET("student/center/coursesPDF")
        Call<LibraryModelResponse.Root> getLibrary(@Header("Authorization") String authHeader);
    }

    interface GetMP3Files{
        @GET("student/center/coursesvoice")
        Call<MP3FilesModelResponse.Root> getMp3Files(@Header("Authorization") String authHeader);
    }

    interface GetSuggesstionCourse{
        @GET("student/course/{id}/sgcourse")
        Call<SuggesstionCourseModelResponse.Root> getSuggesstionCourse(@Header("Authorization") String authHeader, @Path(value = "id", encoded = true) String id);
    }

    interface GetCoursePDF {
        @GET("student/courses/{id}/PDF")
        Call<CoursePdfListModel.Root> getCoursePdf(@Header("Authorization") String authHeader,
                                                   @Path(value = "id", encoded = true) String id, @Query("code") String code);
    }

    interface GetCourseMp3Files {
        @GET("student/courses/{id}/voice")
        Call<CourseMp3ListFiles.Root> getCourseMp3Files(@Header("Authorization") String authHeader,
                                                        @Path(value = "id", encoded = true) String id, @Query("code") String code);
    }

    interface UserBlocked {
        @POST("student/CheckUser")
        Call<UserBlockedModelResponse.Root> userBlocked(@Header("Authorization") String authHeader,
                                                        @Query("phone") String phone);
    }
    interface EnableCreateAccount {
        @GET("student/enable_creat_account")
        Call<CheckRegisterResponse.Root> enableCreateAccount();
    }
    interface ServerStatus {
        @GET("server_status")
        Call<ServerStatusResponse.Root> serverStatus();
    }
}
