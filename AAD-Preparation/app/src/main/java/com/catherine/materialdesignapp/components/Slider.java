package com.catherine.materialdesignapp.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Slider extends ConstraintLayout {
    private TextView tv_name, tv_progress_desc;
    private SeekBar seekBar;

    private final static int MIN_PROGRESS = 0;
    private final static int MAX_PROGRESS = 0;

    public Slider(Context context) {
        super(context);
        initView(context);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context ctx) {
        View.inflate(ctx, R.layout.slider, this);
        tv_name = this.findViewById(R.id.tv_name);
        tv_progress_desc = this.findViewById(R.id.tv_progress_desc);
        seekBar = this.findViewById(R.id.sb);
    }

    private void initView(Context ctx, AttributeSet attrs) {
        initView(ctx);

        TypedArray styleable = ctx.obtainStyledAttributes(attrs, R.styleable.Slider);
        int min = styleable.getInt(R.styleable.Slider_c_min_progress, MIN_PROGRESS);
        int max = styleable.getInt(R.styleable.Slider_c_max_progress, MAX_PROGRESS);
        int progress = styleable.getInt(R.styleable.Slider_c_progress, MIN_PROGRESS);
        String title = styleable.getString(R.styleable.Slider_c_name);
        String desc = styleable.getString(R.styleable.Slider_c_progress_desc);
        styleable.recycle();
        if (TextUtils.isEmpty(desc)) {
            desc = progress + "";
        }

        tv_name.setText(title);
        tv_progress_desc.setText(desc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(min);
        }
        seekBar.setMax(max);
        seekBar.setProgress(progress);
    }


    public void setTitle(String title) {
        tv_name.setText(title);
    }

    public String getTitle() {
        return tv_name.getText().toString();
    }


    public void setProgressDesc(String desc) {
        tv_progress_desc.setText(desc);
    }

    public int getProgressPercentage() {
        int min = MIN_PROGRESS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            min = seekBar.getMin();
        }
        return (seekBar.getProgress() - min) / (seekBar.getMax() - min);
    }

    public String getProgressDesc() {
        return tv_progress_desc.getText().toString();
    }


    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setMaxProgress(int max) {
        seekBar.setMax(max);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setMinProgress(int min) {
        seekBar.setMin(min);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        seekBar.setOnSeekBarChangeListener(listener);
    }


}
