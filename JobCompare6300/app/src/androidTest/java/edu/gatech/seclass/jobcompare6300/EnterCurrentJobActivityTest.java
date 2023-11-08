package edu.gatech.seclass.jobcompare6300;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;

import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class EnterCurrentJobActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> tActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp(){
        // Clear everything for a clean run
        MainActivity.jobList.clear();
        MainActivity.currentJob = null;
        MainActivity.compWeights = new CompensationWeights();
        MainActivity.jobDbHelper.cleanDB();
    }

    public static void addCurrentJobHelper(String title, String company, String location, int col, double salary, double bonus, int leave, int pLeave, int lifeInsur){
        onView(withId(R.id.currJobButton)).perform(click());

        onView(withId(R.id.curJobTitleId)).perform(clearText(), replaceText(title), closeSoftKeyboard());
        onView(withId(R.id.curCompanyId)).perform(clearText(), replaceText(company), closeSoftKeyboard());
        onView(withId(R.id.curCityId)).perform(clearText(), replaceText(location.split(", ")[0]), closeSoftKeyboard());
        onView(withId(R.id.curStateId)).perform(clearText(), replaceText(location.split(", ")[1]), closeSoftKeyboard());
        onView(withId(R.id.curCOLId)).perform(clearText(), replaceText(Integer.toString(col)), closeSoftKeyboard());
        onView(withId(R.id.curYearSalaryId)).perform(clearText(), replaceText(Double.toString(salary)), closeSoftKeyboard());
        onView(withId(R.id.curYearBonusId)).perform(clearText(), replaceText(Double.toString(bonus)), closeSoftKeyboard());
        onView(withId(R.id.curLeaveId)).perform(clearText(), replaceText(Integer.toString(leave)), closeSoftKeyboard());
        onView(withId(R.id.curParentalLeaveId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(pLeave)), closeSoftKeyboard());
        onView(withId(R.id.curLifeInsuranceId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(lifeInsur)), closeSoftKeyboard());
    }

    @Test
    public void CancelAddCurrentJob() {
        // Test Case 5 - Cancel entering current job details
        addCurrentJobHelper("Engineer II", "Google", "Palo Alto, CA", 98, 200000, 2000, 15, 10, 5);
        onView(withId(R.id.curCancelId)).perform(ViewActions.scrollTo(), click());

        assertEquals(Collections.emptyList(), MainActivity.jobList);
        assertEquals(null, MainActivity.currentJob);
    }

    @Test
    public void addAndEditValidCurrentJob() {
        // Test Case 6 - Enter Valid current job details
        addCurrentJobHelper("Engineer II", "Google", "Palo Alto, CA", 98, 200000, 2000, 15, 10, 5);
        onView(withId(R.id.curSaveCurrentJobId)).perform(ViewActions.scrollTo(), click());

        assertEquals(1, MainActivity.jobList.size());
        assertEquals("Engineer II", MainActivity.jobList.get(0).getTitle());
        assertEquals("Google", MainActivity.jobList.get(0).getCompany());
        assertEquals("Palo Alto, CA", MainActivity.jobList.get(0).getLocation());
        assertEquals(98, MainActivity.jobList.get(0).getCostOfLiving());
        assertEquals(200000, MainActivity.jobList.get(0).getSalary(), 0.0);
        assertEquals(2000, MainActivity.jobList.get(0).getBonus(), 0.0);
        assertEquals(15, MainActivity.jobList.get(0).getLeave());
        assertEquals(10, MainActivity.jobList.get(0).getParentalLeave());
        assertEquals(5, MainActivity.jobList.get(0).getLifeInsurancePercentage());

        assertNotNull(MainActivity.currentJob);

        assertEquals("Engineer II", MainActivity.currentJob.getTitle());
        assertEquals("Google", MainActivity.currentJob.getCompany());
        assertEquals("Palo Alto, CA", MainActivity.currentJob.getLocation());
        assertEquals(98, MainActivity.currentJob.getCostOfLiving());
        assertEquals(200000, MainActivity.currentJob.getSalary(), 0.0);
        assertEquals(2000, MainActivity.currentJob.getBonus(), 0.0);
        assertEquals(15, MainActivity.currentJob.getLeave());
        assertEquals(10, MainActivity.currentJob.getParentalLeave());
        assertEquals(5, MainActivity.currentJob.getLifeInsurancePercentage());

        // Test Case 8 - Edit current job details
        addCurrentJobHelper("Engineer, Software", "Facebook", "Silcon Valley, CA", 100, 180000, 1500, 10, 7, 3);
        onView(withId(R.id.curSaveCurrentJobId)).perform(ViewActions.scrollTo(), click());

        assertEquals(1, MainActivity.jobList.size());
        assertEquals("Engineer, Software", MainActivity.jobList.get(0).getTitle());
        assertEquals("Facebook", MainActivity.jobList.get(0).getCompany());
        assertEquals("Silcon Valley, CA", MainActivity.jobList.get(0).getLocation());
        assertEquals(100, MainActivity.jobList.get(0).getCostOfLiving());
        assertEquals(180000, MainActivity.jobList.get(0).getSalary(), 0.0);
        assertEquals(1500, MainActivity.jobList.get(0).getBonus(), 0.0);
        assertEquals(10, MainActivity.jobList.get(0).getLeave());
        assertEquals(7, MainActivity.jobList.get(0).getParentalLeave());
        assertEquals(3, MainActivity.jobList.get(0).getLifeInsurancePercentage());

        assertNotNull(MainActivity.currentJob);

        assertEquals("Engineer, Software", MainActivity.currentJob.getTitle());
        assertEquals("Facebook", MainActivity.currentJob.getCompany());
        assertEquals("Silcon Valley, CA", MainActivity.currentJob.getLocation());
        assertEquals(100, MainActivity.currentJob.getCostOfLiving());
        assertEquals(180000, MainActivity.currentJob.getSalary(), 0.0);
        assertEquals(1500, MainActivity.currentJob.getBonus(), 0.0);
        assertEquals(10, MainActivity.currentJob.getLeave());
        assertEquals(7, MainActivity.currentJob.getParentalLeave());
        assertEquals(3, MainActivity.currentJob.getLifeInsurancePercentage());
    }
}
