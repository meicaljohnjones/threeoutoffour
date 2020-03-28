package com.clackjones.threeoutoffour.model;

import android.content.Context;

import com.clackjones.threeoutoffour.state.SerializableGameStateProvider;

public class ThreeOutOfFourGameLocator {
    private static ThreeOutOfFourGameLocator locator;
    private ThreeOutOfFourGame threeOutOfFourGame;

    private ThreeOutOfFourGameLocator(Context context) {
        threeOutOfFourGame = new EventFiringThreeOutOfFourGame(RoundProvider.getInstance(),
                SerializableGameStateProvider.getInstance(), context);
    }

    public static ThreeOutOfFourGameLocator getInstance(Context context) {
        if (locator == null) {
            locator = new ThreeOutOfFourGameLocator(context);
        }
        return locator;
    }

    public ThreeOutOfFourGame threeOutOfFourGame() {
        return threeOutOfFourGame;
    }
}
