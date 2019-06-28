package com.catherine.materialdesignapp.models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class ReceiverItem implements Parcelable {
    public String title;
    public String subtitle;
    public String lButton;
    public String rButton;

    public ReceiverItem(String title, String subtitle, String lButton, String rButton) {
        this.title = title;
        this.subtitle = subtitle;
        this.lButton = lButton;
        this.rButton = rButton;
    }

    public static final Creator CREATOR = new Creator() {
        public ReceiverItem createFromParcel(Parcel in) {
            return new ReceiverItem(in);
        }

        public ReceiverItem[] newArray(int size) {
            return new ReceiverItem[size];
        }
    };

    // Parcelling part
    public ReceiverItem(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        lButton = in.readString();
        rButton = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(lButton);
        dest.writeString(rButton);
    }

    private ReceiverItem(Builder builder) {
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.lButton = builder.lButton;
        this.rButton = builder.rButton;
    }

    public static class Builder {
        private String title;
        private String subtitle;
        private String lButton;
        private String rButton;

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

        public Builder lButton(String lButton) {
            this.lButton = lButton;
            return this;
        }

        public Builder rButton(String rButton) {
            this.rButton = rButton;
            return this;
        }

        public ReceiverItem build() {
            return new ReceiverItem(this);
        }
    }

    @Override
    public String toString() {
        return "CardItem{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", lButton='" + lButton + '\'' +
                ", rButton='" + rButton + '\'' +
                '}';
    }
}