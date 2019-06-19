package com.catherine.materialdesignapp;

import android.test.ActivityInstrumentationTestCase2;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.filters.MediumTest;
import com.catherine.materialdesignapp.activities.MainActivity;
import com.google.android.material.navigation.NavigationView;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;

@MediumTest
public class DrawerNavigatorTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String[] group1Titles;
    private String[] group2Titles;

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

        mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
        mNavigationView = mDrawerLayout.findViewById(R.id.nav_view);
    }

    public void testNavigationDrawerAppearance() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
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
}
