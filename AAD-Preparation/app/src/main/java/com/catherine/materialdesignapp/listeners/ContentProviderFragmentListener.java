package com.catherine.materialdesignapp.listeners;

public interface ContentProviderFragmentListener {
    String[] getList(int index);

    void popUpFragment(int index);
}
