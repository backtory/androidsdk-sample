package com.backtory.android.sdksample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backtory.java.internal.BacktoryLeaderBoard.LeaderBoardRank;
import com.backtory.java.internal.BacktoryLeaderBoard.LeaderBoardResponse;

import java.util.Random;


public class GameFragment extends MainActivity.AbsFragment {
    private static Random random = new Random(System.currentTimeMillis());
    TextView coinView;
    TextView timeView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_game;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        coinView = (TextView) v.findViewById(R.id.textview_coin);
        timeView = (TextView) v.findViewById(R.id.textview_time);
        return v;
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.button_send_event, R.id.button_leader_board_rank, R.id.button_leader_board_tops,
                R.id.button_leader_board_around_me};
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshTimeCoin();
    }

    void sendEvent() {
        new GameOverEvent(Integer.parseInt(coinView.getText().toString()),
                            Integer.parseInt(timeView.getText().toString()))
                .sendInBackground(this.<Void>printCallBack());
        refreshTimeCoin();
    }

    void getPlayerRank() {
        new TopPlayersLeaderBoard().getPlayerRankInBackground(this.<LeaderBoardRank>printCallBack());
    }

    void getTopPlayers() {
        new TopPlayersLeaderBoard().getTopPlayersInBackground(2, this.<LeaderBoardResponse>printCallBack());
    }

    void getAroundMePlayers() {
        new TopPlayersLeaderBoard().getPlayersAroundMeInBackground(2, this.<LeaderBoardResponse>printCallBack());
    }

    private void refreshTimeCoin() {
        int coinValue = random.nextInt(100);
        int timeValue = random.nextInt(200);
        coinView.setText(String.valueOf(coinValue));
        timeView.setText(String.valueOf(timeValue));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_event:
                sendEvent();
                break;
            case R.id.button_leader_board_rank:
                getPlayerRank();
                break;
            case R.id.button_leader_board_tops:
                getTopPlayers();
                break;
            case R.id.button_leader_board_around_me:
                getAroundMePlayers();
                break;
        }
    }
}
