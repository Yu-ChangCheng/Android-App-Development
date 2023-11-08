package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Job currentJob;
    public static List<Job> jobList;
    public static CompensationWeights compWeights;
    private Button currJobB;
    private Button offersB;
    private Button compSettB;
    private Button jobListB;

    public static JobDbHelper jobDbHelper;

    private void updateScore() {
        for(int i = 0; i < jobList.size(); i++) {
            Job otherJob = jobList.get(i);
            if(otherJob != null) {
                otherJob.setWeights(compWeights);       //Update Weights, trigger score recalculation
            }
        }
        if(currentJob != null) {
            currentJob.setWeights(compWeights);         //Update Weights, trigger score recalculation
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        jobDbHelper = new JobDbHelper(MainActivity.this);
        jobDbHelper.startDB();

        currJobB = (Button) findViewById(R.id.currJobButton);
        offersB = (Button) findViewById(R.id.offersButton);
        compSettB = (Button) findViewById(R.id.compSettButton);
        jobListB = (Button) findViewById(R.id.jobListButton);

        if (jobList == null) {
            jobList = new ArrayList<Job>();
        }

        if (getIntent().hasExtra("keyCompWeights")) {
            compWeights = getIntent().getParcelableExtra("keyCompWeights");
            updateScore(); //Update Score for all jobs upon returning from CompWeights Update
        } else if (compWeights == null){
            compWeights = new CompensationWeights();
        }
        updateScore();

        currJobB.setOnClickListener(this::onClick);
        offersB.setOnClickListener(this::onClick);
        compSettB.setOnClickListener(this::onClick);
        jobListB.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Intent intent;
        if (view.getId() == currJobB.getId()) {
            intent = new Intent(MainActivity.this, EnterCurrentJobActivity.class);
            startActivity(intent);
        } else if (view.getId() == offersB.getId()) {
            intent = new Intent(MainActivity.this, EnterJobOfferActivity.class);
            startActivity(intent);
        } else if (view.getId() == compSettB.getId()) {
            Intent adjustWeightsIntent = new Intent(MainActivity.this, AdjustWeightsActivity.class);
            adjustWeightsIntent.putExtra("keyCompWeights", compWeights);
            startActivity(adjustWeightsIntent);
        } else if (view.getId() == jobListB.getId()) {
            if (jobList.size() < 1) {
                Toast.makeText(jobListB.getContext(), "You must enter at least one job to view offers", Toast.LENGTH_SHORT).show();
            } else {
                Intent jobListIntent = new Intent(MainActivity.this, JobListActivity.class);
                jobListIntent.putParcelableArrayListExtra("jobList", (ArrayList<? extends Parcelable>) jobList);
                startActivity(jobListIntent);
            }
        }
    }
}