package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends AppCompatActivity {
    public static List<Job> jobList;
    private Button compareButton;
    private Button mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        compareButton = (Button) findViewById(R.id.compareButton);
        mainMenuButton = (Button) findViewById(R.id.mainMenuButton);

        jobList = getIntent().getParcelableArrayListExtra("jobList");

        RecyclerView recyclerView = findViewById(R.id.jobOffersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new JobAdapter(jobList));
    }

    public void onClick(View view) {
        if (view.getId() == compareButton.getId()) {
            List<Job> comparisonJobList = new ArrayList<>();
            for (Job job : jobList) {
                if (job.getSelected()) {
                    comparisonJobList.add(job);
                }
            }

            if (comparisonJobList.size() == 2) {
                Intent comparisonIntent = new Intent(JobListActivity.this, ComparisonActivity.class);
                comparisonIntent.putParcelableArrayListExtra("jobList", (ArrayList<? extends Parcelable>) jobList);
                comparisonIntent.putExtra("job1", comparisonJobList.get(0));
                comparisonIntent.putExtra("job2", comparisonJobList.get(1));
                startActivity(comparisonIntent);
            } else {
                Toast.makeText(compareButton.getContext(), "You must select 2 to compare", Toast.LENGTH_SHORT).show();
            }
        }

        if (view.getId() == mainMenuButton.getId()) {
            Intent mainMenuIntent = new Intent(JobListActivity.this, MainActivity.class);
            startActivity(mainMenuIntent);
        }
    }
}