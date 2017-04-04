package uk.ac.coventry.a260ct.orks.slopemanager.login;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.TestUtils;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by freshollie on 30/03/17.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginInputTest {

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

    private void sendUsernameDoneButton() {
        onView(withId(R.id.login_username_input)).perform(pressImeActionButton());
    }

    private void sendPasswordDoneButton() {
        onView(withId(R.id.login_password_input)).perform(pressImeActionButton());
    }

    private void checkSnackbarText(String text) {
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(text)))
                .check(matches(isDisplayed()));
    }

    public void inputCredentials(String username, String password) {
        inputUsername(username);
        sendUsernameDoneButton();

        inputPassword(password);
    }

    @Test
    public void noUsername_test() {
        inputCredentials("", "password");

        closeSoftKeyboard();
        clickLogin();

        checkSnackbarText(TestUtils.getString(R.string.login_no_username));
    }

    @Test
    public void noPassword_test() {
        inputCredentials("username", "");

        closeSoftKeyboard();
        clickLogin();

        checkSnackbarText(TestUtils.getString(R.string.login_no_password));
    }

    @Test
    public void invalidCredentials_test() {
        inputCredentials("username", "password");

        closeSoftKeyboard();
        clickLogin();

        checkSnackbarText(TestUtils.getString(R.string.login_invalid));
    }

    @Test
    public void keyboardActionComplete_test() {
        inputCredentials("username", "password");

        sendPasswordDoneButton();
        InstrumentationRegistry.getInstrumentation().waitForIdle(new Runnable() {
            @Override
            public void run() {
                checkSnackbarText(TestUtils.getString(R.string.login_invalid));
            }
        });
    }

    @Test
    public void badCharacters_test() {
        inputUsername("' Drop *"); // Checks for SQL injection
        onView(withId(R.id.login_password_input))
                .perform(replaceText("@^%^&*((&^£^$}{}{@}:{{{:{}:{~:{:{{}\\\\\\\\}"));

        closeSoftKeyboard();
        clickLogin();

        checkSnackbarText(TestUtils.getString(R.string.login_invalid));
    }
}