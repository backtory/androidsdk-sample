package com.backtory.android.sdksample;

import android.view.View;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.LoginResponse;
import com.backtory.java.realtime.android.BacktoryRealtimeAndroidApi;
import com.backtory.java.realtime.core.listeners.MatchmakingListener;
import com.backtory.java.realtime.core.models.ConnectResponse;
import com.backtory.java.realtime.core.models.connectivity.matchmaking.MatchFoundMessage;
import com.backtory.java.realtime.core.models.connectivity.matchmaking.MatchNotFoundMessage;
import com.backtory.java.realtime.core.models.connectivity.matchmaking.MatchUpdateMessage;
import com.backtory.java.realtime.core.models.connectivity.matchmaking.MatchmakingResponse;

/**
 * Created by mohammad on 2/15/17.
 *
 */
public class MatchmakingFragment extends MainActivity.AbsFragment implements MatchmakingListener {

    public final String matchmakingName = "matchmaking1";
    public final int skill = 100;
    public final String metaData = "Guess who is back!";
    public String mmRequestId;

    private void loginMMUser1() {
        BacktoryUser.loginInBackground(TestUser.getFirst().username, TestUser.getFirst().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setMatchmakingListener(this);
    }

    private void loginMMUser2() {
        BacktoryUser.loginInBackground(TestUser.getSecond().username, TestUser.getSecond().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setMatchmakingListener(this);
    }

    private void realtimeConnect() {
        BacktoryRealtimeAndroidApi.getInstance().connectAsync(this.<ConnectResponse>printCallBack());
    }

    private void realtimeDisconnect() {
        BacktoryRealtimeAndroidApi.getInstance().disconnectAsync(this.<Void>printCallBack());
    }

    private void requestMatch() {
        BacktoryRealtimeAndroidApi.getInstance().requestMatchmakingAsync(
                matchmakingName, skill, metaData, new BacktoryCallBack<MatchmakingResponse>() {
                    @Override
                    public void onResponse(BacktoryResponse<MatchmakingResponse> response) {
                        MatchmakingFragment.this.<MatchmakingResponse>printCallBack().onResponse(response);
                        if (response.isSuccessful()) {
                            mmRequestId = response.body().getRequestId();
                        }
                    }
                });
    }

    private void cancelRequest() {
        if (mmRequestId == null) {
            textView.setText("No request for match available!");
        }
        BacktoryRealtimeAndroidApi.getInstance().cancelMatchmakingAsync(
                matchmakingName, mmRequestId, this.<Void>printCallBack());
    }



    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.login_mm_user_1, R.id.login_mm_user_2, R.id.realtime_connect, R.id.realtime_disconnect,
                            R.id.request_match, R.id.cancel_request};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_matchmaking;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_mm_user_1:
                loginMMUser1();
                break;
            case R.id.login_mm_user_2:
                loginMMUser2();
                break;
            case R.id.realtime_connect:
                realtimeConnect();
                break;
            case R.id.realtime_disconnect:
                realtimeDisconnect();
                break;
            case R.id.request_match:
                requestMatch();
                break;
            case R.id.cancel_request:
                cancelRequest();
                break;
        }
    }

    @Override
    public void onMatchFound(MatchFoundMessage matchFoundMessage) {
        textView.setText("Match found!\n" + MainActivity.gson.toJson(matchFoundMessage));
        String matchId = matchFoundMessage.getMatchId();
        RealtimeFragment.getInstance().setMatchId(matchId);
    }

    @Override
    public void onMatchUpdate(MatchUpdateMessage matchUpdateMessage) {
        textView.setText("Match updated!\n" + MainActivity.gson.toJson(matchUpdateMessage));
    }

    @Override
    public void onMatchNotFound(MatchNotFoundMessage matchNotFoundMessage) {
        textView.setText("Match not found!\n" + MainActivity.gson.toJson(matchNotFoundMessage));
    }
}
