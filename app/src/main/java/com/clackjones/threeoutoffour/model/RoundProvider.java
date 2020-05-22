package com.clackjones.threeoutoffour.model;

interface RoundProvider {
    Round getNextRound(int lastRoundNumber);
}
