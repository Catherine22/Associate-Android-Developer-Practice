package com.catherine.materialdesignapp;

import android.view.View;
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
}
