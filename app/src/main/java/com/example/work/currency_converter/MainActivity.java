package com.example.work.currency_converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    CurrenciesAdapter currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView)findViewById(R.id.listCurrencies);

//        EditText moneyText= (EditText)findViewById(R.id.moneyValue);
//        TextWatcher watcherMoney();
//        watcherMoney.onTextChanged(moneyText.getText());

        currencies= new CurrenciesAdapter(this);
        currencies.loadFromCSV("taux_20141028.csv");

        // Bind to our new adapter.
        listview.setAdapter(currencies);

        Button refresh_button= (Button)findViewById(R.id.refresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                EditText moneyText= (EditText)findViewById(R.id.moneyValue);
                try {
                    double moneyValue= Double.parseDouble(moneyText.getText().toString());
                    currencies.refresh(moneyValue);
                } catch (Exception e) {
                    System.err.println("Impossible de caster en Double : " + moneyText.getText().toString());
                    return false;
                }
            return true;
//            case R.id.help:
//                showHelp();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
