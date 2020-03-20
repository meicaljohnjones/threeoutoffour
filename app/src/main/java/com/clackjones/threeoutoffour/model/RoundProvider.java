package com.clackjones.threeoutoffour.model;

import com.clackjones.threeoutoffour.R;

import java.util.LinkedList;
import java.util.Queue;

public class RoundProvider {
    private static RoundProvider roundProvider = null;
    private Queue<Round> rounds = new LinkedList<>();

    private RoundProvider() {

    }

    public static RoundProvider get() {
        if (RoundProvider.roundProvider == null) {
            RoundProvider.roundProvider = new RoundProvider();
            RoundProvider.roundProvider.initialize();
        }
        return RoundProvider.roundProvider;
    }

    private void initialize() {
        //round 1 - medium
        Round round1 = new Round(R.drawable.rain, R.drawable.cat, R.drawable.dog, R.drawable.water,
                "WET", new String[]{"E", "T", "W", "A", "Y", "A", "T", "B", "E", "A"});

        round1.shuffleRandomLetters();
        rounds.offer(round1);

        //round 2 - medium
        Round round2 = new Round(R.drawable.box, R.drawable.vase, R.drawable.giftbox, R.drawable.package1,
                "BOX", new String[]{"B", "T", "O", "X", "Y", "A", "T", "B", "E", "A"});

        round2.shuffleRandomLetters();
        rounds.offer(round2);

        //round 3 - easy
        Round round3 = new Round(R.drawable.fly, R.drawable.bird, R.drawable.plane, R.drawable.frog,
                "FLY", new String[]{"D", "F", "L", "H", "Y", "A", "T", "B", "A", "T"});

        round3.shuffleRandomLetters();
        rounds.offer(round3);

        //round 4 - easy
        Round round4 = new Round(R.drawable.wave, R.drawable.sound, R.drawable.plane, R.drawable.hello,
                "WAVE", new String[]{"W", "E", "A", "V", "Y", "A", "T", "B", "E", "A"});

        round4.shuffleRandomLetters();
        rounds.offer(round4);

        //round 5 - easy
        Round round5 = new Round(R.drawable.bowl, R.drawable.toilet, R.drawable.bowling, R.drawable.bird,
                "BOWL", new String[]{"W", "O", "L", "V", "Y", "A", "T", "B", "E", "A"});

        round5.shuffleRandomLetters();
        rounds.offer(round5);

        //round 6 - easy
        Round round6 = new Round(R.drawable.cry, R.drawable.torn, R.drawable.cry2, R.drawable.bowl,
                "TEAR", new String[]{"W", "E", "A", "V", "R", "A", "T", "E", "A", "I"});

        round6.shuffleRandomLetters();
        rounds.offer(round6);

        //round 7
        Round round7 = new Round(R.drawable.tie, R.drawable.tying, R.drawable.knot, R.drawable.bowl,
                "TIE", new String[]{"W", "E", "A", "V", "R", "A", "T", "I", "E", "A"});

        round7.shuffleRandomLetters();
        rounds.offer(round7);

        //round 8 - easy
        Round round8 = new Round(R.drawable.stop, R.drawable.sign, R.drawable.sign2, R.drawable.tie,
                "SIGN", new String[]{"W", "E", "S", "N", "I", "G", "T", "I", "E", "A"});

        round8.shuffleRandomLetters();
        rounds.offer(round8);

        //round 9 - medium
        Round round9 = new Round(R.drawable.catgroom, R.drawable.groom, R.drawable.monkeys, R.drawable.bird,
                "GROOM", new String[]{"W", "O", "S", "M", "R", "G", "O", "I", "E", "A"});

        round9.shuffleRandomLetters();
        rounds.offer(round9);

        //round 10
        Round round10 = new Round(R.drawable.bow, R.drawable.horse, R.drawable.archer, R.drawable.groom,
                "BOW", new String[]{"W", "O", "S", "M", "R", "G", "B", "I", "E", "A"});

        round10.shuffleRandomLetters();
        rounds.offer(round10);

        //round 11
        Round round11 = new Round(R.drawable.film, R.drawable.bread, R.drawable.toiletpaper, R.drawable.knot,
                "ROLL", new String[]{"W", "O", "S", "M", "R", "L", "L", "I", "E", "A"});

        round11.shuffleRandomLetters();
        rounds.offer(round11);

        //round 12 - medium
        Round round12 = new Round(R.drawable.rings, R.drawable.planet, R.drawable.tree, R.drawable.film,
                "RINGS", new String[]{"E", "S", "G", "N", "I", "R", "T", "E", "A", "I"});

        round12.shuffleRandomLetters();
        rounds.offer(round12);

        //round 13 - medium
        Round round13 = new Round(R.drawable.gold, R.drawable.soap, R.drawable.chocolate, R.drawable.giftbox,
                "BARS", new String[]{"E", "A", "S", "N", "I", "R", "T", "B", "A", "I"});

        round13.shuffleRandomLetters();
        rounds.offer(round13);

        //round 14 - medium
        Round round14 = new Round(R.drawable.fire, R.drawable.soccer, R.drawable.tennis, R.drawable.gold,
                "MATCH", new String[]{"E", "A", "S", "C", "H", "R", "T", "M", "A", "I"});

        round14.shuffleRandomLetters();
        rounds.offer(round14);

        //round 15 - easy
        Round round15 = new Round(R.drawable.watch, R.drawable.pocketwatch, R.drawable.binoculars, R.drawable.fire,
                "WATCH", new String[]{"E", "A", "S", "C", "H", "R", "T", "W", "A", "I"});

        round15.shuffleRandomLetters();
        rounds.offer(round15);

        //round 16 - easy
        Round round16 = new Round(R.drawable.bark1, R.drawable.dogbark, R.drawable.bark2, R.drawable.leaf,
                "BARK", new String[]{"E", "A", "S", "C", "H", "R", "T", "W", "A", "I"});

        round16.shuffleRandomLetters();
        rounds.offer(round16);

        //round 17
        Round round17 = new Round(R.drawable.bass1, R.drawable.bass2, R.drawable.bass, R.drawable.tea,
                "BASS", new String[]{"E", "A", "S", "C", "H", "R", "T", "W", "A", "I"});

        round17.shuffleRandomLetters();
        rounds.offer(round17);

        //round 18
        Round round18 = new Round(R.drawable.cranebird1, R.drawable.crane2, R.drawable.cranebird2, R.drawable.firetruck,
                "CRANE", new String[]{"E", "A", "S", "C", "N", "R", "T", "W", "A", "I"});

        round18.shuffleRandomLetters();
        rounds.offer(round18);

        //round 19
        Round round19 = new Round(R.drawable.fan1, R.drawable.fan2, R.drawable.fan3, R.drawable.crane2,
                "FAN", new String[]{"E", "A", "S", "C", "N", "F", "T", "W", "A", "I"});

        round19.shuffleRandomLetters();
        rounds.offer(round19);

        //round 20
        Round round20 = new Round(R.drawable.lion, R.drawable.mail, R.drawable.letter, R.drawable.fan1,
                "LETTER", new String[]{"E", "A", "R", "E", "L", "T", "T", "W", "A", "I"});

        round20.shuffleRandomLetters();
        rounds.offer(round20);

        //round 21
        Round round21 = new Round(R.drawable.lion, R.drawable.mail, R.drawable.letter, R.drawable.fan1,
                "STAMP", new String[]{"E", "A", "R", "E", "L", "S", "T", "M", "P", "I"});

        round21.shuffleRandomLetters();
        rounds.offer(round21);

    }

    public Round getNextRound() {
        return rounds.poll();
    }
}
