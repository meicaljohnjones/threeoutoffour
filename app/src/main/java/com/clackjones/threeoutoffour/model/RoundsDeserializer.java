package com.clackjones.threeoutoffour.model;

import android.util.Base64;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RoundsDeserializer {
    public List<Round> readRounds(InputStream inputStream) {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));

        List<Round> rounds = new ArrayList<>(20);

        try {
            jsonReader.beginArray();
            while(jsonReader.hasNext()) {
                rounds.add(readRound(jsonReader));
            }
            jsonReader.endArray();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return rounds;
    }

    private Round readRound(JsonReader reader) throws IOException {
        Integer roundNumber = null;
        String answer = null;
        String[] randomLetters = null;
        byte[] topLeftImage = null;
        byte[] topRightImage = null;
        byte[] bottomLeftImage = null;
        byte[] bottomRightImage = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("roundNumber")) {
                roundNumber = reader.nextInt();
            } else if (name.equals("answer")) {
                answer = reader.nextString();
            } else if (name.equals("randomLetters")) {
                randomLetters = readRandomLetters(reader);
            } else if (name.equals("topLeftImage")) {
                topLeftImage = Base64.decode(reader.nextString(), Base64.DEFAULT);
            } else if (name.equals("topRightImage")) {
                topRightImage = Base64.decode(reader.nextString(), Base64.DEFAULT);
            } else if (name.equals("bottomLeftImage")) {
                bottomLeftImage = Base64.decode(reader.nextString(), Base64.DEFAULT);
            } else if (name.equals("bottomRightImage")) {
                bottomRightImage = Base64.decode(reader.nextString(), Base64.DEFAULT);
            } else {
                reader.skipValue();
            }
            // todo add other attributes
        }
        reader.endObject();

        return new Round(roundNumber,  topLeftImage, topRightImage, bottomLeftImage,
                bottomRightImage, answer, randomLetters);
    }

    private String[] readRandomLetters(JsonReader reader) throws IOException {
        List<String> randomLetters = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            randomLetters.add(reader.nextString());
        }
        reader.endArray();

        String[] randLetters = new String[randomLetters.size()];
        return randomLetters.toArray(randLetters);
    }
}
