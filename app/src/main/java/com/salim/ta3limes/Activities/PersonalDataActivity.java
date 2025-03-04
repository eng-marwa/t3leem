package com.salim.ta3limes.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Models.response.PersonalDataModelResponse;
import com.salim.ta3limes.Models.response.ResetPasswordModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.StaticMethods;
import com.salim.ta3limes.viewmodels.PersonalDataViewModel;
import com.salim.ta3limes.viewmodels.factory.PersonalDataModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {

    public static final String TAG = "PersonalDataActivity";
    PersonalDataModelResponse.DataBean.UserBean userBean;
    PersonalDataViewModel personalDataViewModel;
    private int REQUEST_CAMERA = 0, RESULT_LOAD_IMAGE = 1;
    String path, token, message;
    private MultipartBody.Part picture;
    SharedPreferencesUtilities preferencesUtilities;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.user_profile)
    CircleImageView userProfile;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.available)
    TextView phone_txt;
    @BindView(R.id.faculty_txt)
    TextView facultyTxt;
    @BindView(R.id.department_txt)
    TextView departmentTxt;
    @BindView(R.id.year_txt)
    TextView yearTxt;
    @BindView(R.id.loading)
    MKLoader loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        token = preferencesUtilities.getToken();
        userBean = new PersonalDataModelResponse.DataBean.UserBean();
        personalDataViewModel = new ViewModelProvider(this, new PersonalDataModelFactory(this, userBean)).get(PersonalDataViewModel.class);
        personalDataViewModel.getInfo();
        personalDataViewModel.progress.observe(this, aBoolean -> {
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        personalDataViewModel.checkUser();
        personalDataViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        personalDataViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });

        personalDataViewModel.name.observe(this, s -> {
            if (s != null)
                txtName.setText(s);
        });

        personalDataViewModel.phone.observe(this, s -> {
            if (s != null)
                phone_txt.setText(s);
        });

        personalDataViewModel.fuculty.observe(this, s -> {
            if (s != null)
                facultyTxt.setText(s);
        });
        personalDataViewModel.department.observe(this, s -> {
            if (s != null)
                departmentTxt.setText(s);
        });
        personalDataViewModel.year.observe(this, s -> {
            if (s != null)
                yearTxt.setText(s);
        });
        personalDataViewModel.image.observe(this, s -> {
            if (s != null)
                Glide.with(this)
                        .load(s)
                        .error(R.drawable.profile_man)
                        .into(userProfile);
        });

    }

    private void checkLogin() {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalDataViewModel.checkUser();
        personalDataViewModel.message.observe(this, s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        personalDataViewModel.blocked.observe(this, aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    @OnClick({R.id.back, R.id.ivAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.ivAdd:
                TextView gallar, cameraa, clos;
                final Dialog dialog1 = new Dialog(PersonalDataActivity.this);
                dialog1.setContentView(R.layout.dialog);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.show();

                gallar = dialog1.findViewById(R.id.gallary);
                gallar.setOnClickListener(v -> {
                    gallaryIntent();
                    dialog1.dismiss();
                });

                cameraa = dialog1.findViewById(R.id.camera);
                cameraa.setOnClickListener(v -> {
                    CameraIntent();
                    dialog1.dismiss();
                });

                clos = dialog1.findViewById(R.id.close);
                clos.setOnClickListener(v -> dialog1.dismiss());
                break;
        }

    }

    private void gallaryIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pickImageIntent, " اختر صورة "), RESULT_LOAD_IMAGE);
    }

    private void CameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri filePath = data.getData();
            String imgpath = StaticMethods.getRealPathFromURI(getApplicationContext(), filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgpath);

            path = imgpath;
            Log.e(TAG, "onActivityResult: Path >>" + path);
            editPicture(picture);
            userProfile.setImageBitmap(bitmap);


        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri imgUri = StaticMethods.getImageUri(photo, getApplicationContext());
            userProfile.setImageBitmap(photo);

            String imgpath = StaticMethods.getRealPathFromURI(getApplicationContext(), imgUri);
            path = imgpath;
            Log.e(TAG, "onActivityResult: Path >>" + path);
            editPicture(picture);

            Toast.makeText(getApplicationContext(), "تم حفظ الصورة !", Toast.LENGTH_SHORT).show();
        }

    }

    public void editPicture(MultipartBody.Part picture) {
        loading.setVisibility(View.VISIBLE);

        if (path != null) {
            File file = new File(path);
            try {
                if (file.exists()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    picture = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "");
            picture = MultipartBody.Part.createFormData("picture", "", requestBody);
        }

        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.EditData editData = builder.editData();
        Call<ResetPasswordModelResponse> call = editData.editData("Bearer " + preferencesUtilities.getToken(), picture);
        call.enqueue(new Callback<ResetPasswordModelResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordModelResponse> call, Response<ResetPasswordModelResponse> response) {
                Log.e(TAG, "onResponse: Response >> " + response);
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: Response >> " + response);
                    loading.setVisibility(View.GONE);
                    message = response.body().getMessage();
                    Toast.makeText(PersonalDataActivity.this, message, Toast.LENGTH_SHORT).show();

                    Log.e(TAG, "onResponse: message >> " + message);
                } else {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(PersonalDataActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordModelResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
