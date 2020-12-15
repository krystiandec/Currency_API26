package com.example.currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button mCalcButton;
    private TextView mConvertedTextView;
    private EditText mAmountEditText;
    private Spinner mForSpinner, mHomSpinner;
    private String[] mCurrencies;

    public static final String FOR = "FOR_CURRENCY";
    public static final String HOM = "HOM_CURRENCY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
        mAmountEditText = (EditText) findViewById(R.id.edt_amount);
        mCalcButton = (Button) findViewById(R.id.btn_calc);
        mForSpinner = (Spinner) findViewById(R.id.spn_for);
        mHomSpinner = (Spinner) findViewById(R.id.spn_hom);

        ArrayList<String> arrayList = ((ArrayList<String>)
                getIntent().getSerializableExtra(SplashActivity.KEY_ARRAYLIST));
        Collections.sort(arrayList);
        mCurrencies = arrayList.toArray(new String[arrayList.size()]);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_closed,
                mCurrencies
        );
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mForSpinner.setAdapter(arrayAdapter);
        mHomSpinner.setAdapter(arrayAdapter);

        mForSpinner.setOnItemSelectedListener(this);
        mHomSpinner.setOnItemSelectedListener(this);

        if (savedInstanceState == null
                && (PrefsMgr.getString(this, FOR) == null &&
                PrefsMgr.getString(this, HOM) == null)) {

            mForSpinner.setSelection(findPositionGivenCode("EUR", mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode("PLN", mCurrencies));

            PrefsMgr.setString(this, FOR, "EUR");
            PrefsMgr.setString(this, HOM, "PLN");

        } else {

            mForSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this,
                    FOR), mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this,
                    HOM), mCurrencies));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnu_codes: {
                launchBrowser(SplashActivity.URL_CODES);
            }
            break;
            case R.id.mnu_invert: {
                invertCurrencies();
            }
            break;
            case R.id.mnu_exit: {
                finish();
            }
            break;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_for: {
                PrefsMgr.setString(this, FOR,
                        extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
            }break;
            case R.id.spn_hom: {
                PrefsMgr.setString(this, HOM,
                        extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
            }break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void launchBrowser(String strUri) {
        if (isOnline()) {
            Uri uri = Uri.parse(strUri);
            // wywołanie intencji w sposób pośredni
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void invertCurrencies() {
        int nFor = mForSpinner.getSelectedItemPosition();
        int nHom = mHomSpinner.getSelectedItemPosition();

        mForSpinner.setSelection(nHom);
        mHomSpinner.setSelection(nFor);

        mConvertedTextView.setText("");

        PrefsMgr.setString(this, FOR,
                extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
        PrefsMgr.setString(this, HOM,
                extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));

    }

    private int findPositionGivenCode(String code, String[] currencies) {

        for (int i = 0; i < currencies.length; i++) {
            if (extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)) {
                return i;
            }
        }
        // wartość domyślna
        return 0;
    }

    private String extractCodeFromCurrency(String currency) {
        return (currency).substring(0, 3);
    }

}