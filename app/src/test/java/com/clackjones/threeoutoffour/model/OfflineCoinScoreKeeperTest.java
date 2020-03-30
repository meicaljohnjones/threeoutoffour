package com.clackjones.threeoutoffour.model;

import org.junit.Ignore;
import org.junit.Test;

import java.beans.PropertyChangeSupport;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfflineCoinScoreKeeperTest {

    @Test
    public void shouldHaveZeroCoinsOnInit() {
        // given
        // when
        OfflineCoinScoreKeeper offlineCoinScoreKeeper = new OfflineCoinScoreKeeper();

        // then
        assertThat(offlineCoinScoreKeeper.getCoinScore(), equalTo(0));
    }

    @Test
    public void shouldIncrementScoreByFiveWhenRoundIncremented() {
        OfflineCoinScoreKeeper offlineCoinScoreKeeper = new OfflineCoinScoreKeeper();
        Integer initialScore = offlineCoinScoreKeeper.getCoinScore();

        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, offlineCoinScoreKeeper);

        // when
        propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, 1, 2);

        // then
        Integer scoreIncrement = offlineCoinScoreKeeper.getCoinScore() - initialScore;
        assertThat(scoreIncrement, equalTo(5));
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
