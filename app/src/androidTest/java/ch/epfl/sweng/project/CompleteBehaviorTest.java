package ch.epfl.sweng.project;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.security.SecureRandom;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.util.TestUtilityFunctions.logUserOut;
import static ch.epfl.sweng.project.util.TestUtilityFunctions.wait1s;
import static ch.epfl.sweng.project.util.TestUtilityFunctions.wait250ms;
import static ch.epfl.sweng.project.util.TestUtilityFunctions.wait500ms;
import static ch.epfl.sweng.project.util.TestUtilityFunctions.waitNms;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

public class CompleteBehaviorTest {

    private static final String TAG = "CompleteBehaviorTest";
    /*
 Random string, for the registration.
 Source:
 http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
  */
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();
    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @SuppressWarnings("AnonymousInnerClassWithTooManyMethods")
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View item) {
                ViewParent parent = item.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && item.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @After
    public void finishActivity() {
        mActivityTestRule.getActivity().finish();
        wait1s(TAG);
        logUserOut();
    }

    @Test
    public void testFullApp() {

        wait500ms(TAG);

        String testUserMail = "test@" + randomString(6) + ".org";
        String testUserPassword = "12345";
        String testUserPhone = "+078888888";

        onView(withId(R.id.goto_registration_button)).perform(closeSoftKeyboard()).perform(click());
        wait500ms(TAG);

        onView(withId(R.id.registration_email)).perform(replaceText("test@astutus.org"), closeSoftKeyboard());
        onView(withId(R.id.registration_password)).perform(replaceText("abcdef"), closeSoftKeyboard());
        onView(withId(R.id.registration_password_bis)).perform(replaceText("abcdef"), closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());

        wait250ms(TAG);
        onView(withText(R.string.error_user_already_exists)).inRoot(withDecorView(not(is(mActivityTestRule.
                getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        onView(withId(R.id.registration_email)).perform(replaceText(testUserMail), closeSoftKeyboard());
        onView(withId(R.id.registration_password)).perform(replaceText(testUserPassword), closeSoftKeyboard());
        onView(withId(R.id.registration_password_bis)).perform(replaceText(testUserPassword), closeSoftKeyboard());
        onView(withId(R.id.registration_phone)).perform(replaceText(testUserPhone), closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());

        wait1s(TAG);

        onView(withId(R.id.goto_login_button)).perform(click());




        onView(withId(R.id.login_email)).perform(typeText("HolaSenior@Shanchez.co"), closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("PortesTriEstate"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        wait250ms(TAG);


        onView(withId(R.id.login_email)).perform(replaceText(testUserMail), closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(replaceText(testUserPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        wait1s(TAG);
        onView(withId(R.id.activity_list)).check(matches(isDisplayed()));

        // Incorporated the Filter tests

        onView(withId(R.id.filterButtonPopUp)).perform(click());
        onView(withId(R.id.MaxRooms)).perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.MinRooms)).perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.MaxSurface)).perform(typeText("2000000"), closeSoftKeyboard());
        onView(withId(R.id.MinSurface)).perform(typeText("2000000"), closeSoftKeyboard());

        onView(withId(R.id.MaxPrice)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.MinPrice)).perform(typeText("100"), closeSoftKeyboard());

        onView(withId(R.id.location)).perform(typeText("Renens"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(getString(R.string.building)))).perform(click());


        onView(withId(R.id.filterButton)).perform(click());
        wait250ms(TAG);

        final int[] counts = {0};
        onView(withId(R.id.houseList)).check(matches(new ViewTypeSafeMatcher(counts)));

        for (int i = 0; i < counts[0]; i++) {
            onData(anything()).inAdapterView(withId(R.id.houseList)).atPosition(i).
                    check(matches(hasDescendant(withText(containsString(
                            String.format(
                                    getString(R.string.text_location_surface),
                                    "Renens",
                                    "2'000'000",
                                    "3",
                                    getString(R.string.rooms)
                            ))))));
            onData(anything()).inAdapterView(withId(R.id.houseList)).atPosition(i).
                    check(matches(hasDescendant(withText(containsString(
                            String.format(
                                    getString(R.string.text_price_type),
                                    "100",
                                    getString(R.string.text_currency),
                                    getString(R.string.building)
                            ))))));
        }

        onView(withId(R.id.filterButtonPopUp)).perform(click());
        wait250ms(TAG);
        onView(withId(R.id.eraseButton)).perform(click());
        onView(withId(R.id.filterButton)).perform(click());
        wait250ms(TAG);


        onData(anything()).inAdapterView(withId(R.id.houseList)).atPosition(0).perform(click());
        onView(withId(R.id.addToFavorites)).perform(click());
        pressBack();
        onView(withId(R.id.FavoriteButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.houseList)).atPosition(0).perform(click());
        wait500ms(TAG);
        onView(withId(R.id.addToFavorites)).perform(click());
        pressBack();
        onView(withId(R.id.FavoriteButton)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.houseList)).atPosition(2).perform(click());
        onView(withId(R.id.activity_description)).check(matches(isDisplayed()));

        // wait 3s for the images to load
        waitNms(TAG, 3000);

        ViewInteraction img0 = onView(childAtPosition(withId(R.id.scroll), 0));
        wait500ms(TAG);

        img0.perform(scrollTo());
        wait250ms(TAG);
        img0.perform(click());
        wait250ms(TAG);

        pressBack();

        wait250ms(TAG);

        onView(withId(R.id.action_launch_panorama)).perform(click());

        logUserOut();

    }


    private String getString(int id) {
        return mActivityTestRule.getActivity().getString(id);
    }

    private static class ViewTypeSafeMatcher extends TypeSafeMatcher<View> {
        private final int[] counts;

        ViewTypeSafeMatcher(int[] extCounts) {
            counts = extCounts;
        }

        @Override
        public void describeTo(Description description) {
        }

        @Override
        public boolean matchesSafely(View item) {
            ListView listView = (ListView) item;
            counts[0] = listView.getCount();
            return true;
        }
    }

}
