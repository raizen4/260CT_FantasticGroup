package uk.ac.coventry.a260ct.orks.slopemanager;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;
import uk.ac.coventry.a260ct.orks.slopemanager.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by freshollie on 30/03/17.
 */

@LargeTest
public class TestUtils {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(
            LoginActivity.class);

    public static SlopeManagerApplication getApplication() {
        return (SlopeManagerApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

    public static String getString(int res, Object object) {
        return InstrumentationRegistry.getTargetContext().getString(res, object);
    }

    public static String getString(int res) {
        return InstrumentationRegistry.getTargetContext().getString(res);
    }

    public static SlopeDatabase getDatabase() {
        return getApplication().getSlopeDatabase();
    }

    public static void launchValidLogin() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_username_input),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("lol"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_username_input), withText("lol"),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.login_password_input),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("lol"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.login_password_input), withText("lol"),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());
    }

    @Test
    public void loadDashboard_test() {
        launchValidLogin();
    }


}
