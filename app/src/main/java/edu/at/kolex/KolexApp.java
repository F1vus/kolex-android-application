package edu.at.kolex;

import android.app.Application;

import edu.at.kolex.api.ApiClient;

public class KolexApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(this);
    }
}
