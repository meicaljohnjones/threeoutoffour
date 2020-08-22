package com.clackjones.threeoutoffour.score;

import android.content.Context;
import android.content.res.Resources;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class OfflineCoinScoreKeeper implements CoinScoreKeeper {
    private static final long serialVersionUID = 6529685098267757690L;

    private int coinScore;
    private PropertyChangeSupport propertyChangeSupport;
    private transient Context applicationContext;

    public OfflineCoinScoreKeeper() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.coinScore = 0;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Integer getCoinScore() {
        return this.coinScore;
    }

    /**
     * To be used for serialization purposes only
     */
    public void setCoinScore(Integer score) {
        this.coinScore = score;
        this.recitfyCoinScore();
    }

    @Override
    public void buyCoins(Integer numberOfCoins) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Resources r = applicationContext.getResources();

        if (evt.getPropertyName().equals(ThreeOutOfFourGame.WON_ROUND_EVENT)) {
            this.addCoinsSilently(r.getInteger(R.integer.increment_score_new_round));
        } else if (evt.getPropertyName().equals(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT)) {
            this.removeCoins(r.getInteger(R.integer.coins_required_remove_letter));
        } else if (evt.getPropertyName().equals(ThreeOutOfFourGame.HINT_LETTER_REVEALED_EVENT)) {
            this.removeCoins(r.getInteger(R.integer.coins_required_reveal_letter));
        } else if (evt.getPropertyName().equals(ThreeOutOfFourGame.HINT_SKIP_ROUND_EVENT)) {
            this.removeCoins(r.getInteger(R.integer.coins_required_skip_round));
        } else if (evt.getPropertyName().equals(ThreeOutOfFourGame.RESET_GAME_EVENT)) {
            this.coinScore = 0;
        }

        OfflineCoinScoreKeeperProvider.getInstance().saveCoinScoreKeeper(this.applicationContext,
                this);
    }

    private void addCoinsSilently(Integer numCoins) {
        this.coinScore = this.coinScore + numCoins;
        this.recitfyCoinScore();
    }

    public void addCoins(Integer numCoins) {
        int oldCoinScore = this.coinScore;
        addCoinsSilently(numCoins);

        this.propertyChangeSupport.firePropertyChange(CoinScoreKeeper.COIN_SCORE_CHANGED_EVENT, oldCoinScore, this.coinScore);
    }

    private void removeCoins(Integer numCoins) {
        this.addCoins(-numCoins);
    }

    private void recitfyCoinScore() {
        if (this.coinScore < 0) {
            this.coinScore = 0;
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        boolean isListenerAlreadyRegistered = Arrays.asList(this.propertyChangeSupport.getPropertyChangeListeners()).contains(propertyChangeListener);
        if (!isListenerAlreadyRegistered) {
            this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
        }
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
        boolean isListenerAlreadyRegistered = Arrays.asList(this.propertyChangeSupport.getPropertyChangeListeners(propertyName)).contains(propertyChangeListener);
        if (!isListenerAlreadyRegistered) {
            this.propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
        }
    }
}
