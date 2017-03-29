package com.backtory.android.sdksample;

import android.view.View;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.model.BacktoryResponse;
import com.backtory.java.model.BacktoryUser;
import com.backtory.java.model.LoginResponse;
import com.backtory.java.realtime.android.BacktoryRealtimeAndroidApi;
import com.backtory.java.realtime.core.listeners.ChatListener;
import com.backtory.java.realtime.core.models.ConnectResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatGroupCreationResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatGroupInfo;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatGroupMembersListResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatGroupType;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatGroupsListResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.ChatInvitationMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.GroupChatHistoryResponse;
import com.backtory.java.realtime.core.models.connectivity.chat.SimpleChatMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserAddedMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserJoinedMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserLeftMessage;
import com.backtory.java.realtime.core.models.connectivity.chat.UserRemovedMessage;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by mohammad on 2/15/17.
 *
 */
public class ChatFragment extends MainActivity.AbsFragment implements ChatListener {

    /* Person-to-Person Chat */
    private void loginChatUser() {
        BacktoryUser.loginInBackground(TestUser.getFirst().username, TestUser.getFirst().password,
                this.<LoginResponse>printCallBack());
        BacktoryRealtimeAndroidApi.getInstance().setChatListener(this);
    }

    private void realtimeConnect() {
        BacktoryRealtimeAndroidApi.getInstance().connectAsync(
                "", new BacktoryCallBack<ConnectResponse>() {
                    @Override
                    public void onResponse(BacktoryResponse<ConnectResponse> backtoryResponse) {
                        ChatFragment.this.textView.setText(MainActivity.gson.toJson(backtoryResponse));
                    }
                }
        );
//                BacktoryUser.getCurrentUser().getUsername(), this.<ConnectResponse>printCallBack());
    }

    private void sendChatMessage() {
        BacktoryRealtimeAndroidApi.getInstance().sendChatToUserAsync(TestUser.getFirst().userId,
                "Working!", ChatFragment.this.<Void>printCallBack());
    }

    /* Group Chat */
    private void createChatGroup() {
        int id = new Random().nextInt(10);
        BacktoryRealtimeAndroidApi.getInstance().createChatGroupAsync(
                "MyGroup" + id, ChatGroupType.Private, this.<ChatGroupCreationResponse>printCallBack());
    }

    private List<ChatGroupInfo> chatGroupList;

    private void requestGroupsList() {
        BacktoryRealtimeAndroidApi.getInstance().requestListOfChatGroupsAsync(new BacktoryCallBack<ChatGroupsListResponse>() {
            @Override
            public void onResponse(BacktoryResponse<ChatGroupsListResponse> response) {
                ChatFragment.this.<ChatGroupsListResponse>printCallBack().onResponse(response);
                if (response.isSuccessful()) {
                    chatGroupList = response.body().getGroupInfoList();
                }
            }
        });
    }

