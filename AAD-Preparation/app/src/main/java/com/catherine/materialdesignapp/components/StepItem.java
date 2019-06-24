package com.catherine.materialdesignapp.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.catherine.materialdesignapp.R;

public class StepItem extends RelativeLayout {
    private View upper_line, lower_line;
    private ImageView iv_icon;
    private TextView tv_title;

    public StepItem(Context context) {
        super(context);
        initView(context);
    }

    public StepItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StepItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public StepItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context ctx) {
        View view = View.inflate(ctx, R.layout.item_step, this);
        iv_icon = view.findViewById(R.id.iv_icon);
        tv_title = view.findViewById(R.id.tv_title);
        upper_line = view.findViewById(R.id.upper_line);
        lower_line = view.findViewById(R.id.lower_line);
    }

    private void initView(Context ctx, AttributeSet attrs) {
        initView(ctx);
        TypedArray styleable = ctx.obtainStyledAttributes(attrs, R.styleable.StepItem);
        String title = styleable.getString(R.styleable.StepItem_s_title);
        setTitle(title);

        boolean hideUpperLine = styleable.getBoolean(R.styleable.StepItem_s_is_first_step, false);
        boolean hideLowerLine = styleable.getBoolean(R.styleable.StepItem_s_is_last_step, false);
        isFirstStep(hideUpperLine);
        isLastStep(hideLowerLine);

        boolean isDone = styleable.getBoolean(R.styleable.StepItem_s_is_done, false);
        isFinished(isDone);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setIcon(int resId) {
        iv_icon.setImageResource(resId);
    }

    public void isFirstStep(boolean hideUpperLine) {
        if (hideUpperLine)
            upper_line.setVisibility(INVISIBLE);
        else
            upper_line.setVisibility(VISIBLE);
    }

    public void isLastStep(boolean hideLowerLine) {
        if (hideLowerLine)
            lower_line.setVisibility(INVISIBLE);
        else
            lower_line.setVisibility(VISIBLE);
    }

    public void isFinished(boolean isDone) {
        if (isDone) {
            iv_icon.setImageResource(R.drawable.ic_done_black_24dp);
        } else {
            iv_icon.setImageResource(R.drawable.primary_color_circle);
        }
    }

}
