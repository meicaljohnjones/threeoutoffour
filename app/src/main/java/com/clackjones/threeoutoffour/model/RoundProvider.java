package com.clackjones.threeoutoffour.model;

import java.util.Optional;

interface RoundProvider {
    Optional<Round> getNextRound(int lastRoundNumber);
}
