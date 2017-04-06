package uk.ac.coventry.a260ct.orks.slopemanager.Registering;


import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.dashboard.DashboardActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.registration.RegisteringActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginDetailsFragmentTest {

    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("You must have both username and password setted");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                }
            }
            return false;
        }
    }
    @Rule
    public ActivityTestRule<DashboardActivity> mActivityTestRule = new ActivityTestRule<>(DashboardActivity.class);
    public ActivityTestRule<RegisteringActivity> mRegisteringRule = new ActivityTestRule<>(RegisteringActivity.class);

    @Test
    public void loginDetailsFragmentTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_register_button), withText("Register"),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button_advance_to_register_act), withText("Next"),
                        withParent(withId(R.id.content_user_type)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Login Details"), isDisplayed()));
        appCompatTextView.perform(click());


        onView(withId(R.id.buttonSubmitForm)).perform(closeSoftKeyboard()).check(ViewAssertions.matches(isDisplayed())).perform(click());

        onView(withId(R.id.user_password_loggin_fragment)).perform(replaceText("gogo")).perform(pressImeActionButton());
        onView(withId(R.id.username_edit_text_login_frag)).perform(replaceText("gogo")).perform(pressImeActionButton());
        onView(withId(R.id.buttonSubmitForm)).check(ViewAssertions.matches(isDisplayed())).perform(click());

    }

}

