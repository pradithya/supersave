package com.progrema.supersave.data.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.progrema.supersave.data.db.SupersaveContract;

import java.util.Calendar;

/**
 * Created by aria on 11/22/2014.
 */
public class SupersaveProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USER = 100;
    private static final int EXPENSE = 200;
    private static final int SAVING_GOAL = 300;
    private static final int CATEGORY = 400;

    private SupersaveDatabase dbOpenHelper;

    @Override
    public boolean onCreate() {
        dbOpenHelper = new SupersaveDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        final SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            default: {
                final SelectionBuilder builder = buildSelection(match);
                cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USER: {
                //add new user to user table
                //when we create new user, we create a new budget as well
                //and then link the new user to that budget

                //add new budget
                ContentValues newBudget = new ContentValues();
                newBudget.put(SupersaveContract.Budgets.INITIAL_AMOUNT, contentValues.getAsInteger(SupersaveContract.Budgets.INITIAL_AMOUNT));
                newBudget.put(SupersaveContract.Budgets.USED_AMOUNT, contentValues.getAsInteger(SupersaveContract.Budgets.USED_AMOUNT));
                newBudget.put(SupersaveContract.Budgets.START_DATE, contentValues.getAsLong(SupersaveContract.Budgets.START_DATE));
                newBudget.put(SupersaveContract.Budgets.END_DATE, contentValues.getAsLong(SupersaveContract.Budgets.END_DATE));
                long budgetID = db.insertOrThrow(SupersaveDatabase.Tables.BUDGET, null, newBudget);

                // add new user
                ContentValues newUser = new ContentValues();

                //remove piggyback data
                contentValues.remove(SupersaveContract.Budgets.INITIAL_AMOUNT);
                contentValues.remove(SupersaveContract.Budgets.USED_AMOUNT);
                contentValues.remove(SupersaveContract.Budgets.START_DATE);
                contentValues.remove(SupersaveContract.Budgets.END_DATE);
                contentValues.put(SupersaveContract.User.ACTIVE_BUDGET_ID, budgetID);
                db.insertOrThrow(SupersaveDatabase.Tables.USER, null, contentValues);
                return SupersaveContract.User.buildUri();
            }

            case EXPENSE: {
                // add new expense to expenses table
                db.insertOrThrow(SupersaveDatabase.Tables.EXPENSE, null, contentValues);
                return SupersaveContract.Expenses.buildUri();
            }

            case SAVING_GOAL: {
                // add saving goal to saving goals table
                db.insertOrThrow(SupersaveDatabase.Tables.SAVING_GOAL, null, contentValues);
                return SupersaveContract.Savinggoals.buildUri();
            }
            case CATEGORY: {
                db.insertOrThrow(SupersaveDatabase.Tables.EXPENSE_CATEGORY, null, contentValues);
                return SupersaveContract.Categories.buildUri();
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int returnValue;
        SQLiteDatabase table;
        SelectionBuilder builder;

        // Delete specified data from user
        table = dbOpenHelper.getWritableDatabase();
        builder = buildSelection(sUriMatcher.match(uri));
        returnValue = builder.where(selection, selectionArgs).delete(table);

        // Notify user specified table and activity table
        notifyChange(uri);
        return returnValue;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(sUriMatcher.match(uri));
        int retVal = builder.where(selection, selectionArgs).update(db, contentValues);
        notifyChange(uri);

        return retVal;
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }

    private SelectionBuilder buildSelection(int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case USER:
                return builder.table(SupersaveDatabase.Tables.USER);
            case EXPENSE:
                return builder.table(SupersaveDatabase.Tables.EXPENSE);
            case SAVING_GOAL:
                return builder.table(SupersaveDatabase.Tables.SAVING_GOAL);
            case CATEGORY:
                return builder.table(SupersaveDatabase.Tables.EXPENSE_CATEGORY);
            default:
                return null;
        }
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SupersaveContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SupersaveContract.PATH_USER, USER);
        matcher.addURI(authority, SupersaveContract.PATH_EXPENSE, EXPENSE);
        matcher.addURI(authority, SupersaveContract.PATH_SAVING_GOAL, SAVING_GOAL);
        matcher.addURI(authority, SupersaveContract.PATH_EXPENSE_CATEGORY, CATEGORY);


        return matcher;
    }

    private long getCurrentTime() {
        Calendar now = Calendar.getInstance();
        return now.getTimeInMillis();
    }
}
