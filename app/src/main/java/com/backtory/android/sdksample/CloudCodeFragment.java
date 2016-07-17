package com.backtory.android.sdksample;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.backtory.androidsdk.internal.BacktoryCallBack;
import com.backtory.androidsdk.internal.BacktoryResponse;
import com.backtory.androidsdk.internal.HttpStatusCode;
import com.backtory.androidsdk.lambda.BacktoryCloudCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudCodeFragment extends MainActivity.AbsFragment implements View.OnClickListener {


  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_lambda;
  }

  //-------------------------------------------------------------

  void publicEcho() {
    //BacktoryCloudCode.performCloudFunctionAsync("echo", "body body body!", false, String.class, this.<String>printCallBack());
    BacktoryCloudCode.performCloudFunctionAsync(
        "echo", "body body body!",
        String.class, new BacktoryCallBack<String>() {
          @Override
          public void onResponse(BacktoryResponse<String> response) {
            if (response.isSuccessful())
              Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
          }
        });
  }

  void privateGetPerson() {
    BacktoryCloudCode.performCloudFunctionAsync("hello", new Info("453"), Person.class, new BacktoryCallBack<Person>() {
      @Override
      public void onResponse(BacktoryResponse<Person> response) {
        if (response.isSuccessful()) {
          Person person = response.body();
          textView.setText("search result\nname: " + person.name);
        } else
          textView.setText("search failed\n" + response.code() + " " + HttpStatusCode.getErrorByCode(response.code()).name());
      }
    });

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.button_public_echo:
        publicEcho();
        break;
      case R.id.button_private_get_person:
        privateGetPerson();
        break;
    }
  }

  static class Info {
    public Info(String id) {
      this.id = id;
    }

    String id;
  }

  static class Person {
    public String name;
    public int age;
  }
}
