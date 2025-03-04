package com.salim.ta3limes.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;

public class SharedPreferencesUtilities {
public static String BASE_URl;
    SharedPreferences sharedPreferences;

    public SharedPreferencesUtilities(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }    ///// create the key name for what you want to save in shared

    private static String TOKEN = "token";

    private static String REG_TOKEN = "regtoken";

    private static String Huawie_TOKEN = "huawietoken";

    private static String STATUS = "status";

    private static String TYPE = "type";

    private static String ISLOGGEDIN = "loggedIn";

    private static String EMAIL = "email";

    private static String PASSWORD = "password";

    private static String PHONE = "phone";

    private static String CENTER_ID = "city_id";

    private static String GOVERNORATE_ID = "governorate_id";

    private static String GOVERNORATE_NAME = "governorate_name";

    private static String CITY_NAME = "city_name";

    private static String PRICE = "price";


    private static String USER_NAME = "username";

    private static String NURSES_SERVICES = "nurses_services";

    private static String XRAYS_SERVICES = "xrays_services";

    private static String OPTICAL_SERVICES = "opticals_services";

    private static String USER_ADDRESS = "useraddress";

    private static String USER_Place = "userplace";

    private static String PROFILE_STATUS = "profilestatus";

    private static String USER_SERVICES = "userservices";

    private static String USER_EXPERIENCE = "userexperience";

    private static String USER_GENDER = "usergender";

    private static String USER_ID = "userid";

    private static String PROFILE_IMAGE = "profileimage";

    private static String IDENTITY_IMAGE = "identityimage";

    private static String DEVICE_ID = "device_id";

    private static String FILE_ID = "file_id";

    private static String PDF_CODE = "pdf_code";

    private static String MP3_CODE = "mp3_code";

    private static Boolean IS_VERIVIED = false;

    /// now create a setter and getter to edit the value and to get it

