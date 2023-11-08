package edu.gatech.seclass.jobcompare6300;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        jobList = MainActivity.jobList;
        if (jobList != null) {
            Collections.sort(jobList, Comparator.comparingDouble(Job::getScore).reversed());
        }
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View offerView = inflater.inflate(R.layout.job_item, parent, false);
        return new ViewHolder(offerView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        Job job = jobList.get(position);
        if (job.equals(MainActivity.currentJob)) {
            holder.linearLayout.setBackgroundResource(R.color.GOLD_BACKGROUND);
        }
        else if (position % 2 == 0) {
            holder.linearLayout.setBackgroundResource(R.color.JOB_BACKGROUND);
        }

        TextView roleTextView = holder.roleTextView;
        roleTextView.setText(job.getTitle());

        TextView companyTextView = holder.companyTextView;
        companyTextView.setText(job.getCompany());

        TextView locationTextView = holder.locationTextView;
        locationTextView.setText(job.getLocation());

        TextView salaryTextView = holder.salaryTextView;
        salaryTextView.setText("$" + job.getSalary());

        Button scoreButton = holder.scoreButton;
        scoreButton.setText(Double.toString(job.getScore()));

        holder.compareCheckBox.setChecked(jobList.get(position).getSelected());
        holder.compareCheckBox.setTag(position);
        holder.compareCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer) holder.compareCheckBox.getTag();

                int count = 0;
                for (Job job : jobList) {
                    if (job.getSelected()) {
                        count++;
                    }
                }

                if (jobList.get(index).getSelected()) {
                    jobList.get(index).setSelected(false);
                } else {
                    if (count < 2) {
                        jobList.get(index).setSelected(true);
                    } else {
                        holder.compareCheckBox.setChecked(false);
                        Toast.makeText(holder.compareCheckBox.getContext(), "You can only select 2 to compare", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        JobListActivity.jobList = this.jobList;
    }

    @Override
    public int getItemCount() {
        if (jobList != null) {
            return jobList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView roleTextView;
        public TextView companyTextView;
        public TextView locationTextView;
        public TextView salaryTextView;
        public Button scoreButton;
        public CheckBox compareCheckBox;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            roleTextView = (TextView) itemView.findViewById(R.id.role);
            companyTextView = (TextView) itemView.findViewById(R.id.company);
            locationTextView = (TextView) itemView.findViewById(R.id.location);
            salaryTextView = (TextView) itemView.findViewById(R.id.salary);
            scoreButton = (Button) itemView.findViewById(R.id.score);
            compareCheckBox = (CheckBox) itemView.findViewById(R.id.compare);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}