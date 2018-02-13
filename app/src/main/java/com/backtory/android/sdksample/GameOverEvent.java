package com.backtory.android.sdksample;


import com.backtory.java.annotation.EventName;
import com.backtory.java.annotation.FieldName;
import com.backtory.java.internal.BacktoryEvent;

/**
 * Created by Alireza Farahani on 7/2/2016.
 *
 */
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
