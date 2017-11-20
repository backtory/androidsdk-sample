package com.backtory.android.sdksample;


import android.view.View;

import com.backtory.java.HttpStatusCode;
import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.model.GuestRegistrationParam;

import static com.backtory.android.sdksample.MainActivity.generateEmail;
import static com.backtory.android.sdksample.MainActivity.generatePassword;
import static com.backtory.android.sdksample.MainActivity.generateUsername;
import static com.backtory.android.sdksample.MainActivity.gson;
import static com.backtory.android.sdksample.MainActivity.lastGenPassword;
import static com.backtory.android.sdksample.MainActivity.lastGenUsername;

public class AuthFragment extends MainActivity.AbsFragment {

    private void register() {
        new BacktoryUser.Builder().
                setFirstName("Alireza").
                setLastName("Farahani").
                setUsername(generateUsername(true)).
                setEmail(generateEmail(true)).
                setPassword(generatePassword(true)).
                setPhoneNumber("09121234567").
                build().registerInBackground(this.<BacktoryUser>printCallBack());
    }

    private void login() {
        try {
            BacktoryUser.loginInBackground(lastGenUsername, lastGenPassword, this.<Void>printCallBack());
        } catch (IllegalStateException ise) {
            toast(ise.getMessage());
        }
    }

    private void guestLogin() {
        try {
            BacktoryUser.loginAsGuestInBackground(new BacktoryCallBack<Void>() {
                @Override
                public void onResponse(BacktoryResponse<Void> response) {
                    if (response.isSuccessful()) {
                        lastGenUsername = BacktoryUser.getCurrentUser().getUsername();
                        lastGenPassword = BacktoryUser.getCurrentUser().getGuestPassword();
                        textView.setText("successfully logged in! \n" + gson.toJson(BacktoryUser.getCurrentUser()));
                    } else {
                        HttpStatusCode statusCode = HttpStatusCode.getErrorByCode(response.code());
                        textView.setText(
                                String.format("%d %s", statusCode.code(), statusCode.name()));
                    }
                }
            });
        } catch (IllegalStateException ise) {
            toast(ise.getMessage());
        }
    }

    private void completeGuestReg() {
        if (BacktoryUser.getCurrentUser().isGuest()) {
            textView.setText("Guest user's password can not be changed!");
            return;
        }
        lastGenUsername = generateUsername(true);
        lastGenPassword = "guest pass";
        BacktoryUser.getCurrentUser().completeRegistrationInBackground(new GuestRegistrationParam.Builder().
                        setFirstName("not guest!").setLastName("not guest last name").
                        setEmail(generateEmail(true)).setNewPassword(lastGenPassword).
                        setNewUsername(lastGenUsername).build(),
                this.<BacktoryUser>printCallBack());
    }

    private void changePass() {
        if (BacktoryUser.getCurrentUser().isGuest()) {
            textView.setText("Guest user's password can not be changed!");
            return;
        }

        BacktoryUser.getCurrentUser().changePasswordInBackground(lastGenPassword, "4321", new BacktoryCallBack<Void>() {
            @Override
            public void onResponse(BacktoryResponse<Void> response) {
                textView.setText(response.isSuccessful() ? "suc" : "fail " + response.message());
            }
        });
    }

    private void forgetPass() {
        BacktoryUser.forgotPasswordInBackground(lastGenUsername, this.<Void>printCallBack());
    }

    private void updateUser() {
        BacktoryUser currentUser = BacktoryUser.getCurrentUser();
        currentUser.setFirstName("edit");
        currentUser.setLastName("editian");
        currentUser.setUsername(generateUsername(true));
        currentUser.setEmail(generateEmail(true));
        currentUser.setPhoneNumber("22222222");
        currentUser.updateInBackground(this.<BacktoryUser>printCallBack());
    }

    private void logout() {
        BacktoryUser.logoutInBackground();
        textView.setText("successful logout");
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.button_register_user, R.id.button_login_user, R.id.button_guest_login,
                R.id.button_complete_guest, R.id.button_change_pass, R.id.button_forget_pass,
                R.id.button_current_user, R.id.button_update_user, R.id.button_logout};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_auth;
    }

    void currentUser() {
        BacktoryUser currentUser = BacktoryUser.getCurrentUser();
        if (currentUser == null)
            textView.setText("No current user!");
        else
            textView.setText(String.format("firsName: %s\nusername: %s\nguest: %b\nactive: %b",
                    currentUser.getFirstName(), currentUser.getUsername(), currentUser.isGuest(), currentUser.isActive()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register_user:
                register();
                break;
            case R.id.button_login_user:
                login();
                break;
            case R.id.button_guest_login:
                guestLogin();
                break;
            case R.id.button_complete_guest:
                completeGuestReg();
                break;
            case R.id.button_change_pass:
                changePass();
                break;
            case R.id.button_forget_pass:
                forgetPass();
                break;
            case R.id.button_update_user:
                updateUser();
                break;
            case R.id.button_logout:
                logout();
                break;
            case R.id.button_current_user:
                currentUser();
                break;
        }
    }
}
