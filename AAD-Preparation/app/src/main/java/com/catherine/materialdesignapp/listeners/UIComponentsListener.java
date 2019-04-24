package com.catherine.materialdesignapp.listeners;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public interface UIComponentsListener {
    Toolbar getToolbar();

    TabLayout getTabLayout();

    ActionBar getMyActionBar();
}
