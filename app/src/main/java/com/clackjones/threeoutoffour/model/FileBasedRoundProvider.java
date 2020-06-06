package com.clackjones.threeoutoffour.model;

import android.content.Context;
import com.clackjones.threeoutoffour.R;

import java.io.InputStream;
import java.util.Map;

public class FileBasedRoundProvider implements RoundProvider {
    private static FileBasedRoundProvider roundProvider;
    private final RoundsDeserializer roundsDeserializer;
    private Context context;
    private Map<Integer, Round> rounds;


    public FileBasedRoundProvider(Context context, RoundsDeserializer roundsDeserializer) {
        this.context = context;
        this.roundsDeserializer = roundsDeserializer;
    }

    public static FileBasedRoundProvider getInstance(Context context) {
        if (FileBasedRoundProvider.roundProvider == null) {
            FileBasedRoundProvider.roundProvider = new FileBasedRoundProvider(context, new RoundsDeserializer());
            FileBasedRoundProvider.roundProvider.initialize();
        }
        return FileBasedRoundProvider.roundProvider;
    }

    private void initialize() {
        InputStream jsonInputStream = context.getResources().openRawResource(R.raw.rounds);
        this.rounds = this.roundsDeserializer.readRounds(jsonInputStream);
    }

    @Override
    public Round getNextRound(int lastRoundNumber) {
        int nextRoundIdx = lastRoundNumber + 1;
        return this.rounds.get(nextRoundIdx);
    }
}
