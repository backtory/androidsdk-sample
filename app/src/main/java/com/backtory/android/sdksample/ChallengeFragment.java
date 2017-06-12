package com.backtory.android.sdksample;

import android.view.View;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.LoginResponse;
import com.backtory.java.realtime.android.BacktoryRealtimeAndroidApi;
import com.backtory.java.realtime.core.listeners.ChallengeListener;
import com.backtory.java.realtime.core.models.ConnectResponse;
import com.backtory.java.realtime.core.models.connectivity.challenge.ActiveChallengesListResponse;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeAcceptedMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeDeclinedMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeExpiredMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeImpossibleMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeInvitationMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeReadyMessage;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeReadyWithoutYou;
import com.backtory.java.realtime.core.models.connectivity.challenge.ChallengeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad on 2/16/17.
 *
 */
public class ChallengeFragment extends MainActivity.AbsFragment implements ChallengeListener {

    private void loginChallengeUser1() {
        BacktoryUser.loginInBackground(TestUser.getFirst().username, TestUser.getFirst().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setChallengeListener(this);
    }

    private void loginChallengeUser2() {
        BacktoryUser.loginInBackground(TestUser.getSecond().username, TestUser.getSecond().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setChallengeListener(this);
    }


    private void realtimeConnect() {
        BacktoryRealtimeAndroidApi.getInstance().connectAsync(this.<ConnectResponse>printCallBack());
    }

    private void realtimeDisconnect() {
        BacktoryRealtimeAndroidApi.getInstance().disconnectAsync(this.<Void>printCallBack());
    }

    private void requestChallenge() {
        List<String> challengedUsers = new ArrayList<>();
        challengedUsers.add(TestUser.getFirst().userId);
        BacktoryRealtimeAndroidApi.getInstance().requestChallengeAsync(
                challengedUsers, 25, 2, this.<ChallengeResponse>printCallBack());
    }

    private void cancelChallenge() {
        // TODO: 2/17/17 Implement this.
    }

    private String invitedChallengeId;

    private void acceptChallenge() {
        if (invitedChallengeId == null) {
            textView.setText("No invitedChallengeId available!");
            return;
        }
        BacktoryRealtimeAndroidApi.getInstance().acceptChallengeAsync(
                invitedChallengeId, this.<Void>printCallBack());
    }

    private void declineChallenge() {
        if (invitedChallengeId == null) {
            textView.setText("No invitedChallengeId available!");
            return;
        }
        BacktoryRealtimeAndroidApi.getInstance().declineChallengeAsync(
                invitedChallengeId, this.<Void>printCallBack());
    }

    private void requestActiveChallenges() {
        BacktoryRealtimeAndroidApi.getInstance().requestListOfActiveChallengesAsync(
                this.<ActiveChallengesListResponse>printCallBack());
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.login_challenge_user_1, R.id.login_challenge_user_2, R.id.realtime_connect, R.id.realtime_disconnect,
                            R.id.request_challenge, R.id.cancel_challenge,
                            R.id.accept_challenge, R.id.decline_challenge, R.id.request_active_challenges};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_challenge;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_challenge_user_1:
                loginChallengeUser1();
                break;
            case R.id.login_challenge_user_2:
                loginChallengeUser2();
                break;
            case R.id.realtime_connect:
                realtimeConnect();
                break;
            case R.id.realtime_disconnect:
                realtimeDisconnect();
                break;
            case R.id.request_challenge:
                requestChallenge();
                break;
            case R.id.cancel_challenge:
                cancelChallenge();
                break;
            case R.id.accept_challenge:
                acceptChallenge();
                break;
            case R.id.decline_challenge:
                declineChallenge();
                break;
            case R.id.request_active_challenges:
                requestActiveChallenges();
                break;
        }
    }

    @Override
    public void onChallengeInvitation(ChallengeInvitationMessage challengeInvitationMessage) {
        textView.setText("Invited to a challenge!\n" + MainActivity.gson.toJson(challengeInvitationMessage));
        invitedChallengeId = challengeInvitationMessage.getChallengeId();
    }

    @Override
    public void onChallengeAccepted(ChallengeAcceptedMessage challengeAcceptedMessage) {
        textView.setText("Challenge accepted!\n" + MainActivity.gson.toJson(challengeAcceptedMessage));
    }

    @Override
    public void onChallengeDeclined(ChallengeDeclinedMessage challengeDeclinedMessage) {
        textView.setText("Challenge declined!\n" + MainActivity.gson.toJson(challengeDeclinedMessage));
    }

    @Override
    public void onChallengeExpired(ChallengeExpiredMessage challengeExpiredMessage) {
        textView.setText("Challenge expired!\n" + MainActivity.gson.toJson(challengeExpiredMessage));
    }

    @Override
    public void onChallengeImpossible(ChallengeImpossibleMessage message) {
        textView.setText("Challenge is impossible!\n" + MainActivity.gson.toJson(message));
    }

    @Override
    public void onChallengeReady(ChallengeReadyMessage challengeReadyMessage) {
        textView.setText("Challenge is ready!\n" + MainActivity.gson.toJson(challengeReadyMessage));
        String matchId = challengeReadyMessage.getMatchId();
        RealtimeFragment.getInstance().setMatchId(matchId);
    }

    @Override
    public void onChallengeWithoutYou(ChallengeReadyWithoutYou challengeReadyWithoutYou) {
        textView.setText("Challenge is ready without you!\n" + MainActivity.gson.toJson(challengeReadyWithoutYou));
    }
}
