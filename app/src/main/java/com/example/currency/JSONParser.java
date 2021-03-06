package com.example.currency;

import android.util.Log;

import org.json.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {

    private

    static InputStream sInputStream = null;
    static JSONObject sReturnJsonObject = null;
    static String sRawJsonString = "";

    public JSONParser() {
    }

    public JSONObject getJSONFromUrl(String url) {
        // próba pobrania odpowiedzi z serwera
        try {
            System.out.println("@@@@@@@@@@@@@@@###################wschodzę do metody : getJSONFromUrl");
            URL internalURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) internalURL.openConnection();
            urlConnection.setRequestMethod("GET");
            sInputStream = new BufferedInputStream(urlConnection.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            sInputStream = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // przekazanie strumienia do obiektu StringBuilder
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sInputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            sInputStream.close();
            sRawJsonString = stringBuilder.toString();
        } catch (Exception e) {
            Log.e("Błąd odczytu z Buffer: " + e.toString(),
                    this.getClass().getSimpleName());
        }
        try {
            sReturnJsonObject = new JSONObject(sRawJsonString);
        } catch (JSONException e) {
            Log.e("Parser", "Błąd w trakcie parsowania danych " + e.toString());
        }
        // zwrócenie obiektu JSON
        return sReturnJsonObject;
    }
}
