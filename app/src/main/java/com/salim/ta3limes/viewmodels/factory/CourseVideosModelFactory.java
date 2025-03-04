package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.viewmodels.VideosViewModel;
import com.salim.ta3limes.Models.response.CourseVideosModelResponse;

import java.util.List;

public class CourseVideosModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    List<CourseVideosModelResponse.Lesson> lessonsBeans;
    List<CourseVideosModelResponse.Suggestlessone> suggestlessones;

    public CourseVideosModelFactory(Context context, List<CourseVideosModelResponse.Lesson> lessonsBeans, List<CourseVideosModelResponse.Suggestlessone> suggestlessones) {
        this.context = context;
        this.lessonsBeans = lessonsBeans;
        this.suggestlessones = suggestlessones;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VideosViewModel(context, lessonsBeans, suggestlessones);
    }
}
