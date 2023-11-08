package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ComparisonActivity extends AppCompatActivity {

    private Button jobListB;
    private Job j1, j2;

    // Text Views for Job Comparison Table
    public static TextView j1Title, j1Company, j1Location, j1Salary, j1Bonus, j1Leave, j1PLeave, j1LI, j1Score;
    public static TextView j2Title, j2Company, j2Location, j2Salary, j2Bonus, j2Leave, j2PLeave, j2LI, j2Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        extractAndSetup();

        displayComparison();
    }

    void displayComparison() {
        final String GREEN_HIGHLIGHT = "0x5C7139";

        j1Title.setText(j1.getTitle());
        j2Title.setText(j2.getTitle());

        j1Company.setText(j1.getCompany());
        j2Company.setText(j2.getCompany());

        j1Location.setText(j1.getLocation());
        j2Location.setText(j2.getLocation());

        double adj_sal = new BigDecimal(j1.getSalary() / j1.getCostOfLiving()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        j1Salary.setText(Double.toString(adj_sal));
        adj_sal = new BigDecimal(j2.getSalary() / j2.getCostOfLiving()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        j2Salary.setText(Double.toString(adj_sal));
        highlight(j1Salary, j2Salary);

        double adj_bon = new BigDecimal(j1.getBonus() / j1.getCostOfLiving()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        j1Bonus.setText(Double.toString(adj_bon));
        adj_bon = new BigDecimal(j2.getBonus() / j2.getCostOfLiving()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        j2Bonus.setText(Double.toString(adj_bon));
        highlight(j1Bonus, j2Bonus);

        j1Leave.setText(Integer.toString(j1.getLeave()));
        j2Leave.setText(Integer.toString(j2.getLeave()));
        highlight(j1Leave, j2Leave);

        j1PLeave.setText(Integer.toString(j1.getParentalLeave()));
        j2PLeave.setText(Integer.toString(j2.getParentalLeave()));
        highlight(j1PLeave, j2PLeave);

        j1LI.setText(Integer.toString(j1.getLifeInsurancePercentage()));
        j2LI.setText(Integer.toString(j2.getLifeInsurancePercentage()));
        highlight(j1LI, j2LI);

        double score = new BigDecimal(j1.getScore()).setScale(1, RoundingMode.HALF_UP).doubleValue();
        j1Score.setText(Double.toString(score));
        score = new BigDecimal(j2.getScore()).setScale(1, RoundingMode.HALF_UP).doubleValue();
        j2Score.setText(Double.toString(score));
        highlight(j1Score, j2Score);
    }

    private void highlight(TextView t1, TextView t2) {
        double v1 = Double.valueOf(t1.getText().toString());
        double v2 = Double.valueOf(t2.getText().toString());

        if (v1 > v2) {
            t1.setTextColor(Color.WHITE);
            t1.setBackgroundResource(R.color.GREEN_HIGHLIGHT);
        } else if (v2 > v1) {
            t2.setTextColor(Color.WHITE);
            t2.setBackgroundResource(R.color.GREEN_HIGHLIGHT);
        }
        // Don't do anything special if they equal
    }

    public void onClick(View view) {
        if (view.getId() == jobListB.getId()) {
            finish();
        }
    }

    public void extractAndSetup() {
        jobListB = (Button) findViewById(R.id.backToJobListButton);

        j1 = getIntent().getParcelableExtra("job1");
        j2 = getIntent().getParcelableExtra("job2");

        j1Title = (TextView) findViewById(R.id.j1_title);
        j1Company = (TextView) findViewById(R.id.j1_company);
        j1Location = (TextView) findViewById(R.id.j1_location);
        j1Salary = (TextView) findViewById(R.id.j1_salary);
        j1Bonus = (TextView) findViewById(R.id.j1_bonus);
        j1Leave = (TextView) findViewById(R.id.j1_leave);
        j1PLeave = (TextView) findViewById(R.id.j1_pLeave);
        j1LI = (TextView) findViewById(R.id.j1_lifeInsur);
        j1Score = (TextView) findViewById(R.id.j1_score);

        j2Title = (TextView) findViewById(R.id.j2_title);
        j2Company = (TextView) findViewById(R.id.j2_company);
        j2Location = (TextView) findViewById(R.id.j2_location);
        j2Salary = (TextView) findViewById(R.id.j2_salary);
        j2Bonus = (TextView) findViewById(R.id.j2_bonus);
        j2Leave = (TextView) findViewById(R.id.j2_leave);
        j2PLeave = (TextView) findViewById(R.id.j2_pLeave);
        j2LI = (TextView) findViewById(R.id.j2_lifeInsur);
        j2Score = (TextView) findViewById(R.id.j2_score);
    }
}