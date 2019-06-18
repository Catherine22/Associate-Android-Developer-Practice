package com.catherine.materialdesignapp;

import android.test.ActivityInstrumentationTestCase2;
import androidx.test.filters.MediumTest;
import com.catherine.materialdesignapp.activities.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@MediumTest
public class DayNightModeTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public DayNightModeTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Espresso does not start the Activity for you we need to do this manually here.
        mActivity = getActivity();
    }

    public void testPreconditions() {
        assertNotNull(mActivity);
    }

    public void testClickOptionsMenuNightMode() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Night Mode")).perform(click());
    }

    public void testClickOptionsMenuDayMode() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Day Mode")).perform(click());
    }
}
