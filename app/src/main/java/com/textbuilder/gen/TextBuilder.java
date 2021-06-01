package com.textbuilder.gen;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TextBuilder {
    private final int depth;

    private final HashMap<String, Word> words = new HashMap<>();

    public TextBuilder(int depth, Context context, String text) {
        this.depth = depth;
        parseWordsFromText(depth, text);
    }

    public String getText(int minLength) {
        StringBuilder text = new StringBuilder();
        String word = getFirstWord();
        String endWord;
        minLength /= depth;
        int counter = 0;

        while (true) {
            if (counter++ > minLength && (endWord = isEnding(word)) != null) {
                text.append(endWord);
                break;
            }
            text.append(word).append(" ");
            String nextWord = words.get(word).getNextWord();
            if (nextWord != null) {
                word = findWord(nextWord);
            } else {
                word = getFirstWord();
            }
        }
        return text.toString();
    }

    private static final String[] endMarks = {".", "?", "!", "...", ")", "@", "\n"};

    private String isEnding(String word) {
        if (word.length() > 1) {
            for (String mark : endMarks) {
                if (word.contains(mark)) {
                    return word.substring(0, word.indexOf(mark) + 1);
                }
            }
        }
        return null;
    }

    public void printText(int minLength) {
        minLength /= depth;
        System.out.println(getText(minLength));
    }

    private String getFirstWord() {
        ArrayList<String> capital = new ArrayList<>();
        for (Map.Entry<String, Word> word : words.entrySet()) {
            if (word.getKey().length() > 0 &&
                    word.getKey().charAt(0) > 'A' &&
                    word.getKey().charAt(0) < 'Я') {
                capital.add(word.getKey());
            }
        }
        return capital.get(new Random().nextInt(capital.size()));
    }


    // TODO: Better handling of empty words
    private void parseWordsFromText(int depth, String text) {
        String[] textWords = text.split(" ");

        StringBuilder word = new StringBuilder(textWords[0]);

        addWord(word.toString());

        for (int i = 0; i < textWords.length - depth; i++) {
            if (textWords[i].equals("") || textWords[i].equals(" ")) {
                continue;
            }
            StringBuilder currentWord = new StringBuilder(textWords[i]);

            for (int j = 1; j < depth; j++) {
                currentWord.append(" ").append(textWords[++i]);
            }
            updateWords(word.toString(), currentWord.toString());
            word = currentWord;
        }
    }

    private void updateWords(String previous, String current) {
        words.get(findWord(previous)).addNextWord(current);
        addWord(current);
    }

    // returns link to word in the list
    private String findWord(String word) {
        if (!words.containsKey(word)) {
            // word not found
            Word newWord = new Word();
            words.put(word, newWord);
        }
        return word;
    }

    private void addWord(String word) {
        if (!words.containsKey(word)) {
            // word not found
            Word newWord = new Word();
            words.put(word, newWord);
        }
    }

    @Override
    public String toString() {
        return "TextGenerator{" +
                "words=" + words.toString() +
                '}';
    }

    private static class Word {

        private final HashMap<String, Integer> nextWords = new HashMap<>();

        private Word() {
        }

        public String toString() {
            return "next words=" + nextWords.toString() + '}';
        }

        private void addNextWord(String word) {
            if (nextWords.containsKey(word)) {
                Integer i = nextWords.get(word);
                nextWords.put(word, i + 1);
            }
            // word not found
            nextWords.put(word, 1);
        }


        // TODO: Better exception handling
        private String getNextWord() {
            try {
                int total = 0;
                // counting total weight
                if (!nextWords.isEmpty()) {
                    for (Map.Entry<String, Integer> nextWord : nextWords.entrySet()) {
                        total += nextWord.getValue();
                    }
                } else throw new Exception("NO NEXT WORD");
                // probability distribution depends on frequency of word occurrence
                int result = new Random().nextInt(total) + 1;
                // getting randomly chosen word
                for (Map.Entry<String, Integer> nextWord : nextWords.entrySet()) {
                    result -= nextWord.getValue();
                    if (result <= 0) {
                        return nextWord.getKey();
                    }
                }
                // only if something goes wrong
                throw new Exception("NO NEXT WORD");
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return null;
        }
    }
}