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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RevealALetterTest {

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
    public void shouldClearAllLettersIfFirstIncorrectAndPlaceFirstCorrectLetter() throws InsufficientCoinScoreException {
        // given
        given(coinScoreKeeper.getCoinScore()).willReturn(ThreeOutOfFourGame.HINT_LETTER_REVEALED_COINS_REQUIRED);
        Round testRound1 = new Round(1, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        // given first choice is wrong (e)
        game.makeChoice(1);

        final Boolean[] isHintFired = {false};
        PropertyChangeListener hintFiredPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isHintFired[0] = true;
            }
        };

        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_LETTER_REVEALED_EVENT,
                hintFiredPropertyChangeListener);

        // when
        game.performRevealALetterHint();

        // then
        // "a" choice (first matching)
        ThreeOutOfFourChoice choice = game.getChoices().get(0);
        assertThat(choice.getIsAlreadySelected(), is(true));

        // should now be "a"
        assertThat(game.getProposedAnswer(), equalTo("a"));

        assertThat(isHintFired[0], is(true));
    }

    @Test
    public void shouldClearAllButFirstLetterIfFirstCorrectAndPlaceSecondCorrectLetter() throws InsufficientCoinScoreException {
        // given
        given(coinScoreKeeper.getCoinScore()).willReturn(ThreeOutOfFourGame.HINT_LETTER_REVEALED_COINS_REQUIRED);
        Round testRound1 = new Round(1, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        // given first choice is correct (a)
        game.makeChoice(0);
        // given second choice is incorrect (o)
        game.makeChoice(3);

        final Boolean[] isHintFired = {false};
        PropertyChangeListener hintFiredPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isHintFired[0] = true;
            }
        };

        game.addPropertyChangeListener(ThreeOutOfFourGame.HINT_LETTER_REVEALED_EVENT,
                hintFiredPropertyChangeListener);

        // when
        game.performRevealALetterHint();

        System.out.println(game.getProposedAnswer());

        // then
        // "a" choice (first matching)
        ThreeOutOfFourChoice choice = game.getChoices().get(0);
        assertThat(choice.getIsAlreadySelected(), is(true));

        ThreeOutOfFourChoice choice2 = game.getChoices().get(1);
        assertThat(choice2.getIsAlreadySelected(), is(true));

        // should now be "a"
        assertThat(game.getProposedAnswer(), equalTo("ae"));

        assertThat(isHintFired[0], is(true));
    }

    @Test
    public void shouldThrowInsufficientFundExceptionWhenInsufficientFunds() throws InsufficientCoinScoreException {
        // given
        exceptionRule.expect(InsufficientCoinScoreException.class);

        final int insufficientCoinSum = ThreeOutOfFourGame.HINT_LETTER_REMOVED_COINS_REQUIRED - 1;
        given(coinScoreKeeper.getCoinScore()).willReturn(insufficientCoinSum);
        Round testRound1 = new Round(1, "image1".getBytes(),
                "image2".getBytes(),
                "image3".getBytes(),
                "image4".getBytes(),
                "aei",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound(0)).willReturn(testRound1);
        given(gameStateProvider.loadGameStateOrCreateNew(any(Context.class))).willReturn(new GameState());
        game.initialize();

        // when
        game.performRevealALetterHint();
    }
}