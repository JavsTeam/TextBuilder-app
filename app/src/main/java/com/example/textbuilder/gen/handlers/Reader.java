package com.example.textbuilder.gen.handlers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Reader {
    public static String readTxt(String pathToTxt) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader buf = new BufferedReader(new FileReader(pathToTxt))) {
            String line;
            while ((line = buf.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) {
                    text.append(word).append(" ");
                }
                text.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static String readTxtFromRes(int resourceId, Context context) {
        Scanner scanner;
        StringBuilder text = new StringBuilder();
        try {
            InputStream in = context.getResources().openRawResource(resourceId);
            scanner = new Scanner(in);
            while (scanner.hasNext()) {
                text.append(scanner.nextLine()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static String readTxt(File txtFile) {
        return readTxt(txtFile.getPath());
    }
}





