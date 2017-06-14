package com.backtory.android.sdksample;

import android.view.View;

import com.backtory.java.realtime.android.BacktoryRealtimeAndroidApi;
import com.backtory.java.realtime.android.BacktoryRealtimeMatchAndroidApi;
import com.backtory.java.realtime.core.listeners.MatchListener;
import com.backtory.java.realtime.core.listeners.RealtimeSdkListener;
import com.backtory.java.realtime.core.models.ConnectResponse;
import com.backtory.java.realtime.core.models.exception.ExceptionMessage;
import com.backtory.java.realtime.core.models.realtime.chat.DirectChatMessage;
import com.backtory.java.realtime.core.models.realtime.chat.MasterMessage;
import com.backtory.java.realtime.core.models.realtime.chat.MatchChatMessage;
import com.backtory.java.realtime.core.models.realtime.match.MatchDisconnectMessage;
import com.backtory.java.realtime.core.models.realtime.match.MatchEndedMessage;
import com.backtory.java.realtime.core.models.realtime.match.MatchEvent;
import com.backtory.java.realtime.core.models.realtime.match.MatchJoinedMessage;
import com.backtory.java.realtime.core.models.realtime.match.MatchStartedMessage;
import com.backtory.java.realtime.core.models.realtime.webhook.JoinedWebhookMessage;
import com.backtory.java.realtime.core.models.realtime.webhook.StartedWebhookMessage;
import com.backtory.java.realtime.core.models.realtime.webhook.WebhookErrorMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mohammad on 2/17/17.
 *
 */
public class RealtimeFragment extends MainActivity.AbsFragment implements RealtimeSdkListener, MatchListener {

    private static RealtimeFragment instance;

    public static RealtimeFragment getInstance() {
        if (instance == null)
            instance = new RealtimeFragment();
        return instance;
    }


    private String matchId;
    private BacktoryRealtimeMatchAndroidApi matchApi;

    public void setMatchId(String matchId) {
        this.matchId = matchId;
        this.matchApi = BacktoryRealtimeAndroidApi.getMatchApi(matchId);
        matchApi.setRealtimeSdkListener(this);
        matchApi.setMatchListener(this);
    }

    private void connectToMatch() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        matchApi.connectAndJoinAsync(this.<ConnectResponse>printCallBack());
    }

    private void sendEvent() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("myKey", "myValue");
        matchApi.sendEvent("My message", data);
    }

    private void directMessage() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        matchApi.directToUserAsync(TestUser.getSecond().userId, "Hello, what's up?!",
                this.<Void>printCallBack());
    }

    private void sendChatToMatch() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        matchApi.sendChatToMatchAsync("Please pause. :)", this.<Void>printCallBack());
    }

    private void sendMatchResult() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        List<String> winners = new ArrayList<>();
        winners.add(TestUser.getFirst().userId);
        matchApi.sendMatchResultAsync(winners, this.<Void>printCallBack());
    }

    private void disconnectFromMatch() {
        if (matchApi == null) {
            textView.setText("No match is available.");
            return;
        }
        matchApi.disconnectAsync(this.<Void>printCallBack());
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.connect_to_match, R.id.send_event, R.id.disconnect_from_match,
                            R.id.direct_message, R.id.send_chat_to_match, R.id.send_match_result};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_realtime;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_to_match:
                connectToMatch();
                break;
            case R.id.send_event:
                sendEvent();
                break;
            case R.id.disconnect_from_match:
                disconnectFromMatch();
                break;
            case R.id.direct_message:
                directMessage();
                break;
            case R.id.send_chat_to_match:
                sendChatToMatch();
                break;
            case R.id.send_match_result:
                sendMatchResult();
                break;
        }
    }

    /* Match methods */
    @Override
    public void onMatchJoinedMessage(MatchJoinedMessage matchJoinedMessage) {
        textView.setText("Match joined.\n" + MainActivity.gson.toJson(matchJoinedMessage));
    }

    @Override
    public void onMatchStartedMessage(MatchStartedMessage matchStartedMessage) {
        textView.setText("Match started.\n" + MainActivity.gson.toJson(matchStartedMessage));
    }

    @Override
    public void onStartedWebhookMessage(StartedWebhookMessage startedWebhookMessage) {

    }

    @Override
    public void onDirectChatMessage(DirectChatMessage directChatMessage) {
        textView.setText("Direct chat received.\n" + MainActivity.gson.toJson(directChatMessage));
    }

    @Override
    public void onMatchEvent(MatchEvent matchEvent) {
        textView.setText("Match event received.\n" + MainActivity.gson.toJson(matchEvent));
    }

    @Override
    public void onMatchChatMessage(MatchChatMessage matchChatMessage) {
        textView.setText("Match chat message received.\n" + MainActivity.gson.toJson(matchChatMessage));
    }

    @Override
    public void onMasterMessage(MasterMessage masterMessage) {
        textView.setText("Master message received.\n" + MainActivity.gson.toJson(masterMessage));
    }

    @Override
    public void onWebhookErrorMessage(WebhookErrorMessage webhookErrorMessage) {

    }

    @Override
    public void onJoinedWebhookMessage(JoinedWebhookMessage joinedWebhookMessage) {

    }

    @Override
    public void onMatchEndedMessage(MatchEndedMessage matchEndedMessage) {
        textView.setText("Match ended.\n" + MainActivity.gson.toJson(matchEndedMessage));
    }

    @Override
    public void onMatchDisconnectMessage(MatchDisconnectMessage matchDisconnectMessage) {
        textView.setText("Match disconnected.\n" + MainActivity.gson.toJson(matchDisconnectMessage));
    }

    /* Realtime Sdk Methods */
    @Override
    public void onDisconnect() {
        textView.setText("Disconnected unexpectedly!");
    }

    @Override
    public void onException(ExceptionMessage exceptionMessage) {
        textView.setText("Exception occurred!\n" + MainActivity.gson.toJson(exceptionMessage));
    }
}
