package com.mz.twitterpuller.data.source.remote;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Json {

  public static String readJsonFromFile(Context context, String filePath) {
    BufferedReader reader = null;
    String line, result = "";
    try {
      InputStream open = context.getAssets().open(filePath);
      reader = new BufferedReader(new BufferedReader(new InputStreamReader(open)));
      while ((line = reader.readLine()) != null) {
        result += line;
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
