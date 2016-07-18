package com.backtory.android.sdksample;

import com.backtory.androidsdk.annotation.EventName;
import com.backtory.androidsdk.annotation.FieldName;
import com.backtory.androidsdk.model.BacktoryEvent;


public class GameOverEvent extends BacktoryEvent {
    @EventName
    public static final String eventName = "GameOver";

    @FieldName("Coin")
    int coinValue;

    @FieldName("Time")
    int timeValue;

    public GameOverEvent(int coinValue, int timeValue) {
        this.coinValue = coinValue;
        this.timeValue = timeValue;
    }
}
