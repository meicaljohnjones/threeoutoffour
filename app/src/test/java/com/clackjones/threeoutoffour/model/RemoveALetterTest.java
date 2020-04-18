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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RemoveALetterTest {

    @Mock
    Context context;

    @Mock
    RoundProvider roundProvider;

    @Mock
    GameStateProvider gameStateProvider;

    @InjectMocks
    EventFiringThreeOutOfFourGame game;


    @Test
    public void shouldRemoveFirstNonMatchingLetterFromChoices() {
        // given
        Round testRound1 = new Round(1, -1, -1, -1, -1,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
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


        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT,
                hintFiredPropertyChangeListener);

        // when
        game.performRemoveALetterHint();

        // then
        // "o" choice (first non-matching)
        ThreeOutOfFourChoice choice = game.getChoices().get(3);
        assertThat(choice.getIsAlreadySelected(), is(true));

        // u choice (second non-matching)
        ThreeOutOfFourChoice choice2 = game.getChoices().get(4);
        assertThat(choice2.getIsAlreadySelected(), is(false));

        // shouldn't have affected proposed answer
        assertThat(game.getProposedAnswer(), equalTo(""));

        assertThat(isHintFired[0], is(true));
    }

    @Test
    public void shouldRemoveSecondNonMatchingLetterFromChoicesWhenCalledTwice() {
        // given
        Round testRound1 = new Round(1, -1, -1, -1, -1,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
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


        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT,
                hintFiredPropertyChangeListener);

        // when
        game.performRemoveALetterHint();
        game.performRemoveALetterHint();

        // then
        // "o" choice (first non-matching)
        ThreeOutOfFourChoice choice = game.getChoices().get(3);
        assertThat(choice.getIsAlreadySelected(), is(true));

        // u choice (second non-matching)
        ThreeOutOfFourChoice choice2 = game.getChoices().get(4);
        assertThat(choice2.getIsAlreadySelected(), is(true));

        // shouldn't have affected proposed answer
        assertThat(game.getProposedAnswer(), equalTo(""));

        assertThat(isHintFired[0], is(true));
    }
}
