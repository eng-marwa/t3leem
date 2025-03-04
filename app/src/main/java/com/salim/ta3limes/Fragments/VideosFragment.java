package com.salim.ta3limes.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.MainActivity;
import com.salim.ta3limes.Adapters.AdapterCourseVideos;
import com.salim.ta3limes.Adapters.AdapterSuggestionCourses;
import com.salim.ta3limes.Models.response.CourseVideosModelResponse;
import com.salim.ta3limes.R;
import com.salim.ta3limes.utilities.SharedPreferencesUtilities;
import com.salim.ta3limes.viewmodels.VideosViewModel;
import com.salim.ta3limes.viewmodels.factory.CourseVideosModelFactory;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
public class VideosFragment extends Fragment implements View.OnClickListener {
    private VideosViewModel mViewModel;
    TextView subscribsNumberTextView, msg_textView, tvSuggestions;
    RecyclerView recyclerViewRecyclerView;
    RecyclerView rvSuggestions;
    MKLoader loading;
    EditText search;
    AdapterCourseVideos adapterCourseVideos;
    AdapterSuggestionCourses adapterSuggestionCourses;
    LinearLayoutManager linearLayoutManager;
    SharedPreferencesUtilities preferencesUtilities;
    List<CourseVideosModelResponse.Lesson> lessonsBeans;
    List<CourseVideosModelResponse.Suggestlessone> suggestBeans;
    int courseID;
    String title;
    String students;
    public static VideosFragment newInstance() {
        return new VideosFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        courseID = getArguments().getInt("courseId",0);
        Log.i("ahmed",""+courseID);

        students = getArguments().getString("students");

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        View v = inflater.inflate(R.layout.videos_fragment, container, false);

        preferencesUtilities = new SharedPreferencesUtilities(getActivity());
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "Cairo-Regular.ttf", true);

        subscribsNumberTextView = v.findViewById(R.id.subscribs_textView);
        msg_textView = v.findViewById(R.id.msg_textView);
        search = v.findViewById(R.id.ivSearch);
        recyclerViewRecyclerView = v.findViewById(R.id.recyclerView_RecyclerView);
        rvSuggestions = v.findViewById(R.id.rvSuggestions);
        tvSuggestions = v.findViewById(R.id.tvSuggestions);
        loading = v.findViewById(R.id.loading);

        subscribsNumberTextView.setText(students);

        lessonsBeans = new ArrayList<>();
        suggestBeans = new ArrayList<>();
        mViewModel = new ViewModelProvider(this, new CourseVideosModelFactory(getActivity(), lessonsBeans, suggestBeans)).get(VideosViewModel.class);

        mViewModel.GetVideos(courseID);
        mViewModel.progress.observe(getActivity(), aBoolean -> {
            if (getActivity() == null){
                return;
            }
            if (aBoolean) {
                loading.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                loading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("انت غير مشترك فى هذا الكورس"))
                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("position", 0));

        });

        mViewModel.checkUser();
        mViewModel.message.observe(getActivity(), s -> {
            if (s.equals("قد تم حظر حسابك لالغاء الحظر يرجي التواصل مع ادارة التطبيق"))
                checkLogin();
        });

        mViewModel.blocked.observe(getActivity(), aBoolean -> {
            if (aBoolean) checkLogin();
        });

        adapterCourseVideos = new AdapterCourseVideos(getActivity(), lessonsBeans);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewRecyclerView.setAdapter(adapterCourseVideos);

        adapterSuggestionCourses = new AdapterSuggestionCourses(getActivity(), suggestBeans);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvSuggestions.setLayoutManager(linearLayoutManager);
        rvSuggestions.setAdapter(adapterSuggestionCourses);

        mViewModel.suggestmutableLiveData.observe(getActivity(), suggestlessones -> {
            if (getActivity() == null){
                return;
            }
            if (suggestlessones != null && !suggestlessones.isEmpty()) {
                adapterSuggestionCourses.getSuggestionBeans().clear();
                adapterSuggestionCourses.setSuggestionList(suggestlessones);
                rvSuggestions.post(() -> adapterSuggestionCourses.notifyDataSetChanged());
            } else {
                rvSuggestions.setVisibility(View.GONE);
                tvSuggestions.setVisibility(View.GONE);
            }
        });

        mViewModel.mutableLiveData.observe(getActivity(), lessonsBeans1 -> {
            if (getActivity() == null){
                return;
            }
            if (lessonsBeans1 != null && !lessonsBeans1.isEmpty()) {
                adapterCourseVideos.getLessonsBeans().clear();
                adapterCourseVideos.setLessonsBeans(lessonsBeans1);
                recyclerViewRecyclerView.post(() -> adapterCourseVideos.notifyDataSetChanged());

            } else {
                recyclerViewRecyclerView.setVisibility(View.GONE);
                msg_textView.setVisibility(View.VISIBLE);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString();
                ArrayList<CourseVideosModelResponse.Lesson> newlist = new ArrayList<CourseVideosModelResponse.Lesson>();
                for (CourseVideosModelResponse.Lesson dataBean : lessonsBeans) {
                    String name = dataBean.title.toLowerCase();
                    if (name.contains(s))
                        newlist.add(dataBean);
                    adapterCourseVideos.notifyDataSetChanged();
                }
                adapterCourseVideos.setFilter(newlist);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.GetVideos(courseID);
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
