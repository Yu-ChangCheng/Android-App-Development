package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EnterCurrentJobActivity extends AppCompatActivity {
    private EditText curJobTitleId;
    private EditText curCompanyId;
    private EditText curStateId;
    private EditText curCityId;
    private EditText curCOLId;
    private EditText curYearSalaryId;
    private EditText curYearBonusId;
    private EditText curLeaveId;
    private EditText curParentalLeaveId;
    private EditText curLifeInsuranceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_job);

        curJobTitleId = findViewById(R.id.curJobTitleId);
        curCompanyId = findViewById(R.id.curCompanyId);
        curStateId = findViewById(R.id.curStateId);
        curCityId = findViewById(R.id.curCityId);
        curCOLId = findViewById(R.id.curCOLId);
        curYearSalaryId = findViewById(R.id.curYearSalaryId);
        curYearBonusId = findViewById(R.id.curYearBonusId);
        curLeaveId = findViewById(R.id.curLeaveId);
        curParentalLeaveId = findViewById(R.id.curParentalLeaveId);
        curLifeInsuranceId = findViewById(R.id.curLifeInsuranceId);

        // if there is a current job, display it
        if (MainActivity.currentJob != null) {
            Job job = MainActivity.currentJob;
            curJobTitleId.setText(job.getTitle());
            curCompanyId.setText(job.getCompany());
            String[] location = job.getLocation().split(", ");
            if (location.length == 2) {
                curCityId.setText(location[0]);
                curStateId.setText(location[1]);
            }
            curCOLId.setText(String.valueOf(job.getCostOfLiving()));
            curYearSalaryId.setText(String.valueOf(job.getSalary()));
            curYearBonusId.setText(String.valueOf(job.getBonus()));
            curLeaveId.setText(String.valueOf(job.getLeave()));
            curParentalLeaveId.setText(String.valueOf(job.getParentalLeave()));
            curLifeInsuranceId.setText(String.valueOf(job.getLifeInsurancePercentage()));
        }


        Button curSaveCurrentJobId = findViewById(R.id.curSaveCurrentJobId);
        curSaveCurrentJobId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Input validation
                if (curJobTitleId.getText().toString().isEmpty()
                        || curCompanyId.getText().toString().isEmpty()
                        || curCityId.getText().toString().isEmpty()
                        || curStateId.getText().toString().isEmpty()
                        || curCOLId.getText().toString().isEmpty()
                        || curYearSalaryId.getText().toString().isEmpty()
                        || curYearBonusId.getText().toString().isEmpty()
                        || curLeaveId.getText().toString().isEmpty()
                        || curParentalLeaveId.getText().toString().isEmpty()
                        || curLifeInsuranceId.getText().toString().isEmpty()) {
                    // One or more fields are empty
                    Toast.makeText(EnterCurrentJobActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All fields are filled. Now we check if the numeric fields have correct number format
                try {
                    Integer.parseInt(curCOLId.getText().toString());
                    Double.parseDouble(curYearSalaryId.getText().toString());
                    Double.parseDouble(curYearBonusId.getText().toString());
                    Integer.parseInt(curLeaveId.getText().toString());
                    Integer.parseInt(curParentalLeaveId.getText().toString());
                    Integer.parseInt(curLifeInsuranceId.getText().toString());
                } catch (NumberFormatException nfe) {
                    // One or more fields have incorrect number format
                    Toast.makeText(EnterCurrentJobActivity.this, "Please enter valid numbers for numeric fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean range_check = rangeCheck();
                if (range_check == false) {
                    return;
                }

                // All fields have correct format. We can create the Job object safely
                Job job = new Job(
                        curJobTitleId.getText().toString(),
                        curCompanyId.getText().toString(),
                        curCityId.getText().toString() + ", " + curStateId.getText().toString(),
                        Integer.parseInt(curCOLId.getText().toString()),
                        Double.parseDouble(curYearSalaryId.getText().toString()),
                        Double.parseDouble(curYearBonusId.getText().toString()),
                        Integer.parseInt(curLeaveId.getText().toString()),
                        Integer.parseInt(curParentalLeaveId.getText().toString()),
                        Integer.parseInt(curLifeInsuranceId.getText().toString()));
                job.calculateScore();

                if (MainActivity.jobList.isEmpty() || !MainActivity.jobList.get(0).equals(job)) {
                    if (MainActivity.currentJob != null) {
                        // Remove the old job from the job list
                        MainActivity.jobList.remove(0);
                    }
                    MainActivity.jobList.add(0, job); // Add to beginning of jobList
                } else {
                    MainActivity.jobList.set(0, job); // If it already exists in the list, update it
                }
                MainActivity.currentJob = job;
                MainActivity.jobDbHelper.addJob(job, true);

                Intent jobListIntent = new Intent(EnterCurrentJobActivity.this, MainActivity.class);
                startActivity(jobListIntent);
            }
        });

        Button curCancelId = findViewById(R.id.curCancelId);
        curCancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean rangeCheck() {
        boolean result = true;
        // Range Checking for Leave, Parental Leave and Life Insurance %
        if (Integer.parseInt(curLeaveId.getText().toString()) < 0 || Integer.parseInt(curLeaveId.getText().toString()) > 30) {
            curLeaveId.setError("Leave Time must be between 0 and 30");
            result = false;
        }
        if (Integer.parseInt(curParentalLeaveId.getText().toString()) < 0 || Integer.parseInt(curParentalLeaveId.getText().toString()) > 20) {
            curParentalLeaveId.setError("Parental Leave Time must be between 0 and 20");
            result = false;
        }
        if (Integer.parseInt(curLifeInsuranceId.getText().toString()) < 0 || Integer.parseInt(curLifeInsuranceId.getText().toString()) > 10) {
            curLifeInsuranceId.setError("Life Insurance % must be between 0 and 10");
            result = false;
        }
        return result;
    }
}
