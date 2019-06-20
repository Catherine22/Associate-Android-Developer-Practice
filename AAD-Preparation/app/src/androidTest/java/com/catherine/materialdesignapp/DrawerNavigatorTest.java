package com.catherine.materialdesignapp;

import android.test.ActivityInstrumentationTestCase2;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.filters.MediumTest;
import androidx.test.filters.SdkSuppress;
import com.catherine.materialdesignapp.activities.*;
import com.google.android.material.navigation.NavigationView;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.is;

@MediumTest
public class DrawerNavigatorTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private NavigationView mNavigationView;
    private String[] group1Titles;
    private String[] group2Titles;
    private int[] headerIds;
    private Map<Integer, String> staticActivityTitles;

    public DrawerNavigatorTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {
        mActivity = getActivity();
        String[] allTitles = mActivity.getResources().getStringArray(R.array.drawer_array);
        group1Titles = new String[4];
        // group 1
        System.arraycopy(allTitles, 0, group1Titles, 0, group1Titles.length);
        // group 2
        group2Titles = new String[2];
        System.arraycopy(allTitles, 4, group2Titles, 0, group2Titles.length);

        headerIds = new int[]{R.id.nav_main_header};

        staticActivityTitles = new HashMap<>();
        staticActivityTitles.put(R.id.nav_dynamic_delivery, DynamicDeliveryActivity.TAG);
        staticActivityTitles.put(R.id.nav_lifecycle, LifecycleActivity.TAG);
        staticActivityTitles.put(R.id.nav_background, BackgroundActivity.TAG);
        staticActivityTitles.put(R.id.nav_notification, NotificationActivity.TAG);


        DrawerLayout mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
        mNavigationView = mDrawerLayout.findViewById(R.id.nav_view);
    }

    public void testNavigationDrawerAppearance() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        // verify headers
        assertEquals(headerIds.length, mNavigationView.getHeaderCount());
        for (int i = 0; i < headerIds.length; i++) {
            View header = mNavigationView.getHeaderView(i);
            assertEquals(headerIds[i], header.getId());
        }
        onView(withId(R.id.tv_title)).check(matches(withText(R.string.nav_header_title)));
        onView(withId(R.id.tv_subtitle)).check(matches(withText(R.string.nav_header_subtitle)));
        onView(withId(R.id.iv_icon)).check(matches(Utils.withDrawable(R.mipmap.ic_launcher_round, withId(R.id.iv_icon))));


        final Menu menu = mNavigationView.getMenu();
        assertNotNull("Menu should not be null", menu);
        // real size = 5 -> R.id.nav_app_components, R.id.nav_background, R.id.nav_notification, R.id.nav_manage, communicate (R.id.nav_lifecycle, R.id.nav_dynamic_delivery)
        assertEquals("Should have matching number of items", group1Titles.length + 1, menu.size());
        // verify whether or not items on the drawer match the Resource
        for (int i = 0; i < group1Titles.length; i++) {
            final MenuItem currItem = menu.getItem(i);
            assertEquals("ID for Item #" + i, group1Titles[i], currItem.getTitle());
        }

        MenuItem group2 = menu.getItem(menu.size() - 1);
        // verify items in another group of the drawer
        SubMenu communicateMenu = group2.getSubMenu();
        for (int i = 0; i < group2Titles.length; i++) {
            final MenuItem currItem = communicateMenu.getItem(i);
            assertEquals("ID for Item #" + i, group2Titles[i], currItem.getTitle());
        }
        onView(withContentDescription(R.string.navigation_drawer_close)).perform(click());
    }

    public void testStaticMenuOnClickEvents() {
        // verify static titles
        for (Integer id : staticActivityTitles.keySet()) {
            clickAndNavigateTest(id, staticActivityTitles.get(id));
        }
    }

    @SdkSuppress(minSdkVersion = 26)
    public void testDynamicMenuOnClickEventsForOreo() {
        // AppComponentsActivity
        String[] titles = mActivity.getResources().getStringArray(R.array.app_component_array_O);
        clickAndNavigateTest(R.id.nav_app_components, titles[0]);

        // UIComponentsActivity
        titles = mActivity.getResources().getStringArray(R.array.ui_component_bottom_navigation);
        clickAndNavigateTest(R.id.nav_ui_components, titles[0]);
    }

    @SdkSuppress(minSdkVersion = 21)
    public void testDynamicMenuOnClickEventsForLollipop() {
        // AppComponentsActivity
        String[] titles = mActivity.getResources().getStringArray(R.array.app_component_array_lollipop);
        clickAndNavigateTest(R.id.nav_app_components, titles[0]);

        // UIComponentsActivity
        titles = mActivity.getResources().getStringArray(R.array.ui_component_bottom_navigation);
        clickAndNavigateTest(R.id.nav_ui_components, titles[0]);
    }

    @SdkSuppress(maxSdkVersion = 20)
    public void testDynamicMenuOnClickEvents() {
        // AppComponentsActivity
        String[] titles = mActivity.getResources().getStringArray(R.array.app_component_array);
        clickAndNavigateTest(R.id.nav_app_components, titles[0]);

        // UIComponentsActivity
        titles = mActivity.getResources().getStringArray(R.array.ui_component_bottom_navigation);
        clickAndNavigateTest(R.id.nav_ui_components, titles[0]);
    }

    private void clickAndNavigateTest(int id, String withString) {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(id));

        onView(isAssignableFrom(Toolbar.class)).check(matches(isDisplayed()));
        onView(isAssignableFrom(Toolbar.class)).check(matches(Utils.withToolbarTitle(is(withString))));
        onView(isRoot()).perform(pressBack());
    }
}
