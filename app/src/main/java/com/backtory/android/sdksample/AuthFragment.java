package com.backtory.android.sdksample;


import android.view.View;

import com.backtory.androidsdk.HttpStatusCode;
import com.backtory.androidsdk.internal.BacktoryAuth;
import com.backtory.androidsdk.internal.BacktoryCallBack;
import com.backtory.androidsdk.model.BacktoryResponse;
import com.backtory.androidsdk.model.BacktoryUser;
import com.backtory.androidsdk.model.GuestRegistrationParam;
import com.backtory.androidsdk.model.LoginResponse;

import static com.backtory.android.sdksample.MainActivity.generateEmail;
import static com.backtory.android.sdksample.MainActivity.generatePassword;
import static com.backtory.android.sdksample.MainActivity.generateUsername;
import static com.backtory.android.sdksample.MainActivity.gson;
import static com.backtory.android.sdksample.MainActivity.lastGenPassword;
import static com.backtory.android.sdksample.MainActivity.lastGenUsername;

public class AuthFragment extends MainActivity.AbsFragment implements View.OnClickListener {

  void register() {
    new BacktoryUser.Builder().
        setFirstName("Alireza").
        setLastName("Farahani").
        setUsername(generateUsername(true)).
        setEmail(generateEmail(true)).
        setPassword(generatePassword(true)).
        setPhoneNumber("09121234567").
        build().registerInBackground(this.<BacktoryUser>printCallBack());

    /*BacktoryUser newUser = new BacktoryUser.Builder().
        setFirstName("Alireza").
        setLastName("Farahani").
        setUsername(generateUsername(true)).
        setEmail(generateEmail(true)).
        setPassword(generatePassword(true)).
        setPhoneNumber("09121234567").build();
    newUser.registerInBackground(new BacktoryCallBack<BacktoryUser>() {
      @Override
      public void onResponse(BacktoryResponse<BacktoryUser> response) {
        if (response.isSuccessful()) {
          Toast.makeText(getContext(), response.body().getUsername() + ", thanks for registration", Toast.LENGTH_SHORT).show();
        } else if (response.code() == HttpStatusCode.Conflict.code()) {
          Toast.makeText(getContext(), "a user with this username already exist", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "registration failed", Toast.LENGTH_SHORT).show();
        }
      }
    });*/
  }

  void login() {
    try {
      BacktoryUser.loginInBackground(lastGenUsername, lastGenPassword, this.<LoginResponse>printCallBack());
    } catch (IllegalStateException ise) {
      toast(ise.getMessage());
    }

    /*final String userName = userNameInput.getText().toString();
    BacktoryUser.loginInBackground(userName, "", new BacktoryCallBack<LoginResponse>() {
      @Override
      public void onResponse(BacktoryResponse<LoginResponse> response) {
        if (response.isSuccessful()) {
          Toast.makeText(getContext(), "Wellcome " + userName, Toast.LENGTH_SHORT).show();
        } else if (response.code() == 401) { // HttpStatusCode.Unauthorized.code()
          Toast.makeText(getContext(), "either username or password is wrong", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "login failed", Toast.LENGTH_SHORT).show();
        }
      }
    });*/
  }

  void newAccessToken() {
    try {
      BacktoryAuth.getNewAccessTokenASync(this.<LoginResponse>printCallBack());
    } catch (IllegalStateException ise) {
      toast(ise.getMessage());
    }
  }

  void guestRegister() {
    new BacktoryUser().registerAsGuestInBackground(new BacktoryCallBack<BacktoryUser>() {
      @Override
      public void onResponse(BacktoryResponse<BacktoryUser> response) {
        if (response.isSuccessful()) {
          lastGenUsername = response.body().getUsername();
          lastGenPassword = response.body().getGuestPassword();
          textView.setText(gson.toJson(response.body()));
        } else {
          HttpStatusCode statusCode = HttpStatusCode.getErrorByCode(response.code());
          textView.setText(statusCode.code() + statusCode.name());
        }
      }
    });
    /*new BacktoryUser().registerAsGuestInBackground(new BacktoryCallBack<BacktoryUser>() {
      @Override
      public void onResponse(BacktoryResponse<BacktoryUser> response) {
        if (response.isSuccessful()) {
          // show user and pass to user for later login or store them and handle login yourself
          textView.setText("your guest username: " + response.body().getUsername() +
                          "\nyour guest password: " + response.body().getGuestPassword();
        }
      }
    });*/
  }

//EditText emailInput = new EditText(getContext());

