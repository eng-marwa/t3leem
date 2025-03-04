package com.salim.ta3limes.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.salim.ta3limes.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StaticMethods {

    public static final String TAG = "StaticMethods";

    public static final int ERROR_IMAGE_PROFILE_MENU = R.drawable.ic_image;
    public static final int PLACE_HOLDER_IMAGE_PROFILE_MENU = R.drawable.ic_image;

    public static int ERROR_IMAGE_CATEGORY = R.drawable.ic_image;
    public static int PLACE_HOLDER_IMAGE_CATEGORY = R.drawable.ic_image;
    public static int ERROR_IMAGE_CHATTING = R.drawable.ic_image;


    private static String dateTimeFormat = "yyyy-MM-dd";
    private static String dateTimeFormatWithDetails = "dd MMM yyyy, HH:mm";
    private static String timeFormatWith = "HH:mm";


    public static String getLocalLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage();
//        return "ar";
    }

    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    private static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static MultipartBody.Part getPart(String filePath, String param) {
        MultipartBody.Part body = null;
        try {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(
                            MultipartBody.FORM,
                            file
                    );
            body = MultipartBody.Part.createFormData(param, file.getName(), requestFile);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }


    public static String get64BaseStringImage(String path) {
        File imageFile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;


    }

    @SuppressLint("HardwareIds")
    public static String getIMEIDeviceId(Activity context) {
        SharedPreferencesUtilities preferencesUtilities = new SharedPreferencesUtilities(context);
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (preferencesUtilities.getDeviceId() != null) {
                deviceId = preferencesUtilities.getDeviceId();
            } else {
                deviceId = UUID.randomUUID().toString();
                preferencesUtilities.setDeviceId(deviceId);
            }
            Log.e(TAG, "getIMEIDeviceId: deviceId>> " + deviceId);
//            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int mm = checkDevicePermission(context, android.Manifest.permission.READ_PHONE_STATE);
                if (mm == -1) {
//            return "wait for Accept Permissions";
                    return "";
                } else {
                    Log.e("DeviceInformation ", mTelephony.getDeviceId());
                    deviceId = mTelephony.getDeviceId();
                    preferencesUtilities.setDeviceId(deviceId);

                    return deviceId;
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {

                Log.e("DeviceInformation ", mTelephony.getDeviceId());
                deviceId = mTelephony.getDeviceId();
                preferencesUtilities.setDeviceId(deviceId);
            } else {
                Log.e("DeviceInformation ", mTelephony.getDeviceId());
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                preferencesUtilities.setDeviceId(deviceId);

            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }


    public static int checkDevicePermission(Activity context, String serviceName) {   /* Return Device IMEI, this value can't be changed   */
        // ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   /*  Security Check for Android Marshemllo and Higher  */
//            if (ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            if (ContextCompat.checkSelfPermission(context, serviceName) != PackageManager.PERMISSION_GRANTED) {   // Should we show an explanation?
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{serviceName},
                        1);
            }   //  INNER OUTER IF
            else
                return 1;
        }   //  OUTER IF
        else {
            return 0;
        }
        return -1;
    }

    public static String getFilePathFromBitmap(Context context, Bitmap bitmap) {
        //create a file to write bitmap data
        String path = null;
        try {
            File f = new File(context.getCacheDir(), System.currentTimeMillis() + ".png");
            f.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            path = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    public static String getRealPathFromURI(Context mContext, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getImage(String filePath) {
        Bitmap source = BitmapFactory.decodeFile(filePath);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(source, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(source, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(source, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = source;
        }

        return Bitmap.createScaledBitmap(rotatedBitmap, 1000, 1000, true);
    }


    public static Uri getImageUri(Bitmap inImage, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }

    public static String getDate(long time, boolean showDateDetails) {
        Date date = null;
        try {
            date = new Date(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Format format = new SimpleDateFormat(showDateDetails ? dateTimeFormatWithDetails : dateTimeFormat);
        return date != null ? format.format(date) : String.valueOf(time);
    }

    public static String getTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat(timeFormatWith);
        return format.format(date);
    }

}