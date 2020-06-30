package br.com.aab.tokens.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IDReader {

  public static void main(String[] args) {
    IDReader reader = new IDReader();
    reader.getCPFs();
  }

  public List<String> getCPFs() {

    String fullPathFileName = "ids.txt";

    List<String> result = new ArrayList<>();
    try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fullPathFileName),
        StandardCharsets.UTF_8)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        result.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}