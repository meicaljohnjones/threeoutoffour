package com.clackjones.threeoutoffour.model;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.*;
import org.mockito.runners.MockitoJUnitRunner;


import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class InMemoryThreeOutOfFourGameTest {
    @Mock
    RoundProvider roundProvider;

    @InjectMocks
    InMemoryThreeOutOfFourGame game;

    @Test
    public void shouldGetListOfChoicesAndAnswerAndEmptyProposedAnswerOnInitializeAndRoundNumberIncrementedEvent() {
        // given
        final Boolean[] isRoundNumberEventFired = {false};
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isRoundNumberEventFired[0] = true;
            }
        };

        Round testRound = new Round(42, 43, 44, 45,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});
        given(roundProvider.getNextRound()).willReturn(testRound);

        game.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, propertyChangeListener);

        // when
        game.initialize();

        // then
        assertThat(game.getChoices().size(), equalTo(5));
        List<ThreeOutOfFourChoice> choices = game.getChoices();

        assertThat(choices.get(0).isAlreadySelected(), equalTo(false));
        assertThat(choices.get(0).getValue(), equalTo("a"));

        assertThat(choices.get(2).isAlreadySelected(), equalTo(false));
        assertThat(choices.get(2).getValue(), equalTo("i"));

        assertThat(choices.get(4).isAlreadySelected(), equalTo(false));
        assertThat(choices.get(4).getValue(), equalTo("u"));

        assertThat(game.getProposedAnswer(), equalTo(""));
        assertThat(game.getCurrentRoundNumber(), equalTo(1));
        assertThat(game.answer, equalTo("aei"));

        assertThat(game.getLettersRemaining(), equalTo(3));

        assertThat(isRoundNumberEventFired[0], equalTo(true));

        assertThat(game.getCurrBottomLeftImage(), not(equalTo(-1)));
        assertThat(game.getCurrBottomRightImage(), not(equalTo(-1)));
        assertThat(game.getCurrTopLeftImage(), not(equalTo(-1)));
        assertThat(game.getCurrTopRightImage(), not(equalTo(-1)));
    }

    @Test
    public void shouldNotIncrementIfInitializeCalledTwice() {
        Round testRound = new Round(-1, -1, -1, -1,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});
        given(roundProvider.getNextRound()).willReturn(testRound);

        // when
        game.initialize();
        game.initialize();

        // then
        assertThat(game.getCurrentRoundNumber(), equalTo(1));
    }

    @Test
    public void shouldSetGameChoiceToActivatedFireLettersRemainingDecrementedEventAndUpdateProposedAnswerOnMakeChoice() {
        // given
        final Boolean[] isChoiceMadeEventFired = {false};
        final int[] choiceIndex = {-1};
        PropertyChangeListener choiceMadeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                IndexedPropertyChangeEvent indexedEvt = (IndexedPropertyChangeEvent) evt;
                isChoiceMadeEventFired[0] = true;
                choiceIndex[0] = indexedEvt.getIndex();
            }
        };

        final Boolean[] isLettersRemainingDecrementedEventFired = {false};
        PropertyChangeListener lettersRemainingListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isLettersRemainingDecrementedEventFired[0] = true;
            }
        };

        final Boolean[] isProposedAnswerChangedFired = {false};
        PropertyChangeListener proposedAnswerChangedListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isProposedAnswerChangedFired[0] = true;
            }
        };

        Round testRound = new Round(42, 43, 44, 45,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});
        given(roundProvider.getNextRound()).willReturn(testRound);
        game.addPropertyChangeListener(ThreeOutOfFourGame.CHOICE_MADE_EVENT, choiceMadeListener);
        game.addPropertyChangeListener(ThreeOutOfFourGame.LETTERS_REMAINING_DECREMENTED_EVENT, lettersRemainingListener);
        game.addPropertyChangeListener(ThreeOutOfFourGame.PROPOSED_ANSWER_CHANGED_EVENT, proposedAnswerChangedListener);

        // when
        game.initialize();
        ThreeOutOfFourChoice choice = game.getChoices().get(1);
        game.makeChoice(choice);

        // then
        assertThat(isChoiceMadeEventFired[0], equalTo(true));
        assertThat(isLettersRemainingDecrementedEventFired[0], equalTo(true));
        assertThat(isProposedAnswerChangedFired[0], equalTo(true));

        // check choice state changed
        ThreeOutOfFourChoice updatedChoice = game.getChoices().get(1);
        assertThat(choiceIndex[0], equalTo(1));
        assertThat(updatedChoice.isAlreadySelected(), equalTo(true));
        assertThat(game.getLettersRemaining(), equalTo(2));

        assertThat(game.getProposedAnswer(), equalTo(choice.getValue()));
    }

    @Test
    public void shouldNotFireEventWhenMakeChoiceOnChoiceAlreadySelected() {
        // given
        final Boolean[] isChoiceMadeEventFired = {false};
        final int[] choiceIndex = {-1};
        PropertyChangeListener choiceMadeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                IndexedPropertyChangeEvent indexedEvt = (IndexedPropertyChangeEvent) evt;
                isChoiceMadeEventFired[0] = true;
                choiceIndex[0] = indexedEvt.getIndex();
            }
        };

        final Boolean[] isLettersRemainingDecrementedEventFired = {false};
        PropertyChangeListener lettersRemainingListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isLettersRemainingDecrementedEventFired[0] = true;
            }
        };

        Round testRound = new Round(42, 43, 44, 45,
                "aei",
                new String[]{"a", "e", "i", "o", "u"});
        given(roundProvider.getNextRound()).willReturn(testRound);


        game.initialize();
        ThreeOutOfFourChoice choice = game.getChoices().get(1);
        game.makeChoice(choice);

        game.addPropertyChangeListener(ThreeOutOfFourGame.CHOICE_MADE_EVENT, choiceMadeListener);
        game.addPropertyChangeListener(ThreeOutOfFourGame.LETTERS_REMAINING_DECREMENTED_EVENT, lettersRemainingListener);

        // when
        // make same choice
        game.makeChoice(choice);

        // then
        assertThat(isChoiceMadeEventFired[0], equalTo(false));
        assertThat(isLettersRemainingDecrementedEventFired[0], equalTo(false));

        // check choice state changed
        ThreeOutOfFourChoice updatedChoice = game.getChoices().get(1);
        assertThat(updatedChoice.isAlreadySelected(), equalTo(true));
        assertThat(game.getLettersRemaining(), equalTo(2));
    }


    @Test
    public void shouldFireIncorrectProposedAnswerEventWhenProposedAnswerCompleteAndIncorrectAndAllGameValuesReset() {
        // given
        Round testRound = new Round(-1, -1, -1, -1,
                "a",
                new String[]{"a", "e", "i", "o", "u"});

        given(roundProvider.getNextRound()).willReturn(testRound);

        final Boolean[] isWrongAnswerEventFired = {false};
        PropertyChangeListener wrongAnswerEvtListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isWrongAnswerEventFired[0] = true;
            }
        };

        // when
        game.addPropertyChangeListener(ThreeOutOfFourGame.INCORRECT_PROPOSED_ANSWER_EVENT, wrongAnswerEvtListener);
        game.initialize();
        ThreeOutOfFourChoice wrongChoice = game.getChoices().get(1);
        game.makeChoice(wrongChoice);

        // when
        assertThat(isWrongAnswerEventFired[0], equalTo(true));

        // all choices deselected
        for (ThreeOutOfFourChoice choice: game.getChoices()) {
            assertThat(choice.isAlreadySelected(), equalTo(false));
        }

        assertThat(game.getLettersRemaining(), equalTo(1));

        // proposed answer empty -proposed answer changed event
        assertThat(game.getProposedAnswer(), equalTo(""));
    }

    @Test
    public void shouldFireRoundNumberIncrementedEventAndUpdateVariablesWhenProposedAnswerIsCorrect() {
        // given
        Round testRound1 = new Round(-1, -1, -1, -1,
                "a",
                new String[]{"a", "e", "i", "o", "u"});

        Round testRound2 = new Round(-2, -2, -2, -2,
                "ee",
                new String[]{"e", "i", "o", "u", "a", "a"});
        given(roundProvider.getNextRound()).willReturn(testRound1, testRound2);

        final Boolean[] isRoundNumberEventFired = {false};
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                evt.getNewValue();
                isRoundNumberEventFired[0] = true;
            }
        };

        // when
        game.addPropertyChangeListener(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, propertyChangeListener);
        game.initialize();
        ThreeOutOfFourChoice correctChoice = game.getChoices().get(0);
        game.makeChoice(correctChoice);

        // when
        assertThat(isRoundNumberEventFired[0], equalTo(true));

        // all choices deselected and reset to ones for new round
        for (ThreeOutOfFourChoice choice: game.getChoices()) {
            System.out.println(choice.getValue());
        }
        System.out.println(game.getChoices());
        assertThat(game.getChoices().size(), equalTo(6));
        assertThat(game.getChoices().get(0).getValue(), equalTo("e"));
        for (ThreeOutOfFourChoice choice: game.getChoices()) {
            assertThat(choice.isAlreadySelected(), equalTo(false));
        }

        assertThat(game.getLettersRemaining(), equalTo(2));

        // proposed answer empty -proposed answer changed event
        assertThat(game.getProposedAnswer(), equalTo(""));

        assertThat(game.getCurrTopRightImage(), equalTo(-2));
        assertThat(game.getCurrTopLeftImage(), equalTo(-2));
        assertThat(game.getCurrBottomRightImage(), equalTo(-2));
        assertThat(game.getCurrBottomRightImage(), equalTo(-2));
    }

}
