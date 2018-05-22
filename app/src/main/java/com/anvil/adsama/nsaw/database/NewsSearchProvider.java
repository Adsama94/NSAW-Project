package com.anvil.adsama.nsaw.database;

import android.content.SearchRecentSuggestionsProvider;

public class NewsSearchProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.anvil.adsama.nsaw.database.NewsSearchProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public NewsSearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}