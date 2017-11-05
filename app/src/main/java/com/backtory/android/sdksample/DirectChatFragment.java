package com.backtory.android.sdksample;

import android.view.View;

import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.LoginResponse;
import com.backtory.java.realtime.android.BacktoryRealtimeAndroidApi;
import com.backtory.java.realtime.core.listeners.ChatListener;
import com.backtory.java.realtime.core.models.ConnectResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatInvitationMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.OfflineMessageResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.SimpleChatMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserAddedMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserChatHistoryResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.UserJoinedMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserLeftMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserRemovedMessage;

import java.util.Calendar;

/**
 * Created by mohammad on 2/15/17.
 *
 */
public class DirectChatFragment extends MainActivity.AbsFragment implements ChatListener {

    /* Person-to-Person Chat */
    private void loginChatUser() {
        BacktoryUser.loginInBackground(TestUser.getFirst().username, TestUser.getFirst().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setChatListener(this);
    }

    private void realtimeConnect() {
        BacktoryRealtimeAndroidApi.getInstance().connectAsync(this.<ConnectResponse>printCallBack());
    }

    private void realtimeDisconnect() {
        BacktoryRealtimeAndroidApi.getInstance().disconnectAsync(this.<Void>printCallBack());
    }

    private void sendChatMessage() {
        BacktoryRealtimeAndroidApi.getInstance().sendChatToUserAsync(TestUser.getFirst().userId,
                "Working!", DirectChatFragment.this.<Void>printCallBack());
    }

    private void requestChatHistory() {
        BacktoryRealtimeAndroidApi.getInstance().requestUserChatHistoryAsync(
                TestUser.getFirst().userId, Calendar.getInstance().getTimeInMillis(),
                this.<UserChatHistoryResponse>printCallBack());
    }

    private void requestOfflineChats() {
        BacktoryRealtimeAndroidApi.getInstance().requestOfflineMessagesAsync(
                this.<OfflineMessageResponse>printCallBack());
    }


    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.login_chat_user, R.id.realtime_connect, R.id.realtime_disconnect,
                R.id.send_chat_message};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_direct_chat;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_chat_user:
                loginChatUser();
                break;
            case R.id.realtime_connect:
                realtimeConnect();
                break;
            case R.id.realtime_disconnect:
                realtimeDisconnect();
                break;
            case R.id.send_chat_message:
                sendChatMessage();
                break;
            case R.id.request_chat_history:
                requestChatHistory();
                break;
            case R.id.request_offline_chats:
                requestOfflineChats();
                break;
        }
    }

    @Override
    public void onPushMessage(SimpleChatMessage simpleChatMessage) {

    }

    @Override
    public void onChatMessage(SimpleChatMessage simpleChatMessage) {
        textView.setText("Chat message received:\n" + MainActivity.gson.toJson(simpleChatMessage));
    }

    @Override
    public void onGroupPushMessage(SimpleChatMessage simpleChatMessage) {

    }

    @Override
    public void onGroupChatMessage(SimpleChatMessage groupChatMessage) {

    }

    @Override
    public void onChatInvitationMessage(ChatInvitationMessage chatInvitationMessage) {

    }

    @Override
    public void onChatGroupUserAddedMessage(UserAddedMessage userAddedMessage) {

    }

    @Override
    public void onChatGroupUserJoinedMessage(UserJoinedMessage userJoinedMessage) {

    }

    @Override
    public void onChatGroupUserLeftMessage(UserLeftMessage userLeftMessage) {

    }

    @Override
    public void onChatGroupUserRemovedMessage(UserRemovedMessage userRemovedMessage) {

    }
}
