package com.salim.ta3limes.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.salim.ta3limes.Models.response.FacultyResponse;
import com.salim.ta3limes.Models.response.GovernorateResponse;
import com.salim.ta3limes.Models.response.RegisterResponse;
import com.salim.ta3limes.Models.response.SpecializationResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.Retrofit.ServiceBuilder;
import com.salim.ta3limes.Retrofit.ServiceInterfaces;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.imgIdCard)
    ImageView imgIdCard;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.loading)
    MKLoader loading;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etStudentName)
    EditText etStudentName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etStudentPhone)
    EditText etStudentPhone;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etFaceBookLink)
    EditText etFaceBookLink;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPassword)
    EditText etPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spCountry)
    Spinner spCountry;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spCollege)
    Spinner spCollege;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spSpecialization)
    Spinner spSpecialization;
    List<GovernorateResponse.Governorate> governorates = new ArrayList<>();
    List<FacultyResponse.Faculties> faculties = new ArrayList<>();
    List<SpecializationResponse.Specialization> specializationList = new ArrayList<>();

    private int governorate_id;
    private int faculty_id;
    private int specialization_id;
    File file = null;
    Uri selectedImageUri;
    Bitmap yourSelectedImage = null;
    private static final int PERMISSION_REQUEST_CODE = 200;
    SharedPreferencesUtilities preferencesUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        preferencesUtilities = new SharedPreferencesUtilities(this);
        requestPermission();
        getGovernorate();
        imgIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    //main logic or main code
                    imageChooser();
                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    requestPermission();
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("name", (etStudentName.getText().toString()));
                    builder.addFormDataPart("phone", (etStudentPhone.getText().toString()));
                    builder.addFormDataPart("password", (etPassword.getText().toString()));
                    builder.addFormDataPart("facebook", (etFaceBookLink.getText().toString()));
                    builder.addFormDataPart("faculty", String.valueOf(faculty_id));
                    builder.addFormDataPart("specialization", String.valueOf(specialization_id));
                    builder.addFormDataPart("governorate", String.valueOf(governorate_id));
                    builder.addFormDataPart("picture", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                    Log.i("Ahmed", file.getName());
                    RequestBody requestBody = builder.build();
                    register(requestBody);
                }
            }
        });
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeybaord(view);
                if (i != 0) {
                    governorate_id = governorates.get(i - 1).getId();
                    ((TextView) view.findViewById(R.id.tvCatName)).setTextColor(Color.BLACK);
                    getFaculties(governorate_id);
                    specialization_id = 0;
                    faculty_id = 0;
                    if (spSpecialization.getAdapter() != null) {
                        spSpecialization.setAdapter(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    faculty_id = faculties.get(i - 1).getId();
                    ((TextView) view.findViewById(R.id.tvCatName)).setTextColor(Color.BLACK);
                    if (spSpecialization.getAdapter() != null) {
                        spSpecialization.setAdapter(null);
                    }
                    getSpecialization(faculty_id);
                    specialization_id = 0;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    specialization_id = specializationList.get(i - 1).getId();
                    ((TextView) view.findViewById(R.id.tvCatName)).setTextColor(Color.BLACK);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    private void register(RequestBody requestBody) {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.Register register = builder.register();
        Call<RegisterResponse> call = register.register(requestBody);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                loading.setVisibility(View.GONE);
                Log.i("Ahmed", response.code() + "");
                if (response.body() != null) {
                    Toast.makeText(RegisterActivity.this, response.code(), Toast.LENGTH_LONG).show();

                    if (response.code() == 200 && response.body().getStatus().equals("success")) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }


                } else if (response.code() == 422) {
                    Log.i("Ahmed", response.errorBody().toString() + "");
                    RegisterResponse errorResponse = null;
                    if (response.errorBody() != null) {
                        try {
                            errorResponse = new Gson().fromJson(response.errorBody().string().toString(), RegisterResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (errorResponse.errors != null) {
                        Log.i("Ahmed", response.code() + "");
                        if (errorResponse.errors.phone != null) {
                            etStudentPhone.setError(errorResponse.errors.phone.get(0));
                        }
                        if (errorResponse.errors.password != null) {
                            etPassword.setError(errorResponse.errors.password.get(0));
                        }
                        if (errorResponse.errors.facebook != null) {
                            etFaceBookLink.setError(errorResponse.errors.facebook.get(0));
                        }
                        if (errorResponse.errors.governorate != null) {
                            Log.i("Ahmed", errorResponse.errors.governorate.get(0));
                        }
                        if (errorResponse.errors.specialization != null) {
                            Log.i("Ahmed", errorResponse.errors.specialization.get(0));
                        }
                        if (errorResponse.errors.faculty != null) {
                            Log.i("Ahmed", errorResponse.errors.faculty.get(0));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Log.i("Ahmed", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(RegisterActivity.this, "حدث خطا ما يرجي المحاوله في وقت لاحق", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RegisterActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    private boolean validate() {
        boolean mCase = true;
        if (file == null) {
            Toast.makeText(RegisterActivity.this, "من فضلك حدد صوره الكارنيه", Toast.LENGTH_SHORT).show();
            mCase = false;
        }
        if (etStudentName.getText().toString().trim().isEmpty()) {
            etStudentName.setError("من فضلك ادخل الاسم");
            mCase = false;
        }
        if (etStudentPhone.getText().toString().trim().isEmpty()) {
            etStudentPhone.setError("من فضلك ادخل رقم الهاتف");
            mCase = false;
        }
        if (etFaceBookLink.getText().toString().trim().isEmpty()) {
            etFaceBookLink.setError("من فضلك ادخل رابط الفيس بوك");
            mCase = false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("من فضلك ادخل الرقم السري");
            mCase = false;
        }
        if (governorate_id == 0) {
            Toast.makeText(RegisterActivity.this, "من فضلك اختار المحافظه", Toast.LENGTH_SHORT).show();
            mCase = false;
        } else if (faculty_id == 0) {
            Toast.makeText(RegisterActivity.this, "من فضلك اختار الكليه", Toast.LENGTH_SHORT).show();
            mCase = false;
        } else if (specialization_id == 0) {
            Toast.makeText(RegisterActivity.this, "من فضلك اختار التخصص", Toast.LENGTH_SHORT).show();
            mCase = false;
        }
        return mCase;
    }


   /* get spinner data from APi */
    private void getGovernorate() {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.getGovernorate getGovernorate = builder.getGovernorate();
        Call<GovernorateResponse> call = getGovernorate.getGovernorate();
        call.enqueue(new Callback<GovernorateResponse>() {
            @Override
            public void onResponse(Call<GovernorateResponse> call, Response<GovernorateResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.body() != null) {
                    setGovernorate(response.body().getData().getGovernorate());

                }
            }

            @Override
            public void onFailure(Call<GovernorateResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });

    }
    private void getFaculties(int country_id) {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.getFaculties getFaculties = builder.getFaculties();
        Call<FacultyResponse> call = getFaculties.getFaculties(country_id);
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.body() != null) {
                    setFaculties(response.body().getData().getFaculties());
                    faculties = response.body().getData().getFaculties();
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {

            }
        });
    }
    private void getSpecialization(int faculty_id) {
        loading.setVisibility(View.VISIBLE);
        ServiceBuilder builder = new ServiceBuilder(preferencesUtilities.getBaseUrl());
        ServiceInterfaces.getSpecialization specialization = builder.getSpecialization();
        Call<SpecializationResponse> call = specialization.getSpecialization(faculty_id);
        call.enqueue(new Callback<SpecializationResponse>() {
            @Override
            public void onResponse(Call<SpecializationResponse> call, Response<SpecializationResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.body() != null) {
                    setSpecializationListToSpinner(response.body().getData().getSpecialization());
                    specializationList = response.body().getData().getSpecialization();
                }
            }

            @Override
            public void onFailure(Call<SpecializationResponse> call, Throwable t) {

            }
        });
    }

    /* set spinner adapter*/
    private void setGovernorate(List<GovernorateResponse.Governorate> governorate) {
        ArrayList<String> spGovernoratesData = new ArrayList<>();
        governorates = governorate;

        if (!governorate.isEmpty()) {
            spGovernoratesData.add("اختار المحافظه");
            for (int i = 0; i < governorate.size(); i++) {

                spGovernoratesData.add(governorate.get(i).getName());
            }
        } else spGovernoratesData.add("لا يوجد");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.state_spinner, R.id.tvCatName, spGovernoratesData) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        stateAdapter.setDropDownViewResource(R.layout.item_spinner_join_dropdown);
        stateAdapter.notifyDataSetChanged();
        spCountry.setAdapter(stateAdapter);
    }

    private void setFaculties(List<FacultyResponse.Faculties> list) {
        ArrayList<String> spFacultiesData = new ArrayList<>();


        if (!list.isEmpty()) {
            spFacultiesData.add("اختار الكليه");
            for (int i = 0; i < list.size(); i++) {

                spFacultiesData.add(list.get(i).getName());
            }
        } else spFacultiesData.add("لا يوجد");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.state_spinner, R.id.tvCatName, spFacultiesData) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        stateAdapter.setDropDownViewResource(R.layout.item_spinner_join_dropdown);
        stateAdapter.notifyDataSetChanged();
        spCollege.setAdapter(stateAdapter);
    }

    private void setSpecializationListToSpinner(List<SpecializationResponse.Specialization> list) {
        ArrayList<String> spSpecializationData = new ArrayList<>();

        if (!list.isEmpty()) {
            spSpecializationData.add("اختار التخصص");
            for (int i = 0; i < list.size(); i++) {

                spSpecializationData.add(list.get(i).getName());
            }
        } else spSpecializationData.add("لا يوجد");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.state_spinner, R.id.tvCatName, spSpecializationData) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        stateAdapter.setDropDownViewResource(R.layout.item_spinner_join_dropdown);
        stateAdapter.notifyDataSetChanged();
        spSpecialization.setAdapter(stateAdapter);
    }

    /* get image file*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == PERMISSION_REQUEST_CODE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {


                    try {
                        yourSelectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                        file = getFile(RegisterActivity.this, selectedImageUri);
                        Log.i("ahmed", file.getName());
                        imgIdCard.setImageBitmap(yourSelectedImage);
                        imgIdCard.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                        ivStoreBg.setImageURI(selectedImageUri);
                    // update the preview image in the layout

                }
            }
        }
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PERMISSION_REQUEST_CODE);
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RegisterActivity.this, "You need to allow access permissions", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }

                }
            });
    private void requestPermission() {

        List<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO);
        } else {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        requestPermissionLauncher.launch(String.valueOf(permissionsToRequest));
    }


}