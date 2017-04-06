package uk.ac.coventry.a260ct.orks.slopemanager.Registering;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AboutMePagetTest {

    private static Matcher<View> withoutError(final boolean shouldAppear) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                String stringError="";
                EditText editText = (EditText) view;
                try {
                    stringError=editText.getError().toString();
                }
                catch (Exception e){
                    //silently fail
                }
                boolean isError=false;
                if(!stringError.matches(""))
                 isError=true;
                return isError==shouldAppear;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityTestRule = new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void test2() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_register_button), withText("Register"),
                        withParent(allOf(withId(R.id.activity_login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner_user_type),
                        withParent(withId(R.id.content_user_type)),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction checkedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Instructor"), isDisplayed()));
        checkedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.spinner_user_type),
                        withParent(withId(R.id.content_user_type)),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        ViewInteraction checkedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Customer"), isDisplayed()));
        checkedTextView2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button_advance_to_register_act), withText("Next"),
                        withParent(withId(R.id.content_user_type)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        onView(withId(R.id.firstName_edit_text))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(replaceText("bogdan"))
                .perform(pressImeActionButton())
                .perform(closeSoftKeyboard())
                .perform(replaceText("bogdan95"))
                .check(ViewAssertions.matches(hasErrorText("That is not a valid first name, it should not have special characters or digits")))
                .perform(replaceText("bogdan"))
                .check(ViewAssertions.matches(isDisplayed()))
                .check(ViewAssertions.matches(withoutError(false)))
                .perform(pressImeActionButton());

        onView(withId(R.id.surname_edit_text))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(replaceText("bogdan"))
                .perform(pressImeActionButton())
                .perform(closeSoftKeyboard())
                .perform(replaceText("bogdan95"))
                .check(ViewAssertions.matches(hasErrorText("That is not a valid surname, it should not have special characters or digits")))
                .perform(replaceText("bogdan"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressImeActionButton());

        onView(withId(R.id.phone_edit_text))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(replaceText("07456854644"))
                .check(ViewAssertions.matches(hasErrorText("Put the prefix of your country first")))
                .perform(pressImeActionButton())
                .perform(closeSoftKeyboard())
                .perform(replaceText("07456854644545454454565436653"))
                .check(ViewAssertions.matches(hasErrorText("The number is too long")))
                .perform(replaceText("+447569485687"))
                .check(ViewAssertions.matches(withoutError(false)))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressImeActionButton());

        onView(withId(R.id.email_edit_text))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(replaceText("bogdan"))
                .check(ViewAssertions.matches(hasErrorText("Enter a valid address")))
                .perform(pressImeActionButton())
                .perform(closeSoftKeyboard())
                .perform(replaceText("bogdan95"))
                .check(ViewAssertions.matches(hasErrorText("Enter a valid address")))
                .perform(replaceText("bogdan@gmail.com"))
                .check(ViewAssertions.matches(withoutError(false)))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressImeActionButton());


    }
}