package edu.gatech.seclass.jobcompare6300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkMainMenuContainsFourButtons() {
        onView(withId(R.id.currJobButton)).check(matches(withText("Enter / Edit Current Job Details")));
        onView(withId(R.id.offersButton)).check(matches(withText("Enter Job Offers")));
        onView(withId(R.id.compSettButton)).check(matches(withText("Adjust Comparison Settings")));
        onView(withId(R.id.jobListButton)).check(matches(withText("View Job Offers")));
    }

    @Test
    public void MainActivityToCurrentJobActivity() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(EnterCurrentJobActivity.class.getName(), null, false);
        onView(withId(R.id.currJobButton)).perform(click());
        activityMonitor.waitForActivity();
        assertNotNull("EnterCurrentJobActivity not launched", activityMonitor);
    }

    @Test
    public void MainActivityToJobOfferActivity() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(EnterJobOfferActivity.class.getName(), null, false);
        onView(withId(R.id.offersButton)).perform(click());
        activityMonitor.waitForActivity();
        assertNotNull("EnterJobOfferActivity not launched", activityMonitor);
    }

    @Test
    public void MainActivityToAdjustWeightsActivity() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(AdjustWeightsActivity.class.getName(), null, false);
        onView(withId(R.id.compSettButton)).perform(click());
        activityMonitor.waitForActivity();
        assertNotNull("ComparisonSettingsActivity not launched", activityMonitor);
    }



    @Test
    public void MainActivityToJobListActivity() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(JobListActivity.class.getName(), null, false);
        onView(withId(R.id.jobListButton)).perform(click());
        activityMonitor.waitForActivity();
        assertNotNull("JobListActivity not launched", activityMonitor);
    }


}