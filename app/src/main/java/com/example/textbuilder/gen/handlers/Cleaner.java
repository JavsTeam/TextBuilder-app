package com.example.textbuilder.gen.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Cleaner {
    public static final String[] defaultArray = {" ", "\n", "http", "#", ":", "[", "]"};

    public static void deleteFromFileIfContains(String[] forbidden, String pathToTxt) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader buf = new BufferedReader(new FileReader(pathToTxt))) {
            String line;
            while ((line = buf.readLine()) != null) {
                text.append(line).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = deleteFromTextIfContains(forbidden, text.toString());
        Writer.writeTextTo(result, pathToTxt);
    }

    public static String deleteFromTextIfContains(String[] forbidden, String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split(" ");

        outer:
        for (String word : words) {
            for (String string : forbidden) {
                if (word.contains(string) || word.isEmpty()) {
                    continue outer;
                }
            }
            result.append(word).append(" ");
        }

        return result.toString();
    }

    public static void placeEndl(String pathToTxt) {
        String[] text = Reader.readTxt(pathToTxt).split(" ");
        StringBuilder result = new StringBuilder();

        for(String s : text) {
            if(s.equals("@")) {
                result.append("\n").append("@").append("\n");
            } else {
                result.append(s).append(" ");
            }
        }

        Writer.writeTextTo(result.toString(), pathToTxt);
    }

    public static void deleteEmptyLines(File txt) {
        String[] text = Reader.readTxt(txt).split("\n");
        StringBuilder result = new StringBuilder();

        for(String s : text) {
            if(!s.equals(" ")) {
                result.append(s).append("\n");
            }
        }

        Writer.writeTextTo(result.toString(), txt);
    }

    public static void deleteFromFileIfContains(String pathToTxt) {
        deleteFromFileIfContains(defaultArray, pathToTxt);
    }

    public static void deleteFromFileIfContains(File txt) {
        deleteFromFileIfContains(defaultArray, txt);
    }

    public static void deleteFromFileIfContains(String[] forbidden, File txt) {
        deleteFromFileIfContains(forbidden, txt.getPath());
    }
}

