package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeListener;

public interface CoinScoreKeeper extends PropertyChangeListener {
    final String COIN_SCORE_CHANGED_EVENT = "COIN_SCORE_CHANGED_EVENT";

    /**
     * @return the 'number of coins' we currently have
     */
    Integer getCoinScore();

    /**
     * @param numberOfCoins num to buy
     */
    void buyCoins(Integer numberOfCoins);

    /**
     * Fire events to  all PropertyChangeListeners when choice made, round won etc.
     * @param propertyChangeListener
     */
    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * Fire specific events to  PropertyChangeListeners when choice made,
     * if the current round was won, if the current choice was false
     * @param propertyName
     * @param propertyChangeListener
     */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);
}
