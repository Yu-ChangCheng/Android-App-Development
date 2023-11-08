package edu.gatech.seclass.jobcompare6300;

import android.os.Parcel;
import android.os.Parcelable;

public class CompensationWeights implements Parcelable{

    //Weights
    private int salary;
    private int bonus;
    private int leave;
    private int pLeave;
    private int lifeInsur;

    public CompensationWeights(int salaryWeight, int bonusWeight, int leaveWeight, int pLeaveWeight, int lifeInsurWeight) {
        this.salary = salaryWeight;
        this.bonus = bonusWeight;
        this.leave = leaveWeight;
        this.pLeave = pLeaveWeight;
        this.lifeInsur = lifeInsurWeight;
    }

    public CompensationWeights() {
        this.salary = 1;
        this.bonus = 1;
        this.leave = 1;
        this.pLeave = 1;
        this.lifeInsur = 1;
    }

    public CompensationWeights(CompensationWeights c) {
        this.salary = c.getSalaryWeight();
        this.bonus = c.getBonusWeight();
        this.leave = c.getLeaveWeight();
        this.pLeave = c.getPLeaveWeight();
        this.lifeInsur = c.getLifeInsurWeight();
    }

    public void adjustCompensationWeights(int salaryWeight, int bonusWeight, int leaveWeight, int pLeaveWeight, int lifeInsurWeight) {
        this.salary = salaryWeight;
        this.bonus = bonusWeight;
        this.leave = leaveWeight;
        this.pLeave = pLeaveWeight;
        this.lifeInsur = lifeInsurWeight;
    }

    public void adjustCompensationWeights(CompensationWeights c) {
        this.salary = c.getSalaryWeight();
        this.bonus = c.getBonusWeight();
        this.leave = c.getLeaveWeight();
        this.pLeave = c.getPLeaveWeight();
        this.lifeInsur = c.getLifeInsurWeight();
    }
    public int getSalaryWeight() { return salary; }
    public int getBonusWeight() { return bonus; }
    public int getLeaveWeight() { return leave; }
    public int getPLeaveWeight() {return pLeave; }
    public int getLifeInsurWeight() { return lifeInsur; }

    public void setSalaryWeight(int salary) { this.salary = salary; }
    public void setBonusWeight(int bonus) { this.bonus = bonus; }
    public void setLeaveWeight(int leave) { this.leave = leave; }
    public void setPLeaveWeight(int pLeave) {this.pLeave = pLeave; }
    public void setLifeInsurWeight(int lifeInsur) { this.lifeInsur = lifeInsur; }


    protected CompensationWeights(Parcel in) {
        salary = in.readInt();
        bonus = in.readInt();
        leave = in.readInt();
        pLeave = in.readInt();
        lifeInsur = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(salary);
        dest.writeInt(bonus);
        dest.writeInt(leave);
        dest.writeInt(pLeave);
        dest.writeInt(lifeInsur);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompensationWeights> CREATOR = new Creator<CompensationWeights>() {
        @Override
        public CompensationWeights createFromParcel(Parcel in) {
            return new CompensationWeights(in);
        }

        @Override
        public CompensationWeights[] newArray(int size) {
            return new CompensationWeights[size];
        }
    };
}
