package edu.gatech.seclass.jobcompare6300;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class AdjustWeightsActivity extends AppCompatActivity {

    private final int minWeight = 1;
    private final int maxWeight = 5;

    private Button bSaveWeights, bCancel;

    private EditText tbSalary, tbBonus, tbLeave, tbPLeave, tbLifeIns;


    private int argSalary, argBonus, argLeave, argPLeave, argLifeIns;

    private CompensationWeights newWeights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        extractWeights();
    }

    public void extractWeights() {
        bSaveWeights = (Button) findViewById(R.id.buttonSaveWeights);
        bCancel = (Button) findViewById(R.id.buttonCancel);

        tbSalary = (EditText) findViewById(R.id.editYearlySalaryWeight);
        tbBonus = (EditText) findViewById(R.id.editYearlyBonusWeight);
        tbLeave = (EditText) findViewById(R.id.editLeaveWeight);
        tbPLeave = (EditText) findViewById(R.id.editpLeaveWeight);
        tbLifeIns = (EditText) findViewById(R.id.editLifeInsWeight);

        newWeights = null;
        String key = "keyCompWeights";

        // Check if the Employee class hasExtra
        if (getIntent().hasExtra(key)) {
            newWeights = getIntent().getParcelableExtra(key);
        }
        else {
            newWeights = new CompensationWeights();
        }

        tbSalary.setText(Integer.toString(newWeights.getSalaryWeight()));
        tbBonus.setText(Integer.toString(newWeights.getBonusWeight()));
        tbLeave.setText(Integer.toString(newWeights.getLeaveWeight()));
        tbPLeave.setText(Integer.toString(newWeights.getPLeaveWeight()));
        tbLifeIns.setText(Integer.toString(newWeights.getLifeInsurWeight()));

    }

    public boolean checkInput() {
        boolean validInput = true;

        argSalary = Integer.parseInt(tbSalary.getText().toString());
        if (argSalary < minWeight || argSalary > maxWeight) {
            tbSalary.setError("Invalid Weight (Enter value between 1-5)");
            validInput = false;
        }

        argBonus = Integer.parseInt(tbBonus.getText().toString());
        if (argBonus < minWeight || argBonus > maxWeight) {
            tbBonus.setError("Invalid Weight (Enter value between 1-5)");
            validInput = false;
        }

        argLeave = Integer.parseInt(tbLeave.getText().toString());
        if (argLeave < minWeight || argLeave > maxWeight) {
            tbLeave.setError("Invalid Weight (Enter value between 1-5)");
            validInput = false;
        }

        argPLeave = Integer.parseInt(tbPLeave.getText().toString());
        if (argPLeave < minWeight || argPLeave > maxWeight) {
            tbPLeave.setError("Invalid Weight (Enter value between 1-5)");
            validInput = false;
        }

        argLifeIns = Integer.parseInt(tbLifeIns.getText().toString());
        if (argLifeIns < minWeight || argLifeIns > maxWeight) {
            tbLifeIns.setError("Invalid Weight (Enter value between 1-5)");
            validInput = false;
        }
        return validInput;
    }


    public void onClick(View view){

        if (view.getId() == bSaveWeights.getId()) {
            if(checkInput()){
                Intent saveWeightsIntent = new Intent(AdjustWeightsActivity.this, MainActivity.class);
                newWeights.adjustCompensationWeights(argSalary,argBonus,argLeave,argPLeave,argLifeIns);
                saveWeightsIntent.putExtra("keyCompWeights", newWeights);
                for (int i = 0; i < MainActivity.jobList.size(); i++) {
                    MainActivity.jobList.get(i).setWeights(newWeights);
                }
                MainActivity.jobDbHelper.addCompWeights(newWeights);

                startActivity(saveWeightsIntent);
            }
        } else if(view.getId() == bCancel.getId()) {
            Intent cancelChangesIntent = new Intent();
            setResult(Activity.RESULT_CANCELED,cancelChangesIntent);
            finish();
        }
    }

}
