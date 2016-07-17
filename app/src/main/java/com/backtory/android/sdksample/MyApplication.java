package com.backtory.android.sdksample;

import android.app.Application;
import android.preference.PreferenceManager;

import com.backtory.androidsdk.Storage;
import com.backtory.androidsdk.internal.Backtory;
import com.backtory.androidsdk.internal.Config;
import com.backtory.androidsdk.internal.Core;

/**
 * Created by Alireza Farahani on 6/12/2016.
 */
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Core.init(this);
    Backtory.init(Config.newBuilder().storage(storage).
        initAuth(BuildConfig.backtory_auth_instance_id, BuildConfig.backtory_auth_key).
        initCloudCode(BuildConfig.backtory_lambda_instance_id).
        initGame(BuildConfig.backtory_game_instance_id).build());
  }

  // ----------------------------------------------------

  Storage storage = new Storage() {
    @Override
    public void put(String key, String data) {
      PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(key, data).apply();
    }

    @Override
    public String get(String key) {
      return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(key, null);
    }

    @Override
    public boolean remove(String key) {
      PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove(key).apply();
      return true;
    }

    @Override
    public void clear() {
      PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().commit();
    }
  };
}
