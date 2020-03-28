package com.clackjones.threeoutoffour.model;

import com.clackjones.threeoutoffour.R;

import java.util.HashMap;
import java.util.Map;

public class RoundProvider {
    private static RoundProvider roundProvider = null;
    private Map<Integer, Round> rounds = new HashMap<>();

    private RoundProvider() {

    }

    public static RoundProvider getInstance() {
        if (RoundProvider.roundProvider == null) {
            RoundProvider.roundProvider = new RoundProvider();
            RoundProvider.roundProvider.initialize();
        }
        return RoundProvider.roundProvider;
    }

    private void addRound(int roundNumber, int topLeftImage, int topRightImage, int bottomLeftImage, int bottomRightImage,
                     String answer, String[] randomLetters) {
        Round round = new Round(roundNumber,  topLeftImage, topRightImage, bottomLeftImage,
                bottomRightImage, answer, randomLetters);

        rounds.put(roundNumber, round);
    }

    private void initialize() {
        //round 1 - medium
        addRound(1, R.drawable.rain, R.drawable.cat, R.drawable.dog, R.drawable.water,
                "WET", new String[]{"E", "T", "W", "A", "Y", "A", "T", "B", "E", "A"});

        //round 2 - medium
        addRound(2, R.drawable.box, R.drawable.vase, R.drawable.giftbox, R.drawable.package1,
                "BOX", new String[]{"B", "T", "O", "X", "Y", "A", "T", "B", "E", "A"});

        //round 3 - easy
        addRound(3, R.drawable.fly, R.drawable.bird, R.drawable.plane, R.drawable.frog,
                "FLY", new String[]{"D", "F", "L", "H", "Y", "A", "T", "B", "A", "T"});


        //round 4 - easy
        addRound(4, R.drawable.wave, R.drawable.sound, R.drawable.plane, R.drawable.hello,
                "WAVE", new String[]{"W", "E", "A", "V", "Y", "A", "T", "B", "E", "A"});


        //round 5 - easy
        addRound(5, R.drawable.bowl, R.drawable.toilet, R.drawable.bowling, R.drawable.bird,
                "BOWL", new String[]{"W", "O", "L", "V", "Y", "A", "T", "B", "E", "A"});

        //round 6 - easy
        addRound(6, R.drawable.cry, R.drawable.torn, R.drawable.cry2, R.drawable.bowl,
                "TEAR", new String[]{"W", "E", "A", "V", "R", "A", "T", "E", "A", "I"});

        //round 7
        addRound(7, R.drawable.tie, R.drawable.tying, R.drawable.knot, R.drawable.bowl,
                "TIE", new String[]{"W", "E", "A", "V", "R", "A", "T", "I", "E", "A"});

        //round 8 - easy
        addRound(8, R.drawable.stop, R.drawable.sign, R.drawable.sign2, R.drawable.tie,
                "SIGN", new String[]{"W", "E", "S", "N", "I", "G", "T", "I", "E", "A"});

        //round 9 - medium
        addRound(9, R.drawable.catgroom, R.drawable.groom, R.drawable.monkeys, R.drawable.bird,
                "GROOM", new String[]{"W", "O", "S", "M", "R", "G", "O", "I", "E", "A"});

        //round 10
        addRound(10, R.drawable.bow, R.drawable.horse, R.drawable.archer, R.drawable.groom,
                "BOW", new String[]{"W", "O", "S", "M", "R", "G", "B", "I", "E", "A"});

        //round 11
        addRound(11, R.drawable.film, R.drawable.bread, R.drawable.toiletpaper, R.drawable.knot,
                "ROLL", new String[]{"W", "O", "S", "M", "R", "L", "L", "I", "E", "A"});

        //round 12 - medium
        addRound(12, R.drawable.rings, R.drawable.planet, R.drawable.tree, R.drawable.film,
                "RINGS", new String[]{"E", "S", "G", "N", "I", "R", "T", "E", "A", "I"});

        //round 13 - medium
        addRound(13, R.drawable.gold, R.drawable.soap, R.drawable.chocolate, R.drawable.giftbox,
                "BARS", new String[]{"E", "A", "S", "N", "I", "R", "T", "B", "A", "I"});

        //round 14 - medium
        addRound(14, R.drawable.fire, R.drawable.soccer, R.drawable.tennis, R.drawable.gold,
                "MATCH", new String[]{"E", "A", "S", "C", "H", "R", "T", "M", "A", "I"});

        //round 15 - easy
        addRound(15, R.drawable.watch, R.drawable.pocketwatch, R.drawable.binoculars, R.drawable.fire,
                "WATCH", new String[]{"E", "A", "S", "C", "H", "R", "T", "W", "A", "I"});

        //round 16 - easy
        addRound(16, R.drawable.bark1, R.drawable.dogbark, R.drawable.bark2, R.drawable.leaf,
                "BARK", new String[]{"E", "A", "S", "C", "H", "R", "B", "W", "A", "K"});

        //round 17
        addRound(17, R.drawable.bass1, R.drawable.bass2, R.drawable.bass, R.drawable.tea,
                "BASS", new String[]{"E", "A", "S", "B", "H", "R", "T", "S", "A", "I"});

        //round 18
        addRound(18, R.drawable.cranebird1, R.drawable.crane2, R.drawable.cranebird2, R.drawable.firetruck,
                "CRANE", new String[]{"E", "A", "S", "C", "N", "R", "T", "W", "A", "I"});

        //round 19
        addRound(19, R.drawable.fan1, R.drawable.fan2, R.drawable.fan3, R.drawable.crane2,
                "FAN", new String[]{"E", "A", "S", "C", "N", "F", "T", "W", "A", "I"});

        //round 20
        addRound(20, R.drawable.lion, R.drawable.mail, R.drawable.letter, R.drawable.fan1,
                "LETTER", new String[]{"E", "A", "R", "E", "L", "T", "T", "W", "A", "I"});

        //round 21
        addRound(21, R.drawable.lion, R.drawable.mail, R.drawable.letter, R.drawable.fan1,
                "STAMP", new String[]{"E", "A", "R", "E", "L", "S", "T", "M", "P", "I"});
    }

    public Round getNextRound(int lastRoundNumber) {
        int nextRoundNumber = lastRoundNumber + 1;
        return rounds.get(nextRoundNumber);
    }
}
