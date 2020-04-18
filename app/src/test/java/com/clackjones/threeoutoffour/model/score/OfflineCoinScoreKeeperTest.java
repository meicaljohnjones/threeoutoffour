package com.clackjones.threeoutoffour.model.score;

import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.score.CoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeper;

import org.junit.Ignore;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfflineCoinScoreKeeperTest {

    @Test
    public void shouldHaveZeroCoinsOnInit() {
        // given
        // when
        OfflineCoinScoreKeeper offlineCoinScoreKeeper =  new OfflineCoinScoreKeeper();

        // then
        assertThat(offlineCoinScoreKeeper.getCoinScore(), equalTo(0));
    }

    @Test
    public void shouldIncrementScoreByFiveWhenRoundIncremented() {
        OfflineCoinScoreKeeper offlineCoinScoreKeeper = new OfflineCoinScoreKeeper();
        Integer initialScore = offlineCoinScoreKeeper.getCoinScore();

        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, offlineCoinScoreKeeper);

        final Boolean[] isCoinScoreChangedEvtCalled = {false};
        offlineCoinScoreKeeper.addPropertyChangeListener(CoinScoreKeeper.COIN_SCORE_CHANGED_EVENT, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isCoinScoreChangedEvtCalled[0] = true;
            }
        });

        // when
        propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, 1, 2);

        // then
        Integer scoreIncrement = offlineCoinScoreKeeper.getCoinScore() - initialScore;
        assertThat(scoreIncrement, equalTo(5));

        assertThat(isCoinScoreChangedEvtCalled[0], equalTo(true));
    }

    @Test
    @Ignore
    public void shouldDecreaseScoreIfHintTaken() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void shouldIncreaseScoreWhenAdWatched() {
        fail("Not yet implemented");
    }
}
