package com.progrema.supersave.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.progrema.supersave.R;
import com.progrema.supersave.data.db.SupersaveContract;
import com.progrema.supersave.data.model.Budget;
import com.progrema.supersave.data.model.Money;
import com.progrema.supersave.data.model.User;
import com.progrema.supersave.util.SupersaveAsynchQueryHandler;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import com.progrema.supersave.util.Utils;
import com.squareup.phrase.Phrase;

public class LoginActivity extends ActionBarActivity {


    /**
     * ERROR CODE
     */
    private final int DATA_OK = 0;
    private final int NAME_NOK = 1;
    private final int CYCLE_DATE_NOK = 2;
    private final int BUDGET_NOK = 3;
    private final int FIRST_TIME_BUDGET_NOK = 4;
    private final int CYCLE_DATE_NOT_EQUAL_TODAY = 100;

    private RelativeLayout firstTimeBudgetContainer;
    private Spinner spinnerCurrencySelection;
    /**********************************************************************
     *************************** Callback Section **************************
     **********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firstTimeBudgetContainer = (RelativeLayout) findViewById(R.id.first_time_budget_container);

        setUpCurrencySelector();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_go) {
            User newUser = null;
            int errorCode = DATA_OK;
            try {
                newUser = getUserData();
            }catch (LoginException ex){
                errorCode = ex.getErrorCode();
            }

            if (errorCode == DATA_OK) {

                /*create new budget*/
                Calendar today = Calendar.getInstance();
                Calendar firstCycleDate = Calendar.getInstance();
                firstCycleDate.set(Calendar.DATE,newUser.getCycleDate());
                firstCycleDate.add(Calendar.MONTH, 1);
                Budget newBudget = new Budget(today.getTime(), firstCycleDate.getTime(),
                        newUser.getRemainingBudget());

                /*append new budget to content to be passed to our content provider*/
                ContentValues values = newUser.getContentValue();
                values = newBudget.appendContentValue(values);

                //save to database asynchronously
                SupersaveAsynchQueryHandler asynchQueryHandler = new SupersaveAsynchQueryHandler(this);
                asynchQueryHandler.startInsert(0, null,
                        SupersaveContract.User.CONTENT_URI, values);

                //TODO set flag to not come back to this activity

                //launch main activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }else
            {
                errorHandler(errorCode);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************
     ****************** Spinner Setup and Callback ************************
     **********************************************************************/
    private void setUpCurrencySelector() {
        spinnerCurrencySelection = (Spinner) findViewById(R.id.id_login_currency);

        //create array adapter for currency spinner containing all possible currency
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item ,
                Utils.getAllCurrenciesCode());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //get local currency string
        String localCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();

        //attach the adapter
        spinnerCurrencySelection.setAdapter(adapter);

        //set default selection to local currency
        spinnerCurrencySelection.setSelection(adapter.getPosition(localCurrency));

    }


    /**********************************************************************
    *************************** Private Helper Section ********************
    **********************************************************************/
   private void errorHandler(int errorCode) {

        if(errorCode == CYCLE_DATE_NOT_EQUAL_TODAY){
            firstTimeBudgetContainer.setVisibility(View.VISIBLE);
            return;
        }

        //TODO implement error handler
    }

    private User getUserData() throws LoginException {

        String name = "USER" ;
        Money monthlyBudget;
        Money firstTimeBudget = null;
        Currency defaultCurrency;
        int cycleDate;

        /*get name*/
//        try {
//            name = ((EditText) findViewById(R.id.id_login_name_textbox)).getText().toString();
//        } catch (Exception ex){
//            throw new LoginException(NAME_NOK);
//        }

        /*get cycle date*/
        try {
            cycleDate = Integer.parseInt(((EditText) findViewById(R.id.id_login_cycle_date_textbox))
                    .getText().toString());
        } catch (Exception ex) {
            throw new LoginException(CYCLE_DATE_NOK);
        }

        int todayDate =  Calendar.getInstance().get(Calendar.DATE);
        if (cycleDate != todayDate){
            setRemainingDayInfo(Utils.getDayDiff(todayDate,cycleDate));
            if (firstTimeBudgetContainer.getVisibility() == View.GONE) {
                throw new LoginException(CYCLE_DATE_NOT_EQUAL_TODAY);
            }
        }

        /*get budget*/
        try {
            int amountIn = Integer.parseInt(((EditText) findViewById(R.id.id_login_budget_textbox))
                    .getText().toString());
            //TODO get currency from spinner

            String currency = (String) spinnerCurrencySelection.getAdapter().
                    getItem((int)spinnerCurrencySelection.getSelectedItemId());

            defaultCurrency = Currency.getInstance(currency);
            monthlyBudget = new Money(defaultCurrency, amountIn);

        } catch (Exception ex) {
            throw new LoginException(BUDGET_NOK);
        }

        //create new user
        User newUser = new User(name, defaultCurrency, monthlyBudget, cycleDate);

        try {
            if (firstTimeBudgetContainer.getVisibility() == View.VISIBLE) {
                int firstTimeBudgetAmount = Integer
                        .parseInt(((EditText) findViewById(R.id.id_login_first_time_budget))
                                .getText().toString());
                firstTimeBudget = new Money(defaultCurrency, firstTimeBudgetAmount);
                newUser.setRemainingBudget(firstTimeBudget);
            }
        } catch (Exception ex) {
            throw new LoginException(FIRST_TIME_BUDGET_NOK);
        }

        return newUser;
    }

    private void setRemainingDayInfo(int diff){
        CharSequence info = Phrase.from(this.getString(R.string.login_cycle_date_help))
                .put("rem_day", String.valueOf(diff))
                .format();
        TextView helpText = (TextView) findViewById(R.id.id_login_help_text);
        helpText.setText(info);
    }

    private class LoginException extends Exception {
        private int errorCode;

        public LoginException(int errorCode){
            this.errorCode = errorCode;
        }

        public int getErrorCode() {
            return errorCode;
        }
    }
}