  void completeGuestReg() {
    BacktoryUser.getCurrentUser().completeRegistrationInBackground(new GuestRegistrationParam.Builder().
            setFirstName("not guest!").setLastName("not guest last name").
            setEmail(generateEmail(true)).setNewPassword("guest pass").
            setNewUsername(generateUsername(true)).build(),
        this.<BacktoryUser>printCallBack());


    /*GuestRegistrationParam params = new GuestRegistrationParam.Builder().
        setFirstName("not guest!").
        setLastName("not guest last name").
        setEmail(emailInput.getText().toString()).
        setNewPassword(passInput.getText().toString()).
        setNewUsername(userNameInput.getText().toString()).build();
    BacktoryUser.getCurrentUser().completeRegistrationInBackground(params,
        new BacktoryCallBack<BacktoryUser>() {
          @Override
          public void onResponse(BacktoryResponse<BacktoryUser> response) {
            if (response.isSuccessful()) {
              Toast.makeText(getContext(),
                  response.body().getFirstName() + ", thanks for registration",
                  Toast.LENGTH_SHORT).show();
            } else if (response.code() == 409) { // HttpStatusCode.Conflict.code()
              Toast.makeText(getContext(), "a user with this username already exist", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(getContext(), "registration failed", Toast.LENGTH_SHORT).show();
            }
          }
        });*/
  }

  void changePass() {
    BacktoryUser.getCurrentUser().changePasswordInBackground(lastGenPassword, "4321", new BacktoryCallBack<Void>() {
      @Override
      public void onResponse(BacktoryResponse<Void> response) {
        textView.setText(response.isSuccessful() ? "suc" : "fail " + response.message());
      }
    });
    /*BacktoryUser.getCurrentUser().changePasswordInBackground(oldPasswordInput.getText().toString(),
        newPasswordInput.getText().toString(), new BacktoryCallBack<Void>() {
          @Override
          public void onResponse(BacktoryResponse<Void> response) {
            if (response.isSuccessful()) {
              Toast.makeText(getContext(), "Your password successfully changed", Toast.LENGTH_SHORT).show();
            } else if (response.code() == 403) { // HttpStatusCode.Forbidden.code()
              Toast.makeText(getContext(), "Old password was incorrect", Toast.LENGTH_SHORT).show();
            } else
              Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();
          }
        });*/
  }

  void forgetPass() {
    BacktoryUser.forgotPasswordInBackground(lastGenUsername, new BacktoryCallBack<Void>() {
      @Override
      public void onResponse(BacktoryResponse<Void> response) {
        textView.setText(response.isSuccessful() ? "suc" : "fail " + response.message());
      }
    });
    /*BacktoryUser.forgotPasswordInBackground(userNameInput.getText().toString(), new BacktoryCallBack<Void>() {
      @Override
      public void onResponse(BacktoryResponse<Void> response) {
        if (response.isSuccessful()) {
          Toast.makeText(getContext(), "Instruction for password recovery will be send to your email", Toast.LENGTH_SHORT).show();
        } else if (response.code() == 404) { // HttpStatusCode.NotFound.code()
          Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        } else
          Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();
      }
    });*/
  }

  void updateUser() {
    BacktoryUser currentUser = BacktoryUser.getCurrentUser();
    currentUser.setFirstName("edit");
    currentUser.setLastName("editian");
    currentUser.setUsername(generateUsername(true));
    currentUser.setEmail(generateEmail(true));
    currentUser.setPhoneNumber("22222222");
    currentUser.updateInBackground(this.<BacktoryUser>printCallBack());

    /*BacktoryUser currentUser = BacktoryUser.getCurrentUser();
    currentUser.setFirstName("edit");
    currentUser.setLastName("editian");
    currentUser.setUsername("<new username>");
    currentUser.setPhoneNumber("22222222");
    currentUser.updateInBackground(new BacktoryCallBack<BacktoryUser>() {
      @Override
      public void onResponse(BacktoryResponse<BacktoryUser> response) {
        if (response.isSuccessful()) {
          Toast.makeText(getContext(), "your user updated successfully", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();
        }
      }
    });*/
  }

  void logout() {
    BacktoryUser.logoutInBackground();
    textView.setText("successful logout");
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_auth;
  }

  void currentUser() {
    BacktoryUser currentUser = BacktoryUser.getCurrentUser();
    textView.setText("firsName: " + currentUser.getFirstName() + "\n" +
        "username: " + currentUser.getUsername());
    //BacktoryUser.getCurrentUser().isGuest();
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
      case R.id.button_new_token:
        newAccessToken();
        break;
      case R.id.button_guest_register:
        guestRegister();
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
