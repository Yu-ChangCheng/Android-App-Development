package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class EnterJobOfferActivity extends AppCompatActivity {

    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText stateEditText;
    private EditText cityEditText;
    private EditText costOfLivingEditText;
    private EditText yearlySalaryEditText;
    private EditText yearlyBonusEditText;
    private EditText leaveEditText;
    private EditText parentalLeaveEditText;
    private EditText lifeInsuranceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_job_offer);

        jobTitleEditText = findViewById(R.id.addJobTitleId);
        companyEditText = findViewById(R.id.addCompanyId);
        stateEditText = findViewById(R.id.addStateId);
        cityEditText = findViewById(R.id.addCityId);
        costOfLivingEditText = findViewById(R.id.addCOLId);
        yearlySalaryEditText = findViewById(R.id.addYearSalaryId);
        yearlyBonusEditText = findViewById(R.id.addYearBonusId);
        leaveEditText = findViewById(R.id.addLeaveId);
        parentalLeaveEditText = findViewById(R.id.addParentalLeaveId);
        lifeInsuranceEditText = findViewById(R.id.addLifeInsuranceId);

        Button saveJobButton = findViewById(R.id.addSaveJobOfferId);
        saveJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isJobAdded = handleAddAnotherJob();
                if (isJobAdded){
                finish();
                }
            }
        });


        // Handle other button clicks
        Button addAnotherJobButton = findViewById(R.id.addAnotherJobId);
        addAnotherJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddAnotherJob();
            }
        });

        Button compareToCurrentJobButton = findViewById(R.id.addCompareCurrentJobId);
        compareToCurrentJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCompareToCurrentJob();
            }
        });

        // Disabling the "Compare" button if no current job has been entered
        if (MainActivity.currentJob == null) {
            compareToCurrentJobButton.setEnabled(false);
            compareToCurrentJobButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        } else {
            // Enable the button and set its color to blue if there is a current job
            compareToCurrentJobButton.setEnabled(true);
            int colorNavy = ContextCompat.getColor(getApplicationContext(), R.color.GT_NAVY);
            compareToCurrentJobButton.setBackgroundTintList(ColorStateList.valueOf(colorNavy));
        }

        Button cancelButton = findViewById(R.id.addCancelId);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCancel();
            }
        });
    }

    private void clearField() {
        jobTitleEditText.setText("");
        companyEditText.setText("");
        stateEditText.setText("");
        cityEditText.setText("");
        costOfLivingEditText.setText("");
        yearlySalaryEditText.setText("");
        yearlyBonusEditText.setText("");
        leaveEditText.setText("");
        parentalLeaveEditText.setText("");
        lifeInsuranceEditText.setText("");
    }

    private boolean handleAddAnotherJob() {
        // Input validation
        if (jobTitleEditText.getText().toString().isEmpty()
                || companyEditText.getText().toString().isEmpty()
                || stateEditText.getText().toString().isEmpty()
                || cityEditText.getText().toString().isEmpty()
                || costOfLivingEditText.getText().toString().isEmpty()
                || yearlySalaryEditText.getText().toString().isEmpty()
                || yearlyBonusEditText.getText().toString().isEmpty()
                || leaveEditText.getText().toString().isEmpty()
                || parentalLeaveEditText.getText().toString().isEmpty()
                || lifeInsuranceEditText.getText().toString().isEmpty()) {
            // One or more fields are empty
            Toast.makeText(EnterJobOfferActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All fields are filled. Now we check if the numeric fields have correct number format
        try {
            Integer.parseInt(costOfLivingEditText.getText().toString());
            Double.parseDouble(yearlySalaryEditText.getText().toString());
            Double.parseDouble(yearlyBonusEditText.getText().toString());
            Integer.parseInt(leaveEditText.getText().toString());
            Integer.parseInt(parentalLeaveEditText.getText().toString());
            Integer.parseInt(lifeInsuranceEditText.getText().toString());
        } catch (NumberFormatException nfe) {
            // One or more fields have incorrect number format
            Toast.makeText(EnterJobOfferActivity.this, "Please enter valid numbers for numeric fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean range_check = rangeCheck();
        if (range_check == false) {
            return false;
        }

        // All fields have correct format. We can create the Job object safely
        String jobTitle = jobTitleEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String location = cityEditText.getText().toString() + ", " + stateEditText.getText().toString();
        int costOfLiving = Integer.parseInt(costOfLivingEditText.getText().toString());
        double yearlySalary = Double.parseDouble(yearlySalaryEditText.getText().toString());
        double yearlyBonus = Double.parseDouble(yearlyBonusEditText.getText().toString());
        int leave = Integer.parseInt(leaveEditText.getText().toString());
        int parentalLeave = Integer.parseInt(parentalLeaveEditText.getText().toString());
        int lifeInsurance = Integer.parseInt(lifeInsuranceEditText.getText().toString());

        // Assuming a default score of 0 for a new job offer
        double score = 0;

        Job job = new Job(jobTitle, company, location, costOfLiving, yearlySalary, yearlyBonus, leave, parentalLeave, lifeInsurance);
        job.calculateScore();
        MainActivity.jobList.add(job);
        MainActivity.jobDbHelper.addJob(job, false);
        Toast.makeText(EnterJobOfferActivity.this, "Job Offer Saved", Toast.LENGTH_SHORT).show();
        clearField();
        return true;
    }

    private void handleCompareToCurrentJob() {
        if (MainActivity.currentJob == null) {
            Toast.makeText(this, "No current job to compare to", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!handleAddAnotherJob()) { return; }

        // Create an Intent to start the ComparisonActivity
        Intent intent = new Intent(this, ComparisonActivity.class);

        // Pass the new job and the current job to the ComparisonActivity
        intent.putExtra("job1", MainActivity.jobList.get(MainActivity.jobList.size() - 1));
        intent.putExtra("job2", MainActivity.currentJob);

        // Start the ComparisonActivity
        startActivity(intent);
    }

    private void handleCancel() {
        finish();
    }

    private boolean rangeCheck() {
        boolean result = true;
        // Range Checking for Leave, Parental Leave and Life Insurance %
        if (Integer.parseInt(leaveEditText.getText().toString()) < 0 || Integer.parseInt(leaveEditText.getText().toString()) > 30) {
            leaveEditText.setError("Leave Time must be between 0 and 30");
            result = false;
        }
        if (Integer.parseInt(parentalLeaveEditText.getText().toString()) < 0 || Integer.parseInt(parentalLeaveEditText.getText().toString()) > 20) {
            parentalLeaveEditText.setError("Parental Leave Time must be between 0 and 20");
            result = false;
        }
        if (Integer.parseInt(lifeInsuranceEditText.getText().toString()) < 0 || Integer.parseInt(lifeInsuranceEditText.getText().toString()) > 10) {
            lifeInsuranceEditText.setError("Life Insurance % must be between 0 and 10");
            result = false;
        }
        return result;
    }
}
