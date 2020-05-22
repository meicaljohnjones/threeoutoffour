package com.clackjones.threeoutoffour.model;

import android.content.Context;

import com.clackjones.threeoutoffour.score.CoinScoreKeeper;
import com.clackjones.threeoutoffour.state.GameStateProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SkipRoundHintTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Mock
    Context context;

    @Mock
    RoundProvider roundProvider;

    @Mock
    GameStateProvider gameStateProvider;

    @Mock
    CoinScoreKeeper coinScoreKeeper;

    @InjectMocks
    EventFiringThreeOutOfFourGame game;

    @Test
    public void shouldGoToNextRoundIfHaveEnoughCoins() throws InsufficientCoinScoreException {
        // given
        given(coinScoreKeeper.getCoinScore()).willReturn(ThreeOutOfFourGame.HINT_SKIP_ROUND_COINS_REQUIRED);
        Round testRound1 = new Round(1, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        Round testRound2 = new Round(2, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(roundProvider.getNextRound(1)).willReturn(testRound2);
        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        final Boolean[] isHintFired = {false};
        PropertyChangeListener hintFiredPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isHintFired[0] = true;
            }
        };

        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_SKIP_ROUND_EVENT,
                hintFiredPropertyChangeListener);

        final Boolean[] isRoundIncrementFired = {false};
        PropertyChangeListener roundIncrementedPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getPropertyName();
                isRoundIncrementFired[0] = true;
            }
        };
        game.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT,
                roundIncrementedPropertyChangeListener);

        // when
        game.performSkipRoundHint();

        assertThat(game.getCurrentRoundNumber(), equalTo(2));
        assertThat(isRoundIncrementFired[0], is(true));
        assertThat(isHintFired[0], is(true));
    }

    @Test
    public void shouldNotIncrementRoundIfInsufficientFunds() {
        // given
        int insufficientCoins = ThreeOutOfFourGame.HINT_SKIP_ROUND_COINS_REQUIRED - 1;
        given(coinScoreKeeper.getCoinScore()).willReturn(insufficientCoins);
        Round testRound1 = new Round(1, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        Round testRound2 = new Round(2, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(roundProvider.getNextRound(1)).willReturn(testRound2);
        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        final Boolean[] isHintFired = {false};
        PropertyChangeListener hintFiredPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isHintFired[0] = true;
            }
        };

        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_SKIP_ROUND_EVENT,
                hintFiredPropertyChangeListener);

        final Boolean[] isRoundIncrementFired = {false};
        PropertyChangeListener roundIncrementedPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getPropertyName();
                isRoundIncrementFired[0] = true;
            }
        };
        game.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT,
                roundIncrementedPropertyChangeListener);

        // when
        InsufficientCoinScoreException s = null;
        try {
            game.performSkipRoundHint();
        } catch (InsufficientCoinScoreException e) {
            s = e;
        }

        assertThat(s, notNullValue());
        assertThat(game.getCurrentRoundNumber(), equalTo(1));
        assertThat(isRoundIncrementFired[0], is(false));
        assertThat(isHintFired[0], is(false));
    }

}
