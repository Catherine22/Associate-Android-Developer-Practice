package com.catherine.messagecenter;

import android.os.Bundle;

@SuppressWarnings("unused")
public class Result {

    private Bundle mBundle;
    private boolean mBoolean;
    private String mString;
    private int mInt;
    private Double mDouble;

    public void setInt(int mInt) {
        this.mInt = mInt;
    }

    public void setBundle(Bundle mBundle) {
        this.mBundle = mBundle;
    }

    public void setBoolean(boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public void setString(String mString) {
        this.mString = mString;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public boolean isBoolean() {
        return mBoolean;
    }

    public String getString() {
        return mString;
    }

    public int getInt() {
        return mInt;
    }

    public Double getDouble() {
        return mDouble;
    }

    public void setDouble(Double mDouble) {
        this.mDouble = mDouble;
    }
}
