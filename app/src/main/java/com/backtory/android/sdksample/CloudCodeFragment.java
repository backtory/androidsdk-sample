package com.backtory.android.sdksample;

import android.support.v4.app.Fragment;
import android.view.View;

import com.backtory.java.HttpStatusCode;
import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryCloudCode;
import com.backtory.java.internal.BacktoryResponse;


/**
 * A simple {@link Fragment} subclass.
 */
public class CloudCodeFragment extends MainActivity.AbsFragment {


    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.button_public_echo, R.id.button_private_search};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_cloud_code;
    }

    //-------------------------------------------------------------

    void publicEcho() {
        //BacktoryCloudCode.runInBackground("echo", "body body body!", false, String.class, this.<String>printCallBack());
        BacktoryCloudCode.runInBackground(
                "echo", "body body body!",
                String.class, new BacktoryCallBack<String>() {
                    @Override
                    public void onResponse(BacktoryResponse<String> response) {
                        if (response.isSuccessful())
                            textView.setText(response.body());
                    }
                });
    }

    void privateGetPerson() {
        BacktoryCloudCode.runInBackground("hello", new Info("453"), Person.class, new BacktoryCallBack<Person>() {
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
            case R.id.button_private_search:
                privateGetPerson();
                break;
        }
    }

    static class Info {
        String id;

        public Info(String id) {
            this.id = id;
        }
    }

    static class Person {
        public String name;
        public int age;
    }
}
