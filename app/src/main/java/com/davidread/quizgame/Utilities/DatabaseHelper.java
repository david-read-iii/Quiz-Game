package com.davidread.quizgame.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.davidread.quizgame.Models.Result;
import com.davidread.quizgame.Models.User;

import java.util.ArrayList;

/**
 * This utility class provides functions to manipulate the user and result objects stored in an
 * SQLite database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";

    /**
     * Constructs a database helper with context from the activity where the object is created.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the object is initially created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users and results table.
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Result.CREATE_TABLE);
    }

    /**
     * Called when the database is upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older tables if they exist.
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Result.TABLE_NAME);

        // Create tables again.
        onCreate(db);
    }

    /**
     * Inserts a user object with the following attributes into the database. Returns the id of the
     * inserted user object.
     */
    public long insertUser(String firstName, String lastName, String dateOfBirth, String email, String password) {

        // Get writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Setup row values in a ContentValues object.
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_FIRST_NAME, firstName);
        values.put(User.COLUMN_LAST_NAME, lastName);
        values.put(User.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PASSWORD, password);

        // Insert row into database and get id of row.
        long id = db.insert(User.TABLE_NAME, null, values);

        // Close database.
        db.close();

        // Return row id.
        return id;
    }

    /**
     * Returns a user object stored in the database given its id. Will return a null object if user
     * is not in the database.
     */
    public User getUser(long id) {

        User user = null;

        // Get readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for a user object with the given id.
        Cursor cursor = db.query(
                User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_FIRST_NAME, User.COLUMN_LAST_NAME, User.COLUMN_DATE_OF_BIRTH, User.COLUMN_EMAIL, User.COLUMN_PASSWORD},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null
        );

        // Extract the user object from the cursor if a non-null result was returned.
        if (cursor != null) {
            cursor.moveToFirst();

            user = new User(
                    cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_DATE_OF_BIRTH)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD))
            );

            cursor.close();
        }

        // Close the database.
        db.close();

        return user;
    }

    /**
     * Returns the id of the user object with the given email/password combo. Will return -1 if no
     * combo is found.
     */
    public long getUserId(String email, String password) {

        long id = -1;

        // Get readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for a user object with the given email/password combo.
        String query = "SELECT " + User.COLUMN_ID +
                " FROM " + User.TABLE_NAME +
                " WHERE " + User.COLUMN_EMAIL + " =  ? AND " + User.COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        // Extract the id of the user object from the cursor if a non-null result was returned.
        if (cursor != null) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                id = cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID));
            }

            cursor.close();
        }

        // Close the database.
        db.close();

        return id;
    }

    /**
     * Deletes a user object from the database given its id.
     */
    public void deleteUser(long id) {

        // Get writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the user object with the given id from the database.
        db.delete(
                User.TABLE_NAME,
                User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        // Close database.
        db.close();
    }

    /**
     * Returns an array list containing all stored user objects.
     */
    public ArrayList<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();

        // Get readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all result object entries.
        String query = "SELECT * FROM " + User.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows and add to array list.
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(User.COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(User.COLUMN_LAST_NAME)));
                user.setDateOfBirth(cursor.getString(cursor.getColumnIndex(User.COLUMN_DATE_OF_BIRTH)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));
                users.add(user);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Close database.
        db.close();

        return users;
    }

    /**
     * Returns true if given email is already taken by a user object in the database.
     */
    public boolean isEmailTakenByUser(String email) {

        boolean taken = false;

        // Get readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for a user object with the given email.
        String query = "SELECT 1 FROM " + User.TABLE_NAME + " WHERE " + User.COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Extract the result of the query from the cursor if it is non-null.
        if (cursor != null) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0)
                taken = true;

            cursor.close();
        }

        // Close the database.
        db.close();

        return taken;
    }

    /**
     * Inserts a result object with the following attributes into the database.
     */
    public void insertResult(long userId, int countQuestionsCorrect, int countTotalQuestions) {

        // Get writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Setup row values in a content values object.
        ContentValues values = new ContentValues();
        values.put(Result.COLUMN_USER_ID, userId);
        values.put(Result.COLUMN_TIMESTAMP, System.currentTimeMillis());
        values.put(Result.COLUMN_COUNT_QUESTIONS_CORRECT, countQuestionsCorrect);
        values.put(Result.COLUMN_COUNT_TOTAL_QUESTIONS, countTotalQuestions);

        // Insert row into database.
        db.insert(Result.TABLE_NAME, null, values);

        // Close database.
        db.close();
    }

    /**
     * Deletes the result objects from the database given their user ids.
     */
    public void deleteResultsForUser(long userId) {

        // Get writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the result objects with the specified user ids from the database.
        db.delete(
                Result.TABLE_NAME,
                Result.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        // Close database.
        db.close();
    }

    /**
     * Returns an array list containing all stored result objects. Sorts the array list by timestamp
     * descending.
     */
    public ArrayList<Result> getAllResults() {

        ArrayList<Result> results = new ArrayList<>();

        // Get readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all result object entries.
        String query = "SELECT * FROM " + Result.TABLE_NAME + " ORDER BY " + Result.COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows and add to array list.
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setUserId(cursor.getLong(cursor.getColumnIndex(Result.COLUMN_USER_ID)));
                result.setTimestamp(cursor.getLong(cursor.getColumnIndex(Result.COLUMN_TIMESTAMP)));
                result.setCountQuestionsCorrect(cursor.getInt(cursor.getColumnIndex(Result.COLUMN_COUNT_QUESTIONS_CORRECT)));
                result.setCountTotalQuestions(cursor.getInt(cursor.getColumnIndex(Result.COLUMN_COUNT_TOTAL_QUESTIONS)));
                results.add(result);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Close database.
        db.close();

        return results;
    }
}