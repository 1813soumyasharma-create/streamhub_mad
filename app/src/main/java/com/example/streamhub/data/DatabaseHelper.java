package com.example.streamhub.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "streamhub.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_HISTORY = "history";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_USERS = "users";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_SUBTITLE = "subtitle";
    public static final String COL_TIMESTAMP = "timestamp";

    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_DOB = "dob";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_HISTORY + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT, " + COL_SUBTITLE + " TEXT, " + COL_TIMESTAMP + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT, " + COL_SUBTITLE + " TEXT, " + COL_TIMESTAMP + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT, " + COL_SUBTITLE + " TEXT, " + COL_TIMESTAMP + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COL_EMAIL + " TEXT PRIMARY KEY, " + COL_PASSWORD + " TEXT, " + COL_DOB + " TEXT)");

        // Insert sample user
        ContentValues cvUser = new ContentValues();
        cvUser.put(COL_EMAIL, "user@streamhub.com");
        cvUser.put(COL_PASSWORD, "password123");
        cvUser.put(COL_DOB, "01/01/2000");
        db.insertWithOnConflict(TABLE_USERS, null, cvUser, SQLiteDatabase.CONFLICT_IGNORE);

        // Insert sample history and notes
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, "Space Facts for Kids");
        cv.put(COL_SUBTITLE, "Watched today - 1:24 / 4:00");
        cv.put(COL_TIMESTAMP, "Today");
        db.insert(TABLE_HISTORY, null, cv);

        ContentValues cvNote = new ContentValues();
        cvNote.put(COL_TITLE, "Accessibility Study Notes");
        cvNote.put(COL_SUBTITLE, "ASL sign language gestures and color contrast rules.");
        cvNote.put(COL_TIMESTAMP, "Yesterday");
        db.insert(TABLE_NOTES, null, cvNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String email, String pass, String dob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EMAIL, email.trim().toLowerCase());
        cv.put(COL_PASSWORD, pass);
        cv.put(COL_DOB, dob);
        long result = db.insertWithOnConflict(TABLE_USERS, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1L;
    }

    public boolean validateLogin(String email, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?", new String[]{email.trim().toLowerCase(), pass});
        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email.trim().toLowerCase()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public long addHistory(String title, String subtitle, String timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_SUBTITLE, subtitle);
        cv.put(COL_TIMESTAMP, timestamp);
        return db.insert(TABLE_HISTORY, null, cv);
    }

    public int importCsvData(String csvData) {
        int count = 0;
        if (csvData == null || csvData.trim().isEmpty()) return 0;
        String[] lines = csvData.split("\n");
        SQLiteDatabase db = getWritableDatabase();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                ContentValues cv = new ContentValues();
                cv.put(COL_TITLE, parts[0].trim());
                cv.put(COL_SUBTITLE, parts[1].trim());
                cv.put(COL_TIMESTAMP, parts[2].trim());
                db.insert(TABLE_HISTORY, null, cv);
                count++;
            }
        }
        return count;
    }

    public String exportCsvData() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title,Subtitle,Timestamp\n");
        List<Triple> list = getAllHistory();
        for (Triple item : list) {
            sb.append(item.getFirst()).append(",")
              .append(item.getSecond()).append(",")
              .append(item.getThird()).append("\n");
        }
        return sb.toString();
    }

    public long addNote(String title, String content, String timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_SUBTITLE, content);
        cv.put(COL_TIMESTAMP, timestamp);
        return db.insert(TABLE_NOTES, null, cv);
    }

    public void clearHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
    }

    public List<Triple> getAllHistory() {
        List<Triple> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_TITLE + ", " + COL_SUBTITLE + ", " + COL_TIMESTAMP + " FROM " + TABLE_HISTORY, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Triple(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static class Triple {
        private final String first;
        private final String second;
        private final String third;

        public Triple(String first, String second, String third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public String getFirst() { return first; }
        public String getSecond() { return second; }
        public String getThird() { return third; }
    }
}
