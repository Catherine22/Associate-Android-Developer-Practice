package com.catherine.materialdesignapp.models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class CursorItem implements Parcelable {
    public final static int TOP = 1;
    public final static int BODY = 1 << 1;
    public final static int BOTTOM = 1 << 2;

    private String title;
    private String subtitle;
    private int type;

    public CursorItem(String title, String subtitle, int type) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
    }

    public static final Creator CREATOR = new Creator() {
        public CursorItem createFromParcel(Parcel in) {
            return new CursorItem(in);
        }

        public CursorItem[] newArray(int size) {
            return new CursorItem[size];
        }
    };

    // Parcelling part
    public CursorItem(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeInt(type);
    }

    private CursorItem(Builder builder) {
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.type = builder.type;
    }

    public static class Builder {
        private String title;
        private String subtitle;
        private int type;

        public Builder() {
            title = "";
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public CursorItem build() {
            return new CursorItem(this);
        }
    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CardItem{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}