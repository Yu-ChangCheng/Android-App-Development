package edu.gatech.seclass.jobcompare6300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.graphics.drawable.ColorDrawable;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EnterJobOfferActivityTest {

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

    public static void addNewJobOfferHelper(String title, String company, String location, int col, double salary, double bonus, int leave, int pLeave, int lifeInsur){
        onView(withId(R.id.addJobTitleId)).perform(ViewActions.scrollTo(), clearText(), replaceText(title), closeSoftKeyboard());
        onView(withId(R.id.addCompanyId)).perform(ViewActions.scrollTo(), clearText(), replaceText(company), closeSoftKeyboard());
        onView(withId(R.id.addCityId)).perform(ViewActions.scrollTo(), clearText(), replaceText(location.split(", ")[0]), closeSoftKeyboard());
        onView(withId(R.id.addStateId)).perform(ViewActions.scrollTo(), clearText(), replaceText(location.split(", ")[1]), closeSoftKeyboard());
        onView(withId(R.id.addCOLId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(col)), closeSoftKeyboard());
        onView(withId(R.id.addYearSalaryId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Double.toString(salary)), closeSoftKeyboard());
        onView(withId(R.id.addYearBonusId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Double.toString(bonus)), closeSoftKeyboard());
        onView(withId(R.id.addLeaveId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(leave)), closeSoftKeyboard());
        onView(withId(R.id.addParentalLeaveId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(pLeave)), closeSoftKeyboard());
        onView(withId(R.id.addLifeInsuranceId)).perform(ViewActions.scrollTo(), clearText(), replaceText(Integer.toString(lifeInsur)), closeSoftKeyboard());
    }

    @Test
    public void CancelAddJobOffer() {
        // Test Case 9 - Cancel entering job offer
        onView(withId(R.id.offersButton)).perform(click());
        addNewJobOfferHelper("Principal Computer Engineer", "Rivian", "Irvine, CA", 84, 250000, 2500, 20, 5, 7);
        onView(withId(R.id.addCancelId)).perform(ViewActions.scrollTo(), click());

        assertEquals(0, MainActivity.jobList.size());
    }

    @Test
    public void addSingleValidJobOffer() {
        // Test Case 10 - Enter Valid New Job Offer
        onView(withId(R.id.offersButton)).perform(click());
        addNewJobOfferHelper("Principal Computer Engineer", "Rivian", "Irvine, CA", 84, 250000, 2500, 20, 5, 7);
        onView(withId(R.id.addSaveJobOfferId)).perform(ViewActions.scrollTo(), click());

        assertEquals("Principal Computer Engineer", MainActivity.jobList.get(0).getTitle());
        assertEquals("Rivian", MainActivity.jobList.get(0).getCompany());
        assertEquals("Irvine, CA", MainActivity.jobList.get(0).getLocation());
        assertEquals(84, MainActivity.jobList.get(0).getCostOfLiving());
        assertEquals(250000, MainActivity.jobList.get(0).getSalary(), 0.0);
        assertEquals(2500, MainActivity.jobList.get(0).getBonus(), 0.0);
        assertEquals(20, MainActivity.jobList.get(0).getLeave());
        assertEquals(5, MainActivity.jobList.get(0).getParentalLeave());
        assertEquals(7, MainActivity.jobList.get(0).getLifeInsurancePercentage());
    }

    @Test
    public void addMultipleValidJobOffers() {
        // Test Case 12 - Enter Multiple New Job Offer
        onView(withId(R.id.offersButton)).perform(click());
        addNewJobOfferHelper("Principal Computer Engineer", "Rivian", "Irvine, CA", 84, 250000, 2500, 20, 5, 7);
        onView(withId(R.id.addAnotherJobId)).perform(ViewActions.scrollTo(), click());

        assertEquals("Principal Computer Engineer", MainActivity.jobList.get(0).getTitle());
        assertEquals("Rivian", MainActivity.jobList.get(0).getCompany());
        assertEquals("Irvine, CA", MainActivity.jobList.get(0).getLocation());
        assertEquals(84, MainActivity.jobList.get(0).getCostOfLiving());
        assertEquals(250000, MainActivity.jobList.get(0).getSalary(), 0.0);
        assertEquals(2500, MainActivity.jobList.get(0).getBonus(), 0.0);
        assertEquals(20, MainActivity.jobList.get(0).getLeave());
        assertEquals(5, MainActivity.jobList.get(0).getParentalLeave());
        assertEquals(7, MainActivity.jobList.get(0).getLifeInsurancePercentage());

        addNewJobOfferHelper("Engineer I", "Home Depot", "Atlanta, GA", 77, 130000, 700, 13, 20, 2);
        onView(withId(R.id.addSaveJobOfferId)).perform(ViewActions.scrollTo(), click());

        assertEquals("Engineer I", MainActivity.jobList.get(1).getTitle());
        assertEquals("Home Depot", MainActivity.jobList.get(1).getCompany());
        assertEquals("Atlanta, GA", MainActivity.jobList.get(1).getLocation());
        assertEquals(77, MainActivity.jobList.get(1).getCostOfLiving());
        assertEquals(130000, MainActivity.jobList.get(1).getSalary(), 0.0);
        assertEquals(700, MainActivity.jobList.get(1).getBonus(), 0.0);
        assertEquals(13, MainActivity.jobList.get(1).getLeave());
        assertEquals(20, MainActivity.jobList.get(1).getParentalLeave());
        assertEquals(2, MainActivity.jobList.get(1).getLifeInsurancePercentage());
    }

    @Test
    public void compareJobOfferToCurrentJob() {
        // Test Case 13 - Enter Offer and Compare to Current Job
        EnterCurrentJobActivityTest.addCurrentJobHelper("Engineer II", "Google", "Palo Alto, CA", 98, 200000, 2000, 15, 10, 5);
        onView(withId(R.id.curSaveCurrentJobId)).perform(ViewActions.scrollTo(), click());

        onView(withId(R.id.offersButton)).perform(click());
        addNewJobOfferHelper("Principal Computer Engineer", "Rivian", "Irvine, CA", 84, 250000, 2500, 20, 5, 7);
        onView(withId(R.id.addCompareCurrentJobId)).perform(ViewActions.scrollTo(), click());

        assertEquals("Engineer II", MainActivity.jobList.get(0).getTitle());
        assertEquals("Google", MainActivity.jobList.get(0).getCompany());
        assertEquals("Palo Alto, CA", MainActivity.jobList.get(0).getLocation());
        assertEquals(98, MainActivity.jobList.get(0).getCostOfLiving());
        assertEquals(200000, MainActivity.jobList.get(0).getSalary(), 0.0);
        assertEquals(2000, MainActivity.jobList.get(0).getBonus(), 0.0);
        assertEquals(15, MainActivity.jobList.get(0).getLeave());
        assertEquals(10, MainActivity.jobList.get(0).getParentalLeave());
        assertEquals(5, MainActivity.jobList.get(0).getLifeInsurancePercentage());

        assertEquals("Principal Computer Engineer", MainActivity.jobList.get(1).getTitle());
        assertEquals("Rivian", MainActivity.jobList.get(1).getCompany());
        assertEquals("Irvine, CA", MainActivity.jobList.get(1).getLocation());
        assertEquals(84, MainActivity.jobList.get(1).getCostOfLiving());
        assertEquals(250000, MainActivity.jobList.get(1).getSalary(), 0.0);
        assertEquals(2500, MainActivity.jobList.get(1).getBonus(), 0.0);
        assertEquals(20, MainActivity.jobList.get(1).getLeave());
        assertEquals(5, MainActivity.jobList.get(1).getParentalLeave());
        assertEquals(7, MainActivity.jobList.get(1).getLifeInsurancePercentage());

        // Verify the Comparison Table filled out correctly
        assertEquals("Engineer II", ComparisonActivity.j2Title.getText());
        assertEquals("Google", ComparisonActivity.j2Company.getText());
        assertEquals("Palo Alto, CA", ComparisonActivity.j2Location.getText());
        assertEquals("2040.82", ComparisonActivity.j2Salary.getText());             // 200000 / 98 = 2040.82
        assertEquals("20.41", ComparisonActivity.j2Bonus.getText());                //   2000 / 98 = 20.41
        assertEquals("15", ComparisonActivity.j2Leave.getText());
        assertEquals("10", ComparisonActivity.j2PLeave.getText());
        assertEquals("5", ComparisonActivity.j2LI.getText());

        assertEquals("Principal Computer Engineer", ComparisonActivity.j1Title.getText());
        assertEquals("Rivian", ComparisonActivity.j1Company.getText());
        assertEquals("Irvine, CA", ComparisonActivity.j1Location.getText());
        assertEquals("2976.19", ComparisonActivity.j1Salary.getText());             // 250000 / 84 = 2976.19
        assertEquals("29.76", ComparisonActivity.j1Bonus.getText());                //   2500 / 84 = 29.76
        assertEquals("20", ComparisonActivity.j1Leave.getText());
        assertEquals("5", ComparisonActivity.j1PLeave.getText());
        assertEquals("7", ComparisonActivity.j1LI.getText());
    }
}
