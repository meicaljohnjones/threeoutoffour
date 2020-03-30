package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OfflineCoinScoreKeeper implements CoinScoreKeeper {
    private Integer coinScore;

    public OfflineCoinScoreKeeper() {
        this.coinScore = 0;
    }

    @Override
    public Integer getCoinScore() {
        return this.coinScore;
    }

    @Override
    public void buyCoins(Integer numberOfCoins) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT)) {
            this.addCoins(5);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private void addCoins(Integer numCoins) {
        this.coinScore = this.coinScore + numCoins;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
