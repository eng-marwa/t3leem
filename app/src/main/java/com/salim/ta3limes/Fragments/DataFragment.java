package com.salim.ta3limes.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.MainActivity;
import com.salim.ta3limes.Models.response.CourseDataModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.CustomTextView;
import com.salim.ta3limes.viewmodels.DataViewModel;
import com.salim.ta3limes.viewmodels.factory.CourseDataModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class DataFragment extends Fragment {

    private DataViewModel mViewModel;

    @BindView(R.id.course_name)
    CustomTextView courseName;
    @BindView(R.id.instractor_name)
    CustomTextView instractorName;
    @BindView(R.id.instractor_imgaeView)
    CircleImageView instractorImgaeView;
    @BindView(R.id.center_name)
    CustomTextView centerName;
    @BindView(R.id.center_logo)
    CircleImageView centerLogo;
    @BindView(R.id.date_textView)
    CustomTextView dateTextView;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.loading)
    MKLoader loading;

    int courseID;
    CourseDataModelResponse.DataBean.CourseBean courseBean;

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        courseID = getArguments().getInt("courseId");

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        View v = inflater.inflate(R.layout.data_fragment, container, false);

        courseName = v.findViewById(R.id.course_name);
        centerName = v.findViewById(R.id.center_name);
        centerLogo = v.findViewById(R.id.center_logo);
        instractorName = v.findViewById(R.id.instractor_name);
        instractorImgaeView = v.findViewById(R.id.instractor_imgaeView);
        dateTextView = v.findViewById(R.id.date_textView);
        text = v.findViewById(R.id.text);
        loading = v.findViewById(R.id.loading);

        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "Cairo-Regular.ttf", true);

        courseBean = new CourseDataModelResponse.DataBean.CourseBean();

        mViewModel = new ViewModelProvider(getActivity(), new CourseDataModelFactory(getActivity(), courseBean)).get(DataViewModel.class);
        mViewModel.GetCourseInfo(courseID);
        mViewModel.progress.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }else {
                loading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (getActivity() == null){
                return;
            }
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) checkLogin();
        });
        mViewModel.teacherImage.observe(getActivity(), s -> {
            if (getActivity() == null){
                return;
            }
            Glide.with(getActivity())
                    .load(s)
                    .error(R.drawable.profile_man)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(instractorImgaeView);
        });
        mViewModel.centerImage.observe(getActivity(), s -> {
            if (getActivity() == null){
                return;
            }
            Glide.with(getActivity())
                    .load(s)
                    .error(R.drawable.profile_man)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(centerLogo);
        });

        mViewModel.title.observe(getActivity(), s -> {
            courseName.setText(s);
        });
        mViewModel.teacherName.observe(getActivity(), s -> {
            instractorName.setText(s);
        });
        mViewModel.centerName.observe(getActivity(), s -> {
            centerName.setText(s);
        });
        mViewModel.courseDate.observe(getActivity(), s -> {
            dateTextView.setText(s);
        });
        mViewModel.courseDesc.observe(getActivity(), s -> {
            text.setText(s);
        });
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("انت غير مشترك فى هذا الكورس"))
                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("position", 0));
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (aBoolean) checkLogin();
        });
    }

    private void checkLogin() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
