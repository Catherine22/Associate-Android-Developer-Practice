package com.catherine.materialdesignapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class Utils {
    static Matcher<Object> withToolbarTitle(Matcher<String> matcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            protected boolean matchesSafely(Toolbar item) {
                return matcher.matches(item.getTitle().toString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                matcher.describeTo(description);
            }
        };
    }

    /**
     * retrieve items from recyclerView
     *
     * @param position
     * @param itemMatcher
     * @return
     */
    static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    static Matcher<View> withDrawable(final int resourceId, Matcher<View> matcher) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with image: ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final ImageView view) {
                Bitmap bitmap1 = ((BitmapDrawable) view.getDrawable()).getBitmap();
                Bitmap bitmap2 = BitmapFactory.decodeResource(MyApplication.INSTANCE.getResources(), resourceId);
                boolean isMatch = bitmap1.sameAs(bitmap2);
                bitmap2.recycle();
                return isMatch;
            }
        };
    }
}
