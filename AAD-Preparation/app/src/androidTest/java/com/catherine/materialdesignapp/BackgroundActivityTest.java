package com.catherine.materialdesignapp;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.catherine.materialdesignapp.activities.BackgroundActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BackgroundActivityTest {

    @Rule
    public ActivityTestRule<BackgroundActivity> activityRule;

    private static String[] titles;
    private String[] subtitles;

    @Before
    public void setUp() {
        MyApplication myApplication = ApplicationProvider.getApplicationContext();
        myApplication.init(false);
        activityRule = new ActivityTestRule<>(BackgroundActivity.class);
        activityRule.launchActivity(new Intent());

        titles = activityRule.getActivity().getResources().getStringArray(R.array.background_task_array);
        subtitles = activityRule.getActivity().getResources().getStringArray(R.array.background_task_info_array);
    }

    @Test
    public void checkToolbar() {
        onView(isAssignableFrom(Toolbar.class)).check(matches(isDisplayed()));
        onView(isAssignableFrom(Toolbar.class)).check(matches(Utils.withToolbarTitle(is(BackgroundActivity.TAG))));
    }

    @Test
    public void testScrolling() {
        onView(withId(R.id.rv_features)).perform(RecyclerViewActions.scrollToPosition(titles.length - 1))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void verifyVisual() {
        // verify titles and subtitles of each item
        assertEquals(titles.length, subtitles.length);
        for (int i = 0; i < titles.length; i++) {
            onView(withId(R.id.rv_features)).perform(RecyclerViewActions.scrollToPosition(i))
                    .check(matches(Utils.atPosition(i, hasDescendant(withText(titles[i])))))
                    .check(matches(Utils.atPosition(i, hasDescendant(withText(subtitles[i])))));
            //TODO test simpleDraweeView
        }
    }

    @Test
    public void testItemClickEvents() {
        for (int i = 0; i < titles.length; i++) {
            // scroll to i
            onView(withId(R.id.rv_features)).perform(RecyclerViewActions.scrollToPosition(i))
                    .check(matches(isCompletelyDisplayed()));
            // click i
            onView(withId(R.id.rv_features)).perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.click()));
        }

        // espresso will automatically idle, so needn't to sleep

        // check if items update correctly
        String newString = activityRule.getActivity().getString(R.string.new_string);
        String correctMessage = String.format(Locale.US, newString, activityRule.getActivity().getString(R.string.hello_message));
        for (int i = 0; i < titles.length; i++) {
            onView(withId(R.id.rv_features)).perform(RecyclerViewActions.scrollToPosition(i))
                    .check(matches(Utils.atPosition(i, hasDescendant(withText(correctMessage)))));
        }
    }
}