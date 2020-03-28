package com.clackjones.threeoutoffour.model;

import android.content.Context;

import com.clackjones.threeoutoffour.state.GameStateProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RestartGameTest {

    @Mock
    Context context;

    @Mock
    RoundProvider roundProvider;

    @Mock
    GameStateProvider gameStateProvider;

    @InjectMocks
    EventFiringThreeOutOfFourGame game;


    @Test
    public void shouldResetRoundNumberAndRunInitializeOnRestart() {
        // given
        Round testRound1 = new Round(1, -1, -1, -1, -1,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});
        Round testRound2 = new Round(2, -2, -2, -2, -2,
                "iea",
                new String[]{"i", "e", "a", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(roundProvider.getNextRound(1)).willReturn(testRound2);

        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        final Boolean[] isRoundNumberEventFired = {false};
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isRoundNumberEventFired[0] = true;
            }
        };

        game.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, propertyChangeListener);

        game.makeChoice(game.getChoices().get(0));
        game.makeChoice(game.getChoices().get(1));
        game.makeChoice(game.getChoices().get(2));
        // make sure got to second round
        assertThat(game.getCurrentRoundNumber(), equalTo(2));

        // when
        game.restartGame();

        // then
        verify(gameStateProvider, times(1)).deleteGame(context);

        assertThat(game.getCurrentRoundNumber(), equalTo(1));
        assertThat(game.getProposedAnswer(), equalTo(""));

        assertThat(game.getChoices().get(0).getValue(), equalTo("a"));
        assertThat(game.getChoices().get(1).getValue(), equalTo("e"));
        assertThat(game.getChoices().get(2).getValue(), equalTo("i"));

        assertThat(isRoundNumberEventFired[0], equalTo(true));
    }
}
