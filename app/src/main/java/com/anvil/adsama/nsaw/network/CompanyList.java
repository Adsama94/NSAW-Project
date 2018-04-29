package com.anvil.adsama.nsaw.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CompanyList {

    private static final String LOG_TAG = CompanyList.class.getSimpleName();
    private InputStream mInputStream;

    public CompanyList(InputStream inputStream) {
        mInputStream = inputStream;
    }

    public List read() {
        List<String> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                resultList.add(csvLine);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                mInputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
}