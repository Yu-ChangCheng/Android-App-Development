package edu.gatech.seclass.jobcompare6300;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.File;
import java.util.ArrayList;

public class JobDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_PATH = "/data/data/edu.gatech.seclass.jobcompare6300/databases/";
    private static final String DATABASE_NAME = "JobDb.db";
    private static final String JOB_TABLE_NAME = "Job";
    private static final String CURRENT_JOB_TABLE_NAME = "CurrentJob";
    private static final String WEIGHTS_TABLE_NAME = "Weights";
    private static final int DATABASE_VERSION = 1;

    public JobDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addJob(Job job, boolean currentJob) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", job.getTitle());
        values.put("company", job.getCompany());
        values.put("location", job.getLocation());
        values.put("costOfLiving", job.getCostOfLiving());
        values.put("salary", job.getSalary());
        values.put("bonus", job.getBonus());
        values.put("leave", job.getLeave());
        values.put("parentalLeave", job.getParentalLeave());
        values.put("lifeInsurancePercentage", job.getLifeInsurancePercentage());

        if (currentJob) {
            Cursor cursor = db.rawQuery("SELECT  * FROM " + CURRENT_JOB_TABLE_NAME, null);
            int count = cursor.getCount();
            cursor.close();

            if(count >= 1) {
                db.execSQL("DELETE from " + CURRENT_JOB_TABLE_NAME);
            }
            db.insert(CURRENT_JOB_TABLE_NAME, null, values);
        } else {
            db.insert(JOB_TABLE_NAME, null, values);
        }
        db.close();
    }

    public void addCompWeights(CompensationWeights compensationWeights) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT  * FROM " + WEIGHTS_TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();

        if(count >= 1) {
            db.execSQL("DELETE from " + WEIGHTS_TABLE_NAME);
        }

        ContentValues values = new ContentValues();
        values.put("salary", compensationWeights.getSalaryWeight());
        values.put("bonus", compensationWeights.getBonusWeight());
        values.put("leave", compensationWeights.getLeaveWeight());
        values.put("parentalLeave", compensationWeights.getPLeaveWeight());
        values.put("lifeInsurance", compensationWeights.getLifeInsurWeight());

        db.insert(WEIGHTS_TABLE_NAME, null, values);
        db.close();
    }

    public void createTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create the job table
        String SQL_CREATE_JOB_TABLE = "CREATE TABLE " + JOB_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "company TEXT NOT NULL, "
                + "location TEXT NOT NULL, "
                + "costOfLiving INTEGER NOT NULL, "
                + "salary REAL NOT NULL, "
                + "bonus REAL NOT NULL, "
                + "leave INTEGER NOT NULL, "
                + "parentalLeave INTEGER NOT NULL, "
                + "lifeInsurancePercentage INTEGER NOT NULL);";

        String SQL_CREATE_CURRENT_JOB_TABLE = "CREATE TABLE " + CURRENT_JOB_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "company TEXT NOT NULL, "
                + "location TEXT NOT NULL, "
                + "costOfLiving INTEGER NOT NULL, "
                + "salary REAL NOT NULL, "
                + "bonus REAL NOT NULL, "
                + "leave INTEGER NOT NULL, "
                + "parentalLeave INTEGER NOT NULL, "
                + "lifeInsurancePercentage INTEGER NOT NULL);";

        String SQL_CREATE_WEIGHTS_TABLE = "CREATE TABLE " + WEIGHTS_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "salary INTEGER NOT NULL, "
                + "bonus INTEGER NOT NULL, "
                + "leave INTEGER NOT NULL, "
                + "parentalLeave INTEGER NOT NULL, "
                + "lifeInsurance INTEGER NOT NULL);";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_JOB_TABLE);
        db.execSQL(SQL_CREATE_CURRENT_JOB_TABLE);
        db.execSQL(SQL_CREATE_WEIGHTS_TABLE);

        CompensationWeights defaultCompWeights = new CompensationWeights();
        addCompWeights(defaultCompWeights);

        db.close();
    }

    public void startDB() {
        File databaseFile = new File(DATABASE_PATH + DATABASE_NAME);

        if (databaseFile.exists()) {
            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            qb.setTables(JOB_TABLE_NAME);
            Cursor cursorJobs = db.rawQuery("SELECT * FROM " + JOB_TABLE_NAME, null);

            ArrayList<Job> databaseJobList = new ArrayList<>();
            if (cursorJobs.moveToFirst()) {
                do {
                    databaseJobList.add(new Job(
                            cursorJobs.getString(1),
                            cursorJobs.getString(2),
                            cursorJobs.getString(3),
                            cursorJobs.getInt(4),
                            cursorJobs.getDouble(5),
                            cursorJobs.getDouble(6),
                            cursorJobs.getInt(7),
                            cursorJobs.getInt(8),
                            cursorJobs.getInt(9)));
                } while (cursorJobs.moveToNext());
            }
            MainActivity.jobList = databaseJobList;
            cursorJobs.close();

            qb.setTables(CURRENT_JOB_TABLE_NAME);
            Cursor cursorJobOffer = db.rawQuery("SELECT * FROM " + CURRENT_JOB_TABLE_NAME, null);

            if (cursorJobOffer.moveToFirst()) {
                do {
                    MainActivity.currentJob = new Job(
                            cursorJobOffer.getString(1),
                            cursorJobOffer.getString(2),
                            cursorJobOffer.getString(3),
                            cursorJobOffer.getInt(4),
                            cursorJobOffer.getDouble(5),
                            cursorJobOffer.getDouble(6),
                            cursorJobOffer.getInt(7),
                            cursorJobOffer.getInt(8),
                            cursorJobOffer.getInt(9));
                } while (cursorJobOffer.moveToNext());
            }

            MainActivity.jobList.add(MainActivity.currentJob);
            cursorJobOffer.close();

            qb.setTables(WEIGHTS_TABLE_NAME);
            Cursor cursorWeights = db.rawQuery("SELECT * FROM " + WEIGHTS_TABLE_NAME, null);

            if (cursorWeights.moveToFirst()) {
                do {
                    MainActivity.compWeights = new CompensationWeights(
                            cursorWeights.getInt(1),
                            cursorWeights.getInt(2),
                            cursorWeights.getInt(3),
                            cursorWeights.getInt(4),
                            cursorWeights.getInt(5));
                } while (cursorWeights.moveToNext());
            }
            cursorWeights.close();
        } else {
            createTable();
        }
    }

    public void cleanDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(JOB_TABLE_NAME);
        db.execSQL("DELETE from " + JOB_TABLE_NAME);

        qb.setTables(CURRENT_JOB_TABLE_NAME);
        db.execSQL("DELETE from " + CURRENT_JOB_TABLE_NAME);

        qb.setTables(WEIGHTS_TABLE_NAME);
        db.execSQL("DELETE from " + WEIGHTS_TABLE_NAME);

        db.close();
    }
}
