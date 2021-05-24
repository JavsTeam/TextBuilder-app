package com.example.gen.src.TextBuilder.handlers;

import android.content.Context;

import com.example.textbuilder.service.ReadHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {

    //TODO: needs to be replaced with android analogue
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

    public static String readTxtInApp(int resourceId, Context context) {
        return ReadHandler.readFile(resourceId, context);
    }

    public static String readTxt(File txtFile) {
        return readTxt(txtFile.getPath());
    }
}
