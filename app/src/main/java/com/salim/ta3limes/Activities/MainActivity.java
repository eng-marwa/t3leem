package com.salim.ta3limes.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.salim.ta3limes.Fragments.CentersFragment;
import com.salim.ta3limes.Fragments.CoursesFragment;
import com.salim.ta3limes.Fragments.Mp3FilesFragment;
import com.salim.ta3limes.Fragments.MyLibraryFragment;
import com.salim.ta3limes.Fragments.ProfileFragment;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.ContantsUtils;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.utilities.StaticMethods;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    int navPosition = 0;
    CoursesFragment coursesFragment;
    String mImeiId = "";
    SharedPreferencesUtilities preferencesUtilities;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencesUtilities = new SharedPreferencesUtilities(this);
        Log.e(TAG, "onCreate: Token >> " + preferencesUtilities.getToken());
        navView = findViewById(R.id.nav_view_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "onMessageBundle: notification " + bundle.toString());
        }

        mImeiId = StaticMethods.getIMEIDeviceId(this);
        preferencesUtilities.setDeviceId(mImeiId);

        navPosition = getIntent().getIntExtra("position", 0);

        switch (navPosition) {
            case 0:
                transIntent(new CoursesFragment());
                openFragmentByPosition();
                break;
            case 1:
                ContantsUtils.TYPE = "library";
                transIntent(new MyLibraryFragment());
                openFragmentByPosition();
                break;
            case 2:
                ContantsUtils.TYPE = "fileVoice";
                transIntent(new Mp3FilesFragment());
                openFragmentByPosition();
                break;
            case 3:
                transIntent(new CentersFragment());
                openFragmentByPosition();
                break;
            case 4:
                transIntent(new ProfileFragment());
                openFragmentByPosition();
                break;
        }
        init();
    }

    private void init() {
        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_courses:
                    navPosition = 0;
                    transIntent(new CoursesFragment());
                    break;
                case R.id.nav_library:
                    navPosition = 1;
                    ContantsUtils.TYPE = "library";
                    transIntent(new MyLibraryFragment());
                    break;
                case R.id.nav_mp3:
                    navPosition = 2;
                    ContantsUtils.TYPE = "fileVoice";
                    transIntent(new Mp3FilesFragment());
                    break;
                case R.id.nav_centers:
                    navPosition = 3;
                    transIntent(new CentersFragment());
                    break;
                case R.id.nav_account:
                    navPosition = 4;
                    transIntent(new ProfileFragment());
                    break;
            }
            return true;
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mImeiId = StaticMethods.getIMEIDeviceId(this);
            preferencesUtilities.setDeviceId(mImeiId);
            Log.e(TAG, "onCreate: mImeiId >> " + mImeiId);
        } else {
            Log.e(TAG, "onRequestPermissionsResult: " + "PERMISSION_GRANTED");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("يجب الموافقة علي الاذن السابق لاتمام عملية الدخول");
            builder.setPositiveButton("حسناً", (dialog, which) -> {
                mImeiId = StaticMethods.getIMEIDeviceId(this);
                preferencesUtilities.setDeviceId(mImeiId);
                Log.e(TAG, "onCreate: mImeiId >> " + mImeiId);
            });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (navPosition == 0) {
            super.onBackPressed();
            finish();
        } else {
            navPosition = 0;
            openFragmentByPosition();
        }
    }

    private void transIntent(Fragment fragment) {
        getSupportFragmentManager().popBackStackImmediate();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    private void openFragmentByPosition() {
        switch (navPosition) {
            case 0:
                navView.setSelectedItemId(R.id.nav_courses);
                break;
            case 1:
                navView.setSelectedItemId(R.id.nav_library);
                break;
            case 2:
                navView.setSelectedItemId(R.id.nav_mp3);
                break;
            case 3:
                navView.setSelectedItemId(R.id.nav_centers);
                break;
            case 4:
                navView.setSelectedItemId(R.id.nav_account);
                break;
        }
    }


}
