package com.example.arafathossain.icare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "iCare";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProfileTable.CREATE_TABLE_QUERY);
        db.execSQL(DietTable.CREATE_TABLE_QUERY);
        db.execSQL(MedicalTable.CREATE_MEDICAL_TABLE);
        db.execSQL(HealthTable.CREATE_HEALTH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ProfileTable.DROP_TABLE_QUERY);
        db.execSQL(DietTable.DROP_TABLE_QUERY);
        db.execSQL(ProfileTable.CREATE_TABLE_QUERY);
        db.execSQL(DietTable.CREATE_TABLE_QUERY);
        db.execSQL(MedicalTable.UPGRADE_MEDICAL_TABLE);
        db.execSQL(HealthTable.UPGRADE_HEALTH_TABLE);
    }

    public ArrayList<String> getAllProfileName() {
        ArrayList<String> profileNames = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ProfileTable.TABLE_NAME, new String[]{ProfileTable.COLUMN_PROFILE_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            profileNames = new ArrayList<>();
            do {
                profileNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return profileNames;
    }

    public Profile getProfileByName(String profileName) {
        Profile userProfile = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ProfileTable.TABLE_NAME, null, ProfileTable.COLUMN_PROFILE_NAME + "=?", new String[]{profileName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                userProfile = new Profile();
                userProfile.setBloodGroup(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_BLOOD_GROUP)));
                userProfile.setContactNo(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_CONTACT_NO)));
                userProfile.setEmail(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_EMAIL)));
                userProfile.setDateOfBirth(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_DATE_OF_BIRTH)));
                userProfile.setUserName(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_USER_NAME)));
                userProfile.setWeight(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_WEIGHT)));
                userProfile.setHeight(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_HEIGHT)));
                userProfile.setGender(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_GENDER)));
                userProfile.setProfileName(cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_PROFILE_NAME)));
                userProfile.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            } while (cursor.moveToNext());
        }
        return userProfile;
    }

    public int addProfile(Profile profile) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfileTable.COLUMN_PROFILE_NAME, profile.getProfileName());
        values.put(ProfileTable.COLUMN_USER_NAME, profile.getUserName());
        values.put(ProfileTable.COLUMN_EMAIL, profile.getEmail());
        values.put(ProfileTable.COLUMN_CONTACT_NO, profile.getContactNo());
        values.put(ProfileTable.COLUMN_DATE_OF_BIRTH, profile.getDateOfBirth());
        values.put(ProfileTable.COLUMN_WEIGHT, profile.getWeight());
        values.put(ProfileTable.COLUMN_HEIGHT, profile.getHeight());
        values.put(ProfileTable.COLUMN_BLOOD_GROUP, profile.getBloodGroup());
        values.put(ProfileTable.COLUMN_GENDER, profile.getGender());
        try {
            db.insertOrThrow(ProfileTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public int removeProfile(String profileName) {
        SQLiteDatabase db = getWritableDatabase();
        int row = db.delete(ProfileTable.TABLE_NAME, ProfileTable.COLUMN_PROFILE_NAME + "=?", new String[]{profileName});
        int row1 = db.delete(DietTable.TABLE_NAME, DietTable.COLUMN_PROFILE_NAME + "=?", new String[]{profileName});
        if (row>0&&row1>0) return 1;
        return 0;
    }

    public ArrayList<Integer> getAllDietIdByProfileName(String profileName) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> idList = null;
        Cursor cursor = db.query(DietTable.TABLE_NAME, new String[]{DietTable.COLUMN_ID}, DietTable.COLUMN_PROFILE_NAME + "=?", new String[]{profileName}, null, null, null);
        if (cursor.moveToFirst()) {
            idList = new ArrayList<Integer>();
            do {
                idList.add(cursor.getInt(cursor.getColumnIndex(DietTable.COLUMN_ID)));
            } while (cursor.moveToNext());
            Log.d("sizeid",idList.size()+"");
        }
        return idList;
    }

    public int updateProfile(ContentValues values, int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(ProfileTable.TABLE_NAME, values, "_id=?", new String[]{String.valueOf(id)});
    }

    public boolean checkProfileName(String profileName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(ProfileTable.TABLE_NAME, new String[]{"_id"}, ProfileTable.COLUMN_PROFILE_NAME + "=?", new String[]{profileName}, null, null, null);
        if (cursor == null) return false;
        else return true;
    }

    public int addDietInformation(DietInformation diet) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DietTable.COLUMN_DAY, diet.getDay());
        values.put(DietTable.COLUMN_MENU, diet.getMenu());
        values.put(DietTable.COLUMN_TIME, diet.getTime());
        values.put(DietTable.COLUMN_TITLE, diet.getTitle());
        values.put(DietTable.COLUMN_REMINDER, diet.getReminder());
        values.put(DietTable.COLUMN_PROFILE_NAME, diet.getProfileName());

        int i = (int) db.insert(DietTable.TABLE_NAME, null, values);

        return i;
    }

    public int updateDiet(ContentValues values, int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(DietTable.TABLE_NAME, values, DietTable.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<DietInformation> getDietList(String weekDay, String profileName) {
        ArrayList<DietInformation> dietList = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DietTable.TABLE_NAME, null, DietTable.COLUMN_DAY + "=? AND " + DietTable.COLUMN_PROFILE_NAME + "=?", new String[]{weekDay, profileName}, null, null, null);
        if (cursor.moveToFirst()) {
            dietList = new ArrayList<>();
            do {
                DietInformation diet = new DietInformation();
                diet.setId(cursor.getInt(cursor.getColumnIndex(DietTable.COLUMN_ID)));
                diet.setTitle(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_TITLE)));
                diet.setTime(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_TIME)));
                diet.setReminder(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_REMINDER)));
                diet.setMenu(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_MENU)));
                diet.setProfileName(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_PROFILE_NAME)));
                diet.setDay(cursor.getString(cursor.getColumnIndex(DietTable.COLUMN_DAY)));
                dietList.add(diet);
            } while (cursor.moveToNext());
        }
        return dietList;
    }

    public int removeDiet(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int numRow = db.delete(DietTable.TABLE_NAME, DietTable.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return numRow;
    }

    public class DietTable {
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_PROFILE_NAME = "profile_name";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_REMINDER = "reminder";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MENU = "menu";
        public static final String TABLE_NAME = "diet_informtion";
        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PROFILE_NAME + " TEXT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_MENU + " TEXT," +
                COLUMN_DAY + " TEXT," +
                COLUMN_REMINDER + " TEXT)";
        public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public class ProfileTable {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_PROFILE_NAME = "profile_name";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_CONTACT_NO = "contact_no";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_BLOOD_GROUP = "blood_group";
        public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_GENDER = "gender";
        public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PROFILE_NAME + " TEXT NOT NULL," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_CONTACT_NO + " TEXT," +
                COLUMN_DATE_OF_BIRTH + " TEXT," +
                COLUMN_WEIGHT + " TEXT," +
                COLUMN_HEIGHT + " TEXT," +
                COLUMN_BLOOD_GROUP + " TEXT," +
                COLUMN_GENDER + " TEXT)";
        public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
