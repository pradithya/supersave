package com.progrema.supersave.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import com.progrema.supersave.data.db.SupersaveContract.*;

/**
 * Created by aria on 11/22/2014.
 */
public class SupersaveDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "supersave.db";
    /**
     * NOTE:
     * carefully edit onUpgrade() when bumping database versions to make
     * sure user data is saved.
     */
    private static final int VER_2015_01 = 110; // 1.1
    private static final int DATABASE_VERSION = VER_2015_01;
    private final Context context;

    private static final String PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TEXT_TYPE = " TEXT NOT NULL";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";

    /*old database name*/
    private static final String TABLE_CASH = "TableCash";
    private static final String TABLE_SPENDING_ITEM = "TableSpending";
    private static final String TABLE_WISH_ITEM = "TableWishItem";
    private static final String TABLE_BUDGET = "TableBudget";

    public SupersaveDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static void deleteDataBase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.USER + " ("
                + BaseColumns._ID + PRIMARY_KEY_TYPE + ","
                + UserColumns.USER_NAME + TEXT_TYPE + ","
                + UserColumns.DEFAULT_CURRENCY + TEXT_TYPE + ","
                + UserColumns.DEFAULT_MONTHLY_BUDGET + REAL_TYPE + ","
                + UserColumns.CYCLE_DATE + INT_TYPE + ","
                + UserColumns.ACTIVE_BUDGET_ID + INT_TYPE + ","
                + UserColumns.TOTAL_SAVING + REAL_TYPE + ","
                + " FOREIGN KEY (" + UserColumns.ACTIVE_BUDGET_ID + ") REFERENCES "
                + Tables.BUDGET + " (" + BaseColumns._ID + ")"
                + " )");

        db.execSQL("CREATE TABLE " + Tables.BUDGET + " ("
                + BaseColumns._ID + PRIMARY_KEY_TYPE + ","
                + BudgetColumns.INITIAL_AMOUNT + REAL_TYPE + ","
                + BudgetColumns.USED_AMOUNT + REAL_TYPE + ","
                + BudgetColumns.START_DATE + INT_TYPE + ","
                + BudgetColumns.END_DATE + INT_TYPE
                +")");

        db.execSQL("CREATE TABLE " + Tables.EXPENSE + " ("
                + BaseColumns._ID + PRIMARY_KEY_TYPE + ","
                + ExpenseColumns.BUDGET_ID + INT_TYPE + ","
                + ExpenseColumns.TIMESTAMP + TEXT_TYPE + ","
                + ExpenseColumns.CURRENCY + TEXT_TYPE + ","
                + ExpenseColumns.EXCHANGE_RATE + REAL_TYPE + ","
                + ExpenseColumns.AMOUNT + REAL_TYPE + ","
                + ExpenseColumns.CATEGORY_ID + INT_TYPE + ","
                + ExpenseColumns.OCCURRENCE + INT_TYPE + ","
                + ExpenseColumns.NOTES + TEXT_TYPE + ","
                + " FOREIGN KEY (" + ExpenseColumns.BUDGET_ID + ") REFERENCES "
                + Tables.BUDGET + " (" + BaseColumns._ID + ")"  + ","
                + " FOREIGN KEY (" + ExpenseColumns.CATEGORY_ID + ") REFERENCES "
                + Tables.EXPENSE_CATEGORY + " (" + BaseColumns._ID + ")"
                + ")");

        db.execSQL("CREATE TABLE " + Tables.EXPENSE_CATEGORY + " ("
                + BaseColumns._ID + PRIMARY_KEY_TYPE + ","
                + CategoryColumns.CATEGORY_NAME + TEXT_TYPE +")");

        db.execSQL("CREATE TABLE " + Tables.SAVING_GOAL + " ("
                + BaseColumns._ID + PRIMARY_KEY_TYPE + ","
                + SavinggoalColumns.TIMESTAMP + TEXT_TYPE + ","
                + SavinggoalColumns.NAME + TEXT_TYPE + ","
                + SavinggoalColumns.CURRENCY + TEXT_TYPE + ","
                + SavinggoalColumns.EXCHANGE_RATE + REAL_TYPE + ","
                + SavinggoalColumns.PRICE + REAL_TYPE + ","
                + SavinggoalColumns.STATUS + INT_TYPE + ","
                + SavinggoalColumns.START_DATE + TEXT_TYPE + ","
                + SavinggoalColumns.TARGET_DATE + TEXT_TYPE + ","
                + SavinggoalColumns.CATEGORY_ID + INT_TYPE + ","
                + SavinggoalColumns.NOTES + TEXT_TYPE + ","
                + " FOREIGN KEY (" + SavinggoalColumns.CATEGORY_ID + ") REFERENCES "
                + Tables.EXPENSE_CATEGORY + " (" + BaseColumns._ID + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        //have to delete old db (Supersave v1.0)
        if(db.getVersion() < VER_2015_01) {
            //drop all old db
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPENDING_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISH_ITEM);
        }
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //TODO: reactivate foreign key after debugging
            db.setForeignKeyConstraintsEnabled(false);
        }
        return db;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //TODO: reactivate foreign key after debugging
            db.setForeignKeyConstraintsEnabled(false);
        }
        return db;
    }

    interface Tables {
        String USER = "user";
        String SAVING_GOAL = "saving_goal";
        String EXPENSE = "expense";
        String EXPENSE_CATEGORY = "category";
        String BUDGET = "budget";
    }

    private interface TriggersName {

    }

    private interface Qualified {

    }

}
