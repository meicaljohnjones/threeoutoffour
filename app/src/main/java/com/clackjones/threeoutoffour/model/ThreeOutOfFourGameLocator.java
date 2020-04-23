package com.clackjones.threeoutoffour.model;

import android.content.Context;

import com.clackjones.threeoutoffour.score.CoinScoreKeeper;
import com.clackjones.threeoutoffour.state.SerializableGameStateProvider;

public class ThreeOutOfFourGameLocator {
    private static ThreeOutOfFourGameLocator locator;
    private ThreeOutOfFourGame threeOutOfFourGame;

    private ThreeOutOfFourGameLocator(CoinScoreKeeper coinScoreKeeper, Context context) {
        threeOutOfFourGame = new EventFiringThreeOutOfFourGame(RoundProvider.getInstance(),
                SerializableGameStateProvider.getInstance(), coinScoreKeeper, context);
    }

    public static ThreeOutOfFourGameLocator getInstance(CoinScoreKeeper coinScoreKeeper, Context context) {
        if (locator == null) {
            locator = new ThreeOutOfFourGameLocator(coinScoreKeeper, context);
        }
        return locator;
    }

    public ThreeOutOfFourGame threeOutOfFourGame() {
        return threeOutOfFourGame;
    }
}