    private void requestMembersList() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().requestListOfChatGroupMembersAsync(
                chatGroupList.get(id).getGroupId(), this.<ChatGroupMembersListResponse>printCallBack());
    }

    private void addGroupMember() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().addMemberToChatGroupAsync(
                chatGroupList.get(id).getGroupId(), TestUser.getFirst().userId, this.<Void>printCallBack());
    }

    private void removeGroupMember() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().removeMemberFromChatGroupAsync(
                chatGroupList.get(id).getGroupId(), TestUser.getFirst().userId, this.<Void>printCallBack());
    }

    private void sendChatToGroup() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().sendChatToGroupAsync(
                chatGroupList.get(id).getGroupId(), "Hello Everybodyyy!", this.<Void>printCallBack());
    }

    private void requestGroupChatHistory() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().requestGroupChatHistoryAsync(
                chatGroupList.get(id).getGroupId(), Calendar.getInstance().getTimeInMillis(),
                this.<GroupChatHistoryResponse>printCallBack());
    }

    private String invitedGroupId;

    private void inviteToGroup() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().inviteUserToChatGroupAsync(
                chatGroupList.get(id).getGroupId(), TestUser.getFirst().userId, this.<Void>printCallBack());
    }

    private void joinGroup() {
        if (invitedGroupId == null) {
            textView.setText("Invited group id is null.");
        }
        BacktoryRealtimeAndroidApi.getInstance().joinChatGroupAsync(invitedGroupId, new BacktoryCallBack<Void>() {
            @Override
            public void onResponse(BacktoryResponse<Void> response) {
                ChatFragment.this.<Void>printCallBack().onResponse(response);
                invitedGroupId = null;
            }
        });
    }

    private void leaveGroup() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().leaveChatGroupAsync(
                chatGroupList.get(id).getGroupId(), this.<Void>printCallBack());
    }

    private void makeMemberOwner() {
        if (chatGroupList == null || chatGroupList.size() == 0) {
            textView.setText("List of chat groups is null or empty.");
            return;
        }
        int id = new Random().nextInt(chatGroupList.size());
        BacktoryRealtimeAndroidApi.getInstance().addOwnerToChatGroupAsync(
                chatGroupList.get(id).getGroupId(), TestUser.getFirst().userId, this.<Void>printCallBack());
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.login_chat_user, R.id.realtime_connect, R.id.send_chat_message,
                            R.id.create_chat_group, R.id.request_groups_list, R.id.request_members_list,
                            R.id.add_group_member, R.id.remove_group_member, R.id.send_chat_to_group,
                            R.id.request_group_chat_history, R.id.invite_to_group,
                            R.id.join_group, R.id.leave_group, R.id.make_member_owner};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat;
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
            case R.id.send_chat_message:
                sendChatMessage();
                break;
            case R.id.create_chat_group:
                createChatGroup();
                break;
            case R.id.request_groups_list:
                requestGroupsList();
                break;
            case R.id.request_members_list:
                requestMembersList();
                break;
            case R.id.add_group_member:
                addGroupMember();
                break;
            case R.id.remove_group_member:
                removeGroupMember();
                break;
            case R.id.send_chat_to_group:
                sendChatToGroup();
                break;
            case R.id.request_group_chat_history:
                requestGroupChatHistory();
                break;
            case R.id.invite_to_group:
                inviteToGroup();
                break;
            case R.id.join_group:
                joinGroup();
                break;
            case R.id.leave_group:
                leaveGroup();
                break;
            case R.id.make_member_owner:
                makeMemberOwner();
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
    public void onGroupChatMessage(SimpleChatMessage simpleChatMessage) {
        textView.setText("Group chat message received:\n" + MainActivity.gson.toJson(simpleChatMessage));
    }

    @Override
    public void onChatInvitationMessage(ChatInvitationMessage chatInvitationMessage) {
        textView.setText("Chat invitation message received:\n" + MainActivity.gson.toJson(chatInvitationMessage));
        invitedGroupId = chatInvitationMessage.getGroupId();
    }

    @Override
    public void onChatGroupUserAddedMessage(UserAddedMessage userAddedMessage) {
        textView.setText("User is added to group:\n" + MainActivity.gson.toJson(userAddedMessage));
    }

    @Override
    public void onChatGroupUserJoinedMessage(UserJoinedMessage userJoinedMessage) {
        textView.setText("User joined to group:\n" + MainActivity.gson.toJson(userJoinedMessage));
    }

    @Override
    public void onChatGroupUserLeftMessage(UserLeftMessage userLeftMessage) {
        textView.setText("User left the group:\n" + MainActivity.gson.toJson(userLeftMessage));
    }

    @Override
    public void onChatGroupUserRemovedMessage(UserRemovedMessage userRemovedMessage) {
        textView.setText("User is removed from group:\n" + MainActivity.gson.toJson(userRemovedMessage));
    }
}
