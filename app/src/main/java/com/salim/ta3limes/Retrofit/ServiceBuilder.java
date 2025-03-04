package com.salim.ta3limes.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    SharedPreferencesUtilities preferencesUtilities;
    public Retrofit retrofit;

    public Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    //    public OkHttpClient client = new OkHttpClient();
    public OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public ServiceBuilder(String baseUrl) {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl+"/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://t3lim-es.com/api/v1/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
    }
    public ServiceInterfaces.Register register(){
        return retrofit.create(ServiceInterfaces.Register.class);
    }
    public ServiceInterfaces.getSpecialization getSpecialization(){
        return retrofit.create(ServiceInterfaces.getSpecialization.class);
    }
    public ServiceInterfaces.getFaculties getFaculties(){
        return retrofit.create(ServiceInterfaces.getFaculties.class);
    }
    public ServiceInterfaces.getGovernorate getGovernorate(){
        return retrofit.create(ServiceInterfaces.getGovernorate.class);
    }
    public ServiceInterfaces.Start getSLider() {
        return retrofit.create(ServiceInterfaces.Start.class);
    }
    public ServiceInterfaces.DeleteComment DeleteComment() {
        return retrofit.create(ServiceInterfaces.DeleteComment.class);
    }
    public ServiceInterfaces.EditComment EditComment() {
        return retrofit.create(ServiceInterfaces.EditComment.class);
    }
    public ServiceInterfaces.LoginUser loginUser() {
        return retrofit.create(ServiceInterfaces.LoginUser.class);
    }

    public ServiceInterfaces.UpdateFireBaseToken updateFireBaseToken() {
        return retrofit.create(ServiceInterfaces.UpdateFireBaseToken.class);
    }

    public ServiceInterfaces.LogOutUser logOutUser() {
        return retrofit.create(ServiceInterfaces.LogOutUser.class);
    }

    public ServiceInterfaces.GetTerms getTerms() {
        return retrofit.create(ServiceInterfaces.GetTerms.class);
    }

    public ServiceInterfaces.GetAboutUs getAboutUs() {
        return retrofit.create(ServiceInterfaces.GetAboutUs.class);
    }

    public ServiceInterfaces.GetInfo getInfo() {
        return retrofit.create(ServiceInterfaces.GetInfo.class);
    }

    public ServiceInterfaces.EditData editData(){
        return retrofit.create(ServiceInterfaces.EditData.class);
    }

    public ServiceInterfaces.SendComplain sendComplain() {
        return retrofit.create(ServiceInterfaces.SendComplain.class);
    }

    public ServiceInterfaces.GetNotification getNotification() {
        return retrofit.create(ServiceInterfaces.GetNotification.class);
    }

    public ServiceInterfaces.ChangePassword changePassword() {
        return retrofit.create(ServiceInterfaces.ChangePassword.class);
    }

    public ServiceInterfaces.GetCenterCourses getCenterCourses() {
        return retrofit.create(ServiceInterfaces.GetCenterCourses.class);
    }

    public ServiceInterfaces.GetCourseVideos getCourseVideos() {
        return retrofit.create(ServiceInterfaces.GetCourseVideos.class);
    }

    public ServiceInterfaces.GetCourseData getCourseData() {
        return retrofit.create(ServiceInterfaces.GetCourseData.class);
    }

    public ServiceInterfaces.GetCoursePDFs getCoursePDFs() {
        return retrofit.create(ServiceInterfaces.GetCoursePDFs.class);
    }

    public ServiceInterfaces.GetVideoDetails getVideoDetails() {
        return retrofit.create(ServiceInterfaces.GetVideoDetails.class);
    }

    public ServiceInterfaces.GetVideoPDF getVideoPDF() {
        return retrofit.create(ServiceInterfaces.GetVideoPDF.class);
    }

    public ServiceInterfaces.DownloadPDF downLoadPDF() {
        return retrofit.create(ServiceInterfaces.DownloadPDF.class);
    }

    public ServiceInterfaces.PostComment postComment() {
        return retrofit.create(ServiceInterfaces.PostComment.class);
    } public ServiceInterfaces.PostComment2 postComment2() {
        return retrofit.create(ServiceInterfaces.PostComment2.class);
    }

    public ServiceInterfaces.GetLiveComments getLiveComments() {
        return retrofit.create(ServiceInterfaces.GetLiveComments.class);
    }

    public ServiceInterfaces.PostLiveComment postLiveComment() {
        return retrofit.create(ServiceInterfaces.PostLiveComment.class);
    }

    public ServiceInterfaces.ForgetPassword forgetPassword() {
        return retrofit.create(ServiceInterfaces.ForgetPassword.class);
    }

    public ServiceInterfaces.ResetPassword resetPassword() {
        return retrofit.create(ServiceInterfaces.ResetPassword.class);
    }

    public ServiceInterfaces.Search search (){
        return retrofit.create(ServiceInterfaces.Search.class);
    }

    public ServiceInterfaces.GetMP3Files getMP3Files(){
        return retrofit.create(ServiceInterfaces.GetMP3Files.class);
    }

    public ServiceInterfaces.GetSuggesstionCourse getSuggesstionCourse(){
        return retrofit.create(ServiceInterfaces.GetSuggesstionCourse.class);
    }

    public ServiceInterfaces.GetLibrary getLibrary(){
        return retrofit.create(ServiceInterfaces.GetLibrary.class);
    }

    public ServiceInterfaces.GetCoursePDF getCoursePDF(){
        return retrofit.create(ServiceInterfaces.GetCoursePDF.class);
    }

    public ServiceInterfaces.GetCourseMp3Files getCourseMp3Files(){
        return retrofit.create(ServiceInterfaces.GetCourseMp3Files.class);
    }

    public ServiceInterfaces.UserBlocked userBlocked(){
        return retrofit.create(ServiceInterfaces.UserBlocked.class);
    }
    public ServiceInterfaces.EnableCreateAccount enableCreateRegister(){
        return retrofit.create(ServiceInterfaces.EnableCreateAccount.class);
    }
    public ServiceInterfaces.ServerStatus serverStatus(){
        return retrofit.create(ServiceInterfaces.ServerStatus.class);
    }
}