    public void setUserName(String userName) {
        sharedPreferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void setNursesServices(String nursesServices) {
        sharedPreferences.edit().putString(NURSES_SERVICES, nursesServices).apply();
    }

    public String getNursesServices() {
        return sharedPreferences.getString(NURSES_SERVICES, null);
    }

    public void setXraysServices(String xraysServices) {
        sharedPreferences.edit().putString(XRAYS_SERVICES, xraysServices).apply();
    }

    public String getXraysServices() {
        return sharedPreferences.getString(XRAYS_SERVICES, null);
    }

    public void setOpticalServices(String opticalServices){
        sharedPreferences.edit().putString(OPTICAL_SERVICES, opticalServices).apply();
    }

    public String getOpticalServices(){
        return sharedPreferences.getString(OPTICAL_SERVICES, null);
    }

    public void setProfileStatus(String profileStatus) {
        sharedPreferences.edit().putString(PROFILE_STATUS, profileStatus).apply();
    }

    public String getProfileStatus() {
        return sharedPreferences.getString(PROFILE_STATUS, null);
    }

    public void setTYPE(String type){
        sharedPreferences.edit().putString(TYPE , type).apply();
    }

    public String getTYPE(){
        return sharedPreferences.getString(TYPE, null);
    }

    public void setUserAddress(String userAddress) {
        sharedPreferences.edit().putString(USER_ADDRESS, userAddress).apply();
    }

    public String getUserAddress() {
        return sharedPreferences.getString(USER_ADDRESS, null);
    }

    public void setUSER_Place(String userPlace) {
        sharedPreferences.edit().putString(USER_Place, userPlace).apply();
    }

    public String getUSER_Place() {
        return sharedPreferences.getString(USER_Place, null);
    }

    public void setUserServices(String userServices) {
        sharedPreferences.edit().putString(USER_SERVICES, userServices).apply();
    }

    public String getUserServices() {
        return sharedPreferences.getString(USER_SERVICES, null);
    }

    public void setUserExperience(String userExperience) {
        sharedPreferences.edit().putString(USER_EXPERIENCE, userExperience).apply();
    }

    public String getUserExperience() {
        return sharedPreferences.getString(USER_EXPERIENCE, null);
    }

    public void setUserGender(String userGender) {
        sharedPreferences.edit().putString(USER_GENDER, userGender).apply();
    }

    public String getUserGender() {
        return sharedPreferences.getString(USER_GENDER, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void setUserId(String id) {
        sharedPreferences.edit().putString(USER_ID, id).apply();
    }

    public void setUserAirbodsStatus(int i) {
        sharedPreferences.edit().putInt("airbods", i).apply();
    }

    public int getUserAirbodsStatus() {
        return sharedPreferences.getInt("airbods",0);
    }
    public void setCourseAirbodsStatus(int i) {
        sharedPreferences.edit().putInt("airbodsCourse", i).apply();
    }

    public int getCourseAirbodsStatus() {
        return sharedPreferences.getInt("airbodsCourse",0);
    }
    public void setPASSWORD(String password) {
        sharedPreferences.edit().putString(PASSWORD, password).apply();
    }

    public String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public String getPHONE() {
        return sharedPreferences.getString(PHONE, null);
    }

    public void setPHONE(String phone) {
        sharedPreferences.edit().putString(PHONE, phone).apply();
    }

    public void setCenterId(String centerId) {
        sharedPreferences.edit().putString(CENTER_ID, centerId).apply();
    }

    public String getCenterId() {
        return sharedPreferences.getString(CENTER_ID, null);
    }

    public void setGovernorateId(String governorateId) {
        sharedPreferences.edit().putString(GOVERNORATE_ID, governorateId).apply();
    }

    public String getGovernorateId() {
        return sharedPreferences.getString(GOVERNORATE_ID, null);
    }

    public void setGovernorateName(String governorateName) {
        sharedPreferences.edit().putString(GOVERNORATE_NAME, governorateName).apply();
    }

    public String getGovernorateName() {
        return sharedPreferences.getString(GOVERNORATE_NAME, null);
    }

    public void setCityName(String cityName) {
        sharedPreferences.edit().putString(CITY_NAME, cityName).apply();
    }

    public String getCityName() {
        return sharedPreferences.getString(CITY_NAME, null);
    }

    public String getPRICE() {
        return sharedPreferences.getString(PRICE, null);
    }

    public void setPRICE(String price) {
        sharedPreferences.edit().putString(PRICE, price).apply();
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void setToken(String token) {
        //  key  , value
        sharedPreferences.edit().putString(TOKEN, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, null);
    }

    public void setRegToken(String regToken) {
        //  key  , value
        sharedPreferences.edit().putString(REG_TOKEN, regToken).apply();
    }

    public String getRegToken() {
        return sharedPreferences.getString(REG_TOKEN, null);
    }

    public void setHuawie_TOKEN(String regToken) {
        //  key  , value
        sharedPreferences.edit().putString(Huawie_TOKEN, regToken).apply();
    }

    public String getHuawie_TOKEN() {
        return sharedPreferences.getString(Huawie_TOKEN, null);
    }

    public String getSTATUS() {
        return sharedPreferences.getString(STATUS, null);
    }

    public void setSTATUS(String status) {
        sharedPreferences.edit().putString(STATUS, status).apply();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        //  key  , value
        sharedPreferences.edit().putBoolean(ISLOGGEDIN, isLoggedIn).apply();
    }

    public boolean getLoggedIn() {
        return sharedPreferences.getBoolean(ISLOGGEDIN, false);
    }

    public void setPROFILEIMAGE(String profileImage) {
        sharedPreferences.edit().putString(PROFILE_IMAGE, profileImage).apply();
    }

    public String getPROFILEIMAGE() {
        return sharedPreferences.getString(PROFILE_IMAGE, null);
    }

    public void setIdentityImage(String identityImage) {
        sharedPreferences.edit().putString(IDENTITY_IMAGE, identityImage).apply();
    }

    public String getIdentityImage() {
        return sharedPreferences.getString(IDENTITY_IMAGE, null);
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString(DEVICE_ID, deviceId).apply();
    }
    public void setBaseUrl(String baseUrl) {
        sharedPreferences.edit().putString("BaseUrl", baseUrl).apply();
    }
    public String getBaseUrl() {
        return sharedPreferences.getString("BaseUrl", null);
    }
    public String getDeviceId() {
        return sharedPreferences.getString(DEVICE_ID, null);
    }

public void setFileId(String fileId) {
        sharedPreferences.edit().putString(FILE_ID, fileId).apply();
    }

    public String getFileId() {
        return sharedPreferences.getString(FILE_ID, null);
    }

    public void setPdfCode(String pdfCode){
        sharedPreferences.edit().putString(PDF_CODE, pdfCode).apply();
    }
    public String getPdfCode(){
        return sharedPreferences.getString(PDF_CODE, null);
    }

    public void setMp3Code(String mp3Code){
        sharedPreferences.edit().putString(MP3_CODE, mp3Code).apply();
    }

    public String getMp3Code(){
        return sharedPreferences.getString(MP3_CODE, null);
    }

    public void setIsVerivied (Boolean isVerivied){
        sharedPreferences.edit().putBoolean("IS_VERIVIED", isVerivied);
    }

    public Boolean getIsVerivied(){
        return sharedPreferences.getBoolean("IS_VERIVIED", false);
    }

}
