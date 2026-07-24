package com.afyasalama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.afyasalama.models.Medication;
import com.afyasalama.models.WaterIntake;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AfyaSalama.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_MEDICATIONS = "medications";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DOSAGE = "dosage";
    private static final String COLUMN_TIME = "time";

    private static final String TABLE_WATER = "water_intake";
    private static final String COLUMN_WATER_ID = "id";
    private static final String COLUMN_WATER_AMOUNT = "amount";
    private static final String COLUMN_WATER_TIME = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEDS_TABLE = "CREATE TABLE " + TABLE_MEDICATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DOSAGE + " TEXT,"
                + COLUMN_TIME + " TEXT" + ")";
        db.execSQL(CREATE_MEDS_TABLE);

        String CREATE_WATER_TABLE = "CREATE TABLE " + TABLE_WATER + "("
                + COLUMN_WATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WATER_AMOUNT + " INTEGER,"
                + COLUMN_WATER_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_WATER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String CREATE_WATER_TABLE = "CREATE TABLE " + TABLE_WATER + "("
                    + COLUMN_WATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WATER_AMOUNT + " INTEGER,"
                    + COLUMN_WATER_TIME + " INTEGER" + ")";
            db.execSQL(CREATE_WATER_TABLE);
        }
    }

    public long addMedication(Medication medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, medication.getName());
        values.put(COLUMN_DOSAGE, medication.getDosage());
        values.put(COLUMN_TIME, medication.getTime());
        long id = db.insert(TABLE_MEDICATIONS, null, values);
        db.close();
        return id;
    }

    public List<Medication> getAllMedications() {
        List<Medication> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICATIONS, null);

        if (cursor.moveToFirst()) {
            do {
                Medication med = new Medication(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                list.add(med);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteMedication(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICATIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addWaterIntake(int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WATER_AMOUNT, amount);
        values.put(COLUMN_WATER_TIME, System.currentTimeMillis());
        db.insert(TABLE_WATER, null, values);
        db.close();
    }

    public int getTodayTotalIntake() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        long dayStart = getStartOfDay();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_WATER_AMOUNT + ") FROM " + TABLE_WATER 
                + " WHERE " + COLUMN_WATER_TIME + " >= ?", new String[]{String.valueOf(dayStart)});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    private long getStartOfDay() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
