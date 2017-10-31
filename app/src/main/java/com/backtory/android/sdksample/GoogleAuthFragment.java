package com.backtory.android.sdksample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backtory.java.HttpStatusCode;
import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryGoogleLogin;
import com.backtory.java.internal.BacktoryGoogleLoginFragment;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.GoogleAuthController;
import com.backtory.java.internal.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoogleAuthFragment extends BacktoryGoogleLoginFragment implements View.OnClickListener{
    static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    TextView textView;

    void loginWithGoogle(){ // called on some button click
        getServerAuthCode(new GoogleAuthController.OnServerAuthCodeReceived(){
            @Override
            public void onReceivingAuthCode(String authCode){
                if (TextUtils.isEmpty(authCode))
                    Toast.makeText(GoogleAuthFragment.this.getContext(), "cannot get auth code, wrong server-client-id maybe??", Toast.LENGTH_SHORT).show();
                else
                    BacktoryGoogleLogin.loginWithGoogle(authCode, new BacktoryCallBack<LoginResponse>() {
                        @Override
                        public void onResponse(BacktoryResponse<LoginResponse> response) {
                            if(response.isSuccessful())
                                textView.setText(gson.toJson(response.body()));
                            else {
                                HttpStatusCode statusCode = HttpStatusCode.getErrorByCode(response.code());
                                textView.setText(statusCode.code() + statusCode.name());
                            }
                        }
                    });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_google_auth, container, false);
        textView = (TextView) v.findViewById(R.id.textview);
        v.findViewById(R.id.button_google_login).setOnClickListener(this);
        return v;
    }

    protected <T> BacktoryCallBack<T> printCallBack() {
        return new BacktoryCallBack<T>() {
            @Override
            public void onResponse(BacktoryResponse<T> response) {
                if (response.isSuccessful())
                    textView.setText(response.body() != null ? gson.toJson(response.body()) : "successful");
                else
                    textView.setText(response.message());
            }
        };
    }

    void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_google_login){
            loginWithGoogle();
        }
    }
}
