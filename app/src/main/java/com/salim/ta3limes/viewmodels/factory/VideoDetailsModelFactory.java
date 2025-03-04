package com.salim.ta3limes.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.salim.ta3limes.Models.response.VideoCommentsModelResponse;
import com.salim.ta3limes.viewmodels.VideoDetailsViewModel;

import java.util.ArrayList;

public class VideoDetailsModelFactory extends ViewModelProvider.NewInstanceFactory {

    Context context;
    ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans;

    public VideoDetailsModelFactory(Context context, ArrayList<VideoCommentsModelResponse.DataBean.CommentsBean> commentsBeans) {
        this.context = context;
        this.commentsBeans = commentsBeans;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VideoDetailsViewModel(context, commentsBeans);
    }
}
