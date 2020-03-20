package com.clackjones.threeoutoffour.model;

public class ThreeOutOfFourGameLocator {
    private static ThreeOutOfFourGameLocator locator;
    private ThreeOutOfFourGame threeOutOfFourGame;

    private ThreeOutOfFourGameLocator() {
        threeOutOfFourGame = new InMemoryThreeOutOfFourGame(RoundProvider.get());
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
