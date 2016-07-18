package com.backtory.android.sdksample;

import android.app.Application;

import com.backtory.androidsdk.Storage;
import com.backtory.androidsdk.internal.Backtory;
import com.backtory.androidsdk.internal.Config;
import com.backtory.androidsdk.internal.Core;

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

  Storage storage = new Storage() {
    public static final String PREFERENCE_TAG = "backtory";
    @Override
    public void put(String key, String data) {
      getSharedPreferences(PREFERENCE_TAG, MODE_PRIVATE).edit().putString(key, data).apply();;
    }

    @Override
    public String get(String key) {
      return getSharedPreferences(PREFERENCE_TAG, MODE_PRIVATE).getString(key, null);
    }

    @Override
    public boolean remove(String key) {
      getSharedPreferences(PREFERENCE_TAG, MODE_PRIVATE).edit().remove(key).apply();
      return true;
    }

    @Override
    public void clear() {
      getSharedPreferences(PREFERENCE_TAG, MODE_PRIVATE).edit().clear().commit();
    }
  };
}
