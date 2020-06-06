package com.clackjones.threeoutoffour.model;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InternetGettingRoundProvider implements RoundProvider {
    private static InternetGettingRoundProvider roundProvider;
    private final RoundsDeserializer roundsDeserializer;
    private final SerializedRoundResourceGetter serializedRoundResourceGetter;
    private Context context;
    private Map<Integer, Round> rounds;
    private ExecutorService executorService;



    public InternetGettingRoundProvider(Context context,
                                        SerializedRoundResourceGetter serializedRoundResourceGetter,
                                        RoundsDeserializer roundsDeserializer) {
        this.context = context;
        this.serializedRoundResourceGetter = serializedRoundResourceGetter;
        this.roundsDeserializer = roundsDeserializer;
        this.rounds = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static InternetGettingRoundProvider getInstance(Context context) {
        if (InternetGettingRoundProvider.roundProvider == null) {
            InternetGettingRoundProvider.roundProvider =
                    new InternetGettingRoundProvider(
                            context,
                            new HttpSerializedRoundResourceGetter(),
                            new RoundsDeserializer()
                    );
            InternetGettingRoundProvider.roundProvider.initialize();
        }
        return InternetGettingRoundProvider.roundProvider;
    }

    private void initialize() {
    }

    @Override
    public Round getNextRound(int lastRoundNumber) {
        int nextRoundIdx = lastRoundNumber + 1;

        // TODO: if round not available,
        //      check if rounds serialized to file (from below)

        boolean isNextRoundNumberAvailable = this.rounds.containsKey(nextRoundIdx);
        if (!isNextRoundNumberAvailable) {
            // TODO: serialize below to file
            // TODO: if internet disabled will fail
                // Consider Executor fail after so many seconds and tell user to come back later or enable wifi
            loadNextRoundsFromResource(nextRoundIdx);
        }


        boolean isNextRoundNumberAvailableAfterAttemptLoad = this.rounds.containsKey(nextRoundIdx);
        if (!isNextRoundNumberAvailableAfterAttemptLoad) {
            return null; // TODO: consider return Optional.None to indicate no more rounds
        }

        return rounds.get(nextRoundIdx);
    }

    void loadNextRoundsFromResource(final Integer nextRoundIdx) {
        Callable<Map<Integer, Round>> callableLoadRounds = new Callable<Map<Integer, Round>>() {

            @Override
            public Map<Integer, Round> call() throws Exception {
                return loadNextRoundsFromResourceImpl(nextRoundIdx);
            }
        };

        Future<Map<Integer, Round>> futureRounds = this.executorService.submit(callableLoadRounds);
        while(!futureRounds.isDone()) {}

        try {
            this.rounds = futureRounds.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    Map<Integer, Round> loadNextRoundsFromResourceImpl(Integer nextRoundIdx) {
        try {
            InputStream roundsInputStream = null;
            try {
                roundsInputStream = serializedRoundResourceGetter.getResourceContainingRound(nextRoundIdx);
                return roundsDeserializer.readRounds(roundsInputStream);
            } finally {
                if (roundsInputStream != null) {
                    roundsInputStream.close();
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }
}
