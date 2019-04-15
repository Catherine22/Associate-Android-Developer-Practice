package com.catherine.materialdesignapp.models;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class CardItem implements Parcelable {
    private String image;
    private String title;
    private String subtitle;
    private String lButton;
    private String rButton;

    public CardItem(String image, String title, String subtitle, String lButton, String rButton) {
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.lButton = lButton;
        this.rButton = rButton;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };

    // Parcelling part
    public CardItem(Parcel in) {
        image = in.readString();
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
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(lButton);
        dest.writeString(rButton);
    }

    private CardItem(Builder builder) {
        this.image = builder.image;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.lButton = builder.lButton;
        this.rButton = builder.rButton;
    }

    public static class Builder {
        private String image;
        private String title;
        private String subtitle;
        private String lButton;
        private String rButton;

        public Builder() {
            title = "";
        }

        public Builder image(String image) {
            this.image = image;
            return this;
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

        public CardItem build() {
            return new CardItem(this);
        }
    }


    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getLButton() {
        return lButton;
    }

    public String getRButton() {
        return rButton;
    }

    @Override
    public String toString() {
        return "CardItem{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", lButton='" + lButton + '\'' +
                ", rButton='" + rButton + '\'' +
                '}';
    }
}