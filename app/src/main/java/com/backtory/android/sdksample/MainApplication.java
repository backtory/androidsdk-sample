package com.backtory.android.sdksample;

import android.app.Application;

import com.backtory.java.internal.BacktoryClient;
import com.backtory.java.internal.KeyConfiguration;
import com.backtory.java.internal.LogLevel;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BacktoryClient.setDebugMode(true);
        BacktoryClient.setLogLevel(LogLevel.Debug);
        BacktoryClient.init(KeyConfiguration.newBuilder().
                setAuthKeys(BuildConfig.backtory_auth_instance_id, BuildConfig.backtory_auth_key).
                setCloudcodeKey(BuildConfig.backtory_lambda_instance_id).
                setGameKey(BuildConfig.backtory_game_instance_id).
                setObjectStorageKey(BuildConfig.backtory_object_storage_instance_id).
                setFileStorageKey(BuildConfig.backtory_storage_instance_id).
                setConnectivityKey(BuildConfig.backtory_connectivity_instance_id).
                build(), this);
    }
}
