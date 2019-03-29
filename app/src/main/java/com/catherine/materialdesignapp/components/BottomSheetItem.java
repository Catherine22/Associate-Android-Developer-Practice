package com.catherine.materialdesignapp.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;

public class BottomSheetItem extends RelativeLayout {
    private ImageView iv_icon;
    private TextView tv_title;

    public BottomSheetItem(Context context) {
        super(context);
        initView(context);
    }

    public BottomSheetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BottomSheetItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomSheetItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context ctx) {
        View view = View.inflate(ctx, R.layout.bottom_sheet_item, this);
        iv_icon = view.findViewById(R.id.iv_icon);
        tv_title = view.findViewById(R.id.tv_title);
    }


    private void initView(Context ctx, AttributeSet attrs) {
        initView(ctx);

        String androidPath = "http://schemas.android.com/apk/";
        String packageName = ctx.getPackageName();
        String title = attrs.getAttributeValue(String.format("%s%s", androidPath, packageName), "c_title");
        int icon = attrs.getAttributeResourceValue(String.format("%s%s", androidPath, packageName), "c_icon", -1);
        if (icon != -1) {
            setIcon(icon);
        }
        setTitle(title);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setIcon(int resId) {
        iv_icon.setImageResource(resId);
    }
}
