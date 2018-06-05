package com.vidatak.vidatalk;

/**
 * Created by ericecheverri on 1/25/16.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private String mApplicationString;
    private MainActivity mActivity;

    public MainActivityInstrumentationTest() {
        super(MainActivity.class);
    }

    @Before
    public void initValidString() {
        // Specify a valid string for the application
        mApplicationString = "VidaTalk";
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void changeText_sameActivity() {

        // Check that the text on the drawer is Vidatalk
        onView(withId(R.id.txtAppTitle)).check(matches(withText(mApplicationString)));
    }

    @Test
    public void verifyOnClickHomeFragmentRenders() {
        // Verify Home fragment renders
        onView(withText("Home")).perform(click());
        // Check that the text matches for that unique id
        onView(withId(R.id.homeTxt)).check(matches(withText("")));

    }

    @Test
    public void verifyPainFragmentRenders() {
        // Verify Home fragment renders
        onView(withText("Pain")).perform(click());
        // Check that the text matches for that unique id
        onView(withId(R.id.translationTxt)).check(matches(withText("")));

    }

    @Test
    public void KeyboardFragmentRenders() {
        // Verify Home fragment renders
        onView(withText("Keyboard")).perform(click());
        // Check that the text matches for that unique id
        onView(withId(R.id.translationTxt)).check(matches(withText("")));

    }

    @Test
    public void verifyDrawingFragmentRenders() {
        // Verify Home fragment renders
        onView(withText("Draw")).perform(click());
        // Check that the text matches for that unique id
        onView(withId(R.id.erase)).check(matches(isDisplayed()));
    }
}