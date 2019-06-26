package com.catherine.materialdesignapp.components;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
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

        String namespace = String.format("%s%s", "http://schemas.android.com/apk/", ctx.getPackageName());
        String title = attrs.getAttributeValue(namespace, "c_title");
        int icon = attrs.getAttributeResourceValue(namespace, "c_icon", -1);
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
