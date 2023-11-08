package edu.gatech.seclass.jobcompare6300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdjustWeightsActivityTest {

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

    public static void setComparisonWeights(int sal, int bonus, int leave, int pLeave, int li){
        onView(withId(R.id.compSettButton)).perform(click());

        onView(withId(R.id.editYearlySalaryWeight)).perform(clearText(), replaceText(Integer.toString(sal)), closeSoftKeyboard());
        onView(withId(R.id.editYearlyBonusWeight)).perform(clearText(), replaceText(Integer.toString(bonus)), closeSoftKeyboard());
        onView(withId(R.id.editLeaveWeight)).perform(clearText(), replaceText(Integer.toString(leave)), closeSoftKeyboard());
        onView(withId(R.id.editpLeaveWeight)).perform(clearText(), replaceText(Integer.toString(pLeave)), closeSoftKeyboard());
        onView(withId(R.id.editLifeInsWeight)).perform(clearText(), replaceText(Integer.toString(li)), closeSoftKeyboard());
    }

    @Test
    public void CancelAddCurrentJob() {
        // Test Case 14 - Cancel Comparison Settings
        onView(withId(R.id.offersButton)).perform(click());
        EnterJobOfferActivityTest.addNewJobOfferHelper("Principal Computer Engineer", "Rivian", "Irvine, CA", 84, 250000, 2500, 20, 5, 7);
        onView(withId(R.id.addSaveJobOfferId)).perform(ViewActions.scrollTo(), click());

        setComparisonWeights(1, 2, 3, 4, 5);
        onView(withId(R.id.buttonCancel)).perform(ViewActions.scrollTo(), click());

        assertEquals(1, MainActivity.compWeights.getSalaryWeight());
        assertEquals(1, MainActivity.compWeights.getBonusWeight());
        assertEquals(1, MainActivity.compWeights.getLeaveWeight());
        assertEquals(1, MainActivity.compWeights.getPLeaveWeight());
        assertEquals(1, MainActivity.compWeights.getLifeInsurWeight());
    }

    @Test
    public void enterValidComparisonSettings() {
        // Test Case 16 - Enter Valid Comparison Settings
        setComparisonWeights(5, 4, 3, 3, 1);
        onView(withId(R.id.buttonSaveWeights)).perform(ViewActions.scrollTo(), click());

        assertEquals(5, MainActivity.compWeights.getSalaryWeight());
        assertEquals(4, MainActivity.compWeights.getBonusWeight());
        assertEquals(3, MainActivity.compWeights.getLeaveWeight());
        assertEquals(3, MainActivity.compWeights.getPLeaveWeight());
        assertEquals(1, MainActivity.compWeights.getLifeInsurWeight());
    }

    @Test
    public void adjustComparisonSettings() {
        // Test Case 17 - Adjust Comparison Settings
        EnterCurrentJobActivityTest.addCurrentJobHelper("Engineer II", "Google", "Palo Alto, CA", 98, 200000, 2000, 15, 10, 5);
        onView(withId(R.id.curSaveCurrentJobId)).perform(ViewActions.scrollTo(), click());

        double orig_score = MainActivity.jobList.get(0).getScore();

        setComparisonWeights(5, 4, 3, 3, 1);
        onView(withId(R.id.buttonSaveWeights)).perform(ViewActions.scrollTo(), click());

        double new_score = MainActivity.jobList.get(0).getScore();

        assertEquals(5, MainActivity.compWeights.getSalaryWeight());
        assertEquals(4, MainActivity.compWeights.getBonusWeight());
        assertEquals(3, MainActivity.compWeights.getLeaveWeight());
        assertEquals(3, MainActivity.compWeights.getPLeaveWeight());
        assertEquals(1, MainActivity.compWeights.getLifeInsurWeight());

        // The scores should not be the same after changing the comparison weights
        assertNotEquals(orig_score, new_score);
    }
}
