package com.salim.ta3limes.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatEditText;

import com.salim.ta3limes.R;


public class CustomTextView extends AppCompatEditText {


    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context, attrs);
    }


    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView);

        if (StaticMethods.getLocalLanguage(context).equals("ar")) {
            setGravity(Gravity.RIGHT);
        }else {
            setGravity(Gravity.RIGHT);
        }

        values.recycle();
    }

}
