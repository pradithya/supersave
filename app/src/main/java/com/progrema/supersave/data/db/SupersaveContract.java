package com.progrema.supersave.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aria on 11/22/2014.
 */
public class SupersaveContract {

    public static final String CONTENT_AUTHORITY = "com.progrema.supersave";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_SAVING_GOAL = "saving_goal";
    public static final String PATH_EXPENSE = "expense";
    public static final String PATH_EXPENSE_CATEGORY = "category";
    public static final String PATH_BUDGET = "budget";

    interface UserColumns {
        String USER_NAME = "name";
        String DEFAULT_CURRENCY = "default_currency";
        String DEFAULT_MONTHLY_BUDGET = "default_monthly_budget"; //in default currency
        String CYCLE_DATE = "cycle_date";
        String ACTIVE_BUDGET_ID = "active_budget_id"; //foreign key
        String TOTAL_SAVING = "total_saving"; //in default currency
    }

    interface BudgetColumns {
        String INITIAL_AMOUNT = "initial_amount";
        String USED_AMOUNT = "used_amount";
        String START_DATE = "start_date";
        String END_DATE = "end_date";
    }

    interface ExpenseColumns {
        String TIMESTAMP = "timestamp";
        String BUDGET_ID = "budget_id"; //foreign key
        String CURRENCY = "currency";
        String EXCHANGE_RATE = "exchange_rate";
        String AMOUNT = "amount";
        String CATEGORY_ID = "category"; //foreign key
        String OCCURRENCE = "occurrence";
        String NOTES = "notes";
    }

    interface CategoryColumns {
        String CATEGORY_NAME = "name";
    }

    interface SavinggoalColumns {
        String TIMESTAMP = "timestamp";
        String NAME = "name";
        String CURRENCY = "currency";
        String EXCHANGE_RATE = "exchange_rate";
        String PRICE = "price";
        String STATUS = "status";
        String START_DATE = "start_date";
        String TARGET_DATE = "target_date";
        String CATEGORY_ID = "category"; //foreign key
        String PICTURE = "picture";
        String NOTES = "notes";
    }

    public static class User implements UserColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public interface Query {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            User.USER_NAME,
                            User.DEFAULT_CURRENCY,
                            User.DEFAULT_MONTHLY_BUDGET,
                            User.CYCLE_DATE,
                            User.ACTIVE_BUDGET_ID,
                            User.TOTAL_SAVING
                    };

            final int OFFSET_ID = 0;
            final int OFFSET_USER_NAME = 1;
            final int OFFSET_DEFAULT_CURRENCY = 2;
            final int OFFSET_MONTHLY_BUDGET = 3;
            final int OFFSET_CYCLE_DATE = 4;
            final int OFFSET_ACTIVE_BUDGET_ID = 5;
            final int OFFSET_TOTAL_SAVING = 6;
        }
    }

    public static class Budgets implements BudgetColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUDGET).build();

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public interface Query {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            BudgetColumns.INITIAL_AMOUNT,
                            BudgetColumns.USED_AMOUNT,
                            BudgetColumns.START_DATE,
                            BudgetColumns.END_DATE,
                    };

            final int OFFSET_ID = 0;
            final int OFFSET_INITIAL_AMOUNT = 1;
            final int OFFSET_USED_AMOUNT = 2;
            final int OFFSET_START_DATE = 3;
            final int OFFSET_END_DATE = 4;
        }
    }

    /**
     * Expenses table contract class
     */
    public static class Expenses implements ExpenseColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE).build();

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }


        public interface Query {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Expenses.TIMESTAMP,
                            Expenses.BUDGET_ID,
                            Expenses.CURRENCY,
                            Expenses.EXCHANGE_RATE,
                            Expenses.AMOUNT,
                            Expenses.CATEGORY_ID,
                            Expenses.OCCURRENCE,
                            Expenses.NOTES

                    };

            final int OFFSET_ID = 0;
            final int OFFSET_TIMESTAMP = 1;
            final int OFFSET_BUDGET_ID = 2;
            final int OFFSET_CURRENCY = 3;
            final int OFFSET_EXCHANGE_RATE = 4;
            final int OFFSET_AMOUNT = 5;
            final int OFFSET_CATEGORY = 6;
            final int OFFSET_OCCURRENCE = 7;
            final int OFFSET_NOTES = 8;
        }

    }

    /**
     * Expenses table contract class
     */
    public static class Savinggoals implements SavinggoalColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SAVING_GOAL).build();

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public interface Query {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Savinggoals.TIMESTAMP,
                            Savinggoals.NAME,
                            Savinggoals.CURRENCY,
                            Savinggoals.EXCHANGE_RATE,
                            Savinggoals.PRICE,
                            Savinggoals.STATUS,
                            Savinggoals.START_DATE,
                            Savinggoals.TARGET_DATE,
                            Savinggoals.CATEGORY_ID,
                            Savinggoals.PICTURE,
                            Savinggoals.NOTES
                    };

            final int OFFSET_ID = 0;
            final int OFFSET_TIMESTAMP = 1;
            final int OFFSET_NAME = 2;
            final int OFFSET_CURRENCY = 3;
            final int OFFSET_EXCHANGE_RATE = 4;
            final int OFFSET_PRICE = 5;
            final int OFFSET_STATUS = 6;
            final int OFFSET_START_DATE = 7;
            final int OFFSET_TARGET_DATE = 8;
            final int OFFSET_CATEGORY = 9;
            final int OFFSET_PICTURE = 10;
            final int OFFSET_NOTES = 11;
        }

    }

    /**
     * Expenses table contract class
     */
    public static class Categories implements CategoryColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE_CATEGORY).build();

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public interface Query {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Categories.CATEGORY_NAME
                    };

            final int OFFSET_ID = 0;
            final int OFFSET_CATEGORY_NAME = 1;
        }

    }

}
