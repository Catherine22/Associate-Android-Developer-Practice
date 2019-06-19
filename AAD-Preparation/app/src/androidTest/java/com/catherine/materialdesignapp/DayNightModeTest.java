package com.catherine.materialdesignapp;

import android.test.ActivityInstrumentationTestCase2;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.filters.MediumTest;
import com.catherine.materialdesignapp.activities.MainActivity;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@MediumTest
public class DayNightModeTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private String DAY_MODE = "Day Mode";
    private String NIGHT_MODE = "Night Mode";

    public DayNightModeTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        mActivity = getActivity();
    }

    public void testPreconditions() {
        assertNotNull(mActivity);
    }

    public void testClickOptionsMenuNightMode() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(NIGHT_MODE)).perform(click());
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        assertEquals(nightMode, AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void testClickOptionsMenuDayMode() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(DAY_MODE)).perform(click());
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        assertEquals(nightMode, AppCompatDelegate.MODE_NIGHT_NO);
    }
}
