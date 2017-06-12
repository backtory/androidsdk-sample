package com.backtory.android.sdksample;

import android.app.Application;

import com.backtory.java.internal.BacktoryClient;
import com.backtory.java.internal.Config;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BacktoryClient._setDebugMode(true);

        BacktoryClient.Android.init(Config.newBuilder()
                .initAuth(BuildConfig.backtory_auth_instance_id, BuildConfig.backtory_auth_key)
                .initObjectStorage(BuildConfig.backtory_object_storage_instance_id)
                .initCloudCode(BuildConfig.backtory_lambda_instance_id)
                .initGame(BuildConfig.backtory_game_instance_id)
                .initFileStorage(BuildConfig.backtory_storage_instance_id)
                .initConnectivity(BuildConfig.backtory_connectivity_instance_id)
                .build(), this);
    }
}
