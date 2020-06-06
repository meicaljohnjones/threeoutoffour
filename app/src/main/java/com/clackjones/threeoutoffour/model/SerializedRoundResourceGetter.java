package com.clackjones.threeoutoffour.model;

import java.io.IOException;
import java.io.InputStream;

interface SerializedRoundResourceGetter {
    InputStream getResourceContainingRound(int roundNumber) throws IOException;
}
