package com.example.textbuilder.service;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.textbuilder.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadHandler {
    Context context;

    ReadHandler() {}

    ReadHandler(Context context) {
        this.context = context;
    }

    public static String readFile(int resourceId, Context context) {
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
}
