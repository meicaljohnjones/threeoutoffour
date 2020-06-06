package com.clackjones.threeoutoffour.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpSerializedRoundResourceGetter implements SerializedRoundResourceGetter {
    static final String HTTP_ROUND_RESOURCE = "http://threeoutoffour.clackjones.com/";
    static final String ROUNDS_INDEX_FILENAME = "rounds_index.txt";

    @Override
    public InputStream getResourceContainingRound(int roundNumber) throws IOException {
        List<String> indexFilenames =  retrieveIndexFileNames();
        List<IntegerInterval> roundFileIntervals = filenamesToIntervals(indexFilenames);

        int indexOfCorrectFile = 0;
        for (int i = 0; i < roundFileIntervals.size(); ++i) {
            IntegerInterval currInterval = roundFileIntervals.get(indexOfCorrectFile);
            if (currInterval.valInInterval(roundNumber)) {
                indexOfCorrectFile = i;
                break;
            }
        }

        String correctFileName = indexFilenames.get(indexOfCorrectFile);
        return retrieveInputStreamForResource(correctFileName);
    }

    private List<IntegerInterval> filenamesToIntervals(List<String> indexFilenames) {
        List<IntegerInterval> intervals = new ArrayList<>(indexFilenames.size());

        for (String filename: indexFilenames) {
            String[] components = filename.split("_|\\.");
            intervals.add(
                    new IntegerInterval(
                            Integer.valueOf(components[1]),
                            Integer.valueOf(components[2])
                    )
            );
        }
        return intervals;
    }

    private InputStream retrieveInputStreamForResource(String filename) throws IOException {
        URL url = new URL(HTTP_ROUND_RESOURCE + filename);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        return conn.getInputStream();
    }

    private List<String> retrieveIndexFileNames() throws IOException {
        List<String> filenames = new ArrayList<>();

        URL url = new URL(HTTP_ROUND_RESOURCE + ROUNDS_INDEX_FILENAME);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = reader.readLine()) != null) {
                filenames.add(str);
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return filenames;
    }

    class IntegerInterval {
        Integer lowerInclusive;
        Integer upperInclusive;

        public IntegerInterval(Integer lowerInclusive, Integer upperInclusive) {
            this.lowerInclusive = lowerInclusive;
            this.upperInclusive = upperInclusive;
        }

        public boolean valInInterval(Integer i) {
            return i != null && i <= upperInclusive && i >= lowerInclusive;
        }

        public String toString() {
            return String.format("Interval from %03d to %03d",
                    this.lowerInclusive, this.upperInclusive);
        }
    }

    // For testing
    public static void main(String args[]) {
        SerializedRoundResourceGetter getter = new HttpSerializedRoundResourceGetter();
        try {
            getter.getResourceContainingRound(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
