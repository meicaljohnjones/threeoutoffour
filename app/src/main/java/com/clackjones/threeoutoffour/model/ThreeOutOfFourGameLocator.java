package com.clackjones.threeoutoffour.model;

import com.clackjones.threeoutoffour.state.SerializableGameStateProvider;

public class ThreeOutOfFourGameLocator {
    private static ThreeOutOfFourGameLocator locator;
    private ThreeOutOfFourGame threeOutOfFourGame;

    private ThreeOutOfFourGameLocator() {
        threeOutOfFourGame = new EventFiringThreeOutOfFourGame(RoundProvider.getInstance(), SerializableGameStateProvider.getInstance());
    }

    public static ThreeOutOfFourGameLocator getInstance() {
        if (locator == null) {
            locator = new ThreeOutOfFourGameLocator();
        }
        return locator;
    }

    public ThreeOutOfFourGame threeOutOfFourGame() {
        return threeOutOfFourGame;
    }
}
