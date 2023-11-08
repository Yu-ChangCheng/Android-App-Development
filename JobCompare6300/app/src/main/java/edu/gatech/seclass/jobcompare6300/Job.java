package edu.gatech.seclass.jobcompare6300;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Job implements Parcelable {
    private String title;
    private String company;
    private String location;
    private int CoL;
    private double salary;
    private double bonus;
    private int leave;
    private int pLeave;
    private int lifeInsur;
    private double score;

    public static int weightYearlySalary = 1;
    public static int weightYearlyBonus = 1;
    public static int weightLeave = 1;
    public static int weightParentalLeave = 1;
    public static int weightLifeInsurance = 1;

    private boolean isSelected;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Job job = (Job) obj;
        return Double.compare(job.salary, salary) == 0 &&
                Double.compare(job.bonus, bonus) == 0 &&
                leave == job.leave &&
                pLeave == job.pLeave &&
                lifeInsur == job.lifeInsur &&
                CoL == job.CoL &&
                title.equals(job.title) &&
                company.equals(job.company) &&
                location.equals(job.location);
    }

    public Job(String title, String company, String location, int col, double salary, double bonus, int leave, int pLeave, int lifeInsur) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.CoL = col;
        this.leave = leave;
        this.pLeave = pLeave;
        this.lifeInsur = lifeInsur;
        this.score = calculateScore();
        BigDecimal salary_bd = new BigDecimal(salary).setScale(2, RoundingMode.HALF_UP);
        this.salary = salary_bd.doubleValue();
        BigDecimal bonus_bd = new BigDecimal(bonus).setScale(2, RoundingMode.HALF_UP);
        this.bonus = bonus_bd.doubleValue();
        this.score = calculateScore();
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double calculateScore() {
        double AYS = salary / CoL;
        double AYB = bonus / CoL;
        double adjustedLeave = leave * (AYS / 260);
        double adjustedParentalLeave = pLeave * (AYS / 260);
        double adjustedLifeInsurance = (lifeInsur / 100.0) * AYS;

        double totalWeight = weightYearlySalary + weightYearlyBonus + weightLeave + weightParentalLeave + weightLifeInsurance;

        double score = (weightYearlySalary / totalWeight) * AYS
                + (weightYearlyBonus / totalWeight) * AYB
                + (weightLeave / totalWeight) * adjustedLeave
                + (weightParentalLeave / totalWeight) * adjustedParentalLeave
                + (weightLifeInsurance / totalWeight) * adjustedLifeInsurance;

        BigDecimal score_bd = new BigDecimal(score).setScale(1, RoundingMode.HALF_UP);
        this.score = score_bd.doubleValue();
        return this.score; // Round to the nearest integer for display
    }

    protected Job(Parcel in) {
        title = in.readString();
        company = in.readString();
        location = in.readString();
        CoL = in.readInt();
        BigDecimal salary_bd = new BigDecimal(in.readDouble()).setScale(2, RoundingMode.HALF_UP);
        salary = salary_bd.doubleValue();
        BigDecimal bonus_bd = new BigDecimal(in.readDouble()).setScale(2, RoundingMode.HALF_UP);
        bonus = bonus_bd.doubleValue();
        leave = in.readInt();
        pLeave = in.readInt();
        lifeInsur = in.readInt();
        score = in.readDouble();
    }

    // Copy Constructor
    protected Job(Job j) {
        title = j.getTitle();
        company = j.getCompany();
        location = j.getLocation();
        CoL = j.getCostOfLiving();
        salary = j.getSalary();
        bonus = j.getBonus();
        leave = j.getLeave();
        pLeave = j.getParentalLeave();
        lifeInsur = j.getLifeInsurancePercentage();
        score = j.getScore();
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public int getCostOfLiving() {
        return CoL;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public int getLeave() {
        return leave;
    }

    public int getParentalLeave() {
        return pLeave;
    }

    public int getLifeInsurancePercentage() {
        return lifeInsur;
    }

    public double getScore() {
        return score;
    }

    public void setWeights(CompensationWeights weights) {
        this.weightYearlySalary = weights.getSalaryWeight();
        this.weightYearlyBonus = weights.getBonusWeight();
        this.weightLeave = weights.getLeaveWeight();
        this.weightParentalLeave = weights.getPLeaveWeight();
        this.weightLifeInsurance = weights.getLifeInsurWeight();

        calculateScore();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(company);
        parcel.writeString(location);
        parcel.writeInt(CoL);
        parcel.writeDouble(salary);
        parcel.writeDouble(bonus);
        parcel.writeInt(leave);
        parcel.writeInt(pLeave);
        parcel.writeInt(lifeInsur);
        parcel.writeDouble(score);
    }
}