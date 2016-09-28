package com.backtory.android.sdksample;

import android.app.Application;

import com.backtory.androidsdk.Storage;
import com.backtory.androidsdk.internal.Backtory;
import com.backtory.androidsdk.internal.Config;

public class MainApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Backtory.init(this, Config.newBuilder().storage(new Storage.SharedPreferencesStorage(this)).
        initAuth(BuildConfig.backtory_auth_instance_id, BuildConfig.backtory_auth_key).
        initCloudCode(BuildConfig.backtory_lambda_instance_id).
        initGame(BuildConfig.backtory_game_instance_id).
        initStorage(BuildConfig.backtory_storage_instance_id).
        build());
  }
}
