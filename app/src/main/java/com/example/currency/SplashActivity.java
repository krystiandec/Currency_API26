package com.example.currency;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SplashActivity extends Activity {
    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
    private ArrayList<String> mCurrencies;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        new FerchCodesTask().execute(URL_CODES);

    }

    private class FerchCodesTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                if (jsonObject == null) {
                    throw new JSONException("Brak danych");
                }
                Iterator iterator = jsonObject.keys();
                String key = "";
                mCurrencies = new ArrayList<String>();
                while (iterator.hasNext()) {
                    key = (String) iterator.next();
                    mCurrencies.add(key + " | " + jsonObject.getString(key));
                }
                finish();
            } catch (JSONException e) {
                Toast.makeText(SplashActivity.this,
                        "Wyjątek w danych JSON: " + e.getMessage(),
                        Toast.LENGTH_LONG)
                .show();
                e.printStackTrace();
                finish();
            }


        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            return null;
        }
    }
}