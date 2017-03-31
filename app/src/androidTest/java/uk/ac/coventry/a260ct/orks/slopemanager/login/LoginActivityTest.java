package uk.ac.coventry.a260ct.orks.slopemanager.login;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.util.concurrent.Monitor;
import android.support.test.filters.LargeTest;
import android.support.test.filters.MediumTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.view.View;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.dashboard.DashboardActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.registration.RegisteringActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.registration.UserTypeActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by freshollie on 30/03/17.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    private void inputUsername(String text) {
        onView(withId(R.id.login_username_input)).perform(clearText());
        onView(withId(R.id.login_username_input)).perform(typeText(text));
    }

    private void inputPassword(String text) {
        onView(withId(R.id.login_password_input)).perform(clearText());
        onView(withId(R.id.login_password_input)).perform(typeText(text));
    }

    private void clickLogin() {
        onView(withId(R.id.login_button)).perform(click());
    }


    @Test
    public void validLogin_test() {

        Instrumentation instrumentation = getInstrumentation();

        // Type into the username field...
        inputUsername("lol");
        closeSoftKeyboard();

        // Type into the password field...
        inputPassword("lol");

        closeSoftKeyboard();

        // Register we are interested in the dashboard activity...
        // this has to be done before we do something that will send us to that
        // activity...
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(DashboardActivity.class.getName(), null, false);

        // Click the login button...
        clickLogin();

        // Wait for the dashboard to start...
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Make sure we are logged in...
        onView(withId(R.id.view_sessions_button)).check(matches(isDisplayed()));
    }

    @Test
    public void openRegistration_test() {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(UserTypeActivity.class.getName(), null, false);

        // Click the register button
        onView(withId(R.id.login_register_button)).perform(click());

        // Wait for the activity to appear
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Check we can see it
        onView(withId(R.id.spinner_user_type)).check(matches(isDisplayed()));

    }
}
