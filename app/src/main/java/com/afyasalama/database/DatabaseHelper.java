package com.afyasalama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.afyasalama.models.Medication;
import com.afyasalama.models.DrugLabel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AfyaSalama.db";
    private static final int DATABASE_VERSION = 5;

    // Medications Table
    private static final String TABLE_MEDICATIONS = "medications";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DOSAGE = "dosage";
    private static final String COLUMN_TIME = "time";

    // Water Table
    private static final String TABLE_WATER = "water_intake";
    private static final String COLUMN_WATER_ID = "id";
    private static final String COLUMN_WATER_AMOUNT = "amount";
    private static final String COLUMN_WATER_TIME = "timestamp";

    // Steps Baseline Table
    private static final String TABLE_STEPS = "steps_baseline";
    private static final String COLUMN_STEPS_DATE = "date";
    private static final String COLUMN_STEPS_VALUE = "sensor_value";

    // Drug History Table
    private static final String TABLE_DRUG_HISTORY = "drug_history";
    private static final String COLUMN_DRUG_ID = "id";
    private static final String COLUMN_DRUG_BRAND = "brand_name";
    private static final String COLUMN_DRUG_GENERIC = "generic_name";
    private static final String COLUMN_DRUG_JSON = "json_data";

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "full_name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MEDICATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DOSAGE + " TEXT,"
                + COLUMN_TIME + " TEXT" + ")");

        db.execSQL("CREATE TABLE " + TABLE_WATER + "("
                + COLUMN_WATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WATER_AMOUNT + " INTEGER,"
                + COLUMN_WATER_TIME + " INTEGER" + ")");

        db.execSQL("CREATE TABLE " + TABLE_STEPS + "("
                + COLUMN_STEPS_DATE + " TEXT PRIMARY KEY,"
                + COLUMN_STEPS_VALUE + " INTEGER" + ")");

        db.execSQL("CREATE TABLE " + TABLE_DRUG_HISTORY + "("
                + COLUMN_DRUG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DRUG_BRAND + " TEXT,"
                + COLUMN_DRUG_GENERIC + " TEXT,"
                + COLUMN_DRUG_JSON + " TEXT" + ")");

        db.execSQL("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE " + TABLE_WATER + "("
                    + COLUMN_WATER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WATER_AMOUNT + " INTEGER,"
                    + COLUMN_WATER_TIME + " INTEGER" + ")");
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE " + TABLE_STEPS + "("
                    + COLUMN_STEPS_DATE + " TEXT PRIMARY KEY,"
                    + COLUMN_STEPS_VALUE + " INTEGER" + ")");
        }
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE " + TABLE_DRUG_HISTORY + "("
                    + COLUMN_DRUG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DRUG_BRAND + " TEXT,"
                    + COLUMN_DRUG_GENERIC + " TEXT,"
                    + COLUMN_DRUG_JSON + " TEXT" + ")");
        }
        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT,"
                    + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                    + COLUMN_USER_PASSWORD + " TEXT" + ")");
        }
    }

    // --- Medication Methods ---
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
                list.add(new Medication(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
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

    public void updateMedication(Medication medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, medication.getName());
        values.put(COLUMN_DOSAGE, medication.getDosage());
        values.put(COLUMN_TIME, medication.getTime());
        db.update(TABLE_MEDICATIONS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(medication.getId())});
        db.close();
    }

    // --- Water Methods ---
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

    // --- Steps Methods ---
    public int getStepsBaseline(String date) {
        int value = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STEPS, new String[]{COLUMN_STEPS_VALUE}, COLUMN_STEPS_DATE + "=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return value;
    }

    public void setStepsBaseline(String date, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEPS_DATE, date);
        values.put(COLUMN_STEPS_VALUE, value);
        db.insertWithOnConflict(TABLE_STEPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // --- Drug History Methods ---
    public void addDrugToHistory(DrugLabel drug) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRUG_HISTORY, COLUMN_DRUG_BRAND + " = ?", new String[]{drug.getBrandName()});
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUG_BRAND, drug.getBrandName());
        values.put(COLUMN_DRUG_GENERIC, drug.getGenericName());
        values.put(COLUMN_DRUG_JSON, new Gson().toJson(drug));
        db.insert(TABLE_DRUG_HISTORY, null, values);
        db.execSQL("DELETE FROM " + TABLE_DRUG_HISTORY + " WHERE " + COLUMN_DRUG_ID + " NOT IN " +
                "(SELECT " + COLUMN_DRUG_ID + " FROM " + TABLE_DRUG_HISTORY + " ORDER BY " + COLUMN_DRUG_ID + " DESC LIMIT 10)");
        db.close();
    }

    public List<DrugLabel> getSearchHistory() {
        List<DrugLabel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DRUG_JSON + " FROM " + TABLE_DRUG_HISTORY + " ORDER BY " + COLUMN_DRUG_ID + " DESC", null);
        Gson gson = new Gson();
        if (cursor.moveToFirst()) {
            do {
                list.add(gson.fromJson(cursor.getString(0), DrugLabel.class));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void clearSearchHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRUG_HISTORY, null, null);
        db.close();
    }

    // --- User Authentication Methods ---
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean success = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return success;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
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
