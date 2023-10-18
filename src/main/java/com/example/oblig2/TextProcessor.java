package com.example.oblig2;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor {
    private final Map<String, Map<String, Integer>> data = new HashMap<>();
    private final Pattern pattern = Pattern.compile("\\b\\w+\\b|[,;:\\.]", Pattern.UNICODE_CHARACTER_CLASS);

    public void processText(String text) {
        data.clear();

        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);

        text = new String(utf8Bytes, StandardCharsets.UTF_8);

        Matcher matcher = pattern.matcher(text);

        ArrayList<String> wordsAndPunctuation = new ArrayList<>();

        while (matcher.find()) {
            String wordOrPunctuation = matcher.group();
            wordsAndPunctuation.add(wordOrPunctuation);
        }


       String[] words = wordsAndPunctuation.toArray(new String[0]);
       for (int i = 0; i < words.length - 2; i++) {
            String key = words[i] + "+" + words[i + 1];
            String value = words[i + 2];
            data.putIfAbsent(key, new HashMap<>());
            data.get(key).put(value, data.get(key).getOrDefault(value, 0) + 1);
        }
        printDataMap();
    }

    public void printDataMap() {
        data.forEach((key, valueMap) -> {
            System.out.println("Key: " + key);
            valueMap.forEach((value, count) -> {
                System.out.println("  " + value + ": " + count);
            });
        });
    }

    public String generateText(String startWords, int wordCount) {
        StringBuilder result = new StringBuilder(startWords.replace("+", " "));
        Random random = new Random();

        for (int i = 0; i < wordCount; i++) {
            Map<String, Integer> nextWords = data.get(startWords);
            if (nextWords == null || nextWords.isEmpty()) return "Ingen funnet. Sjekk om du har lastet inn en gyldig fil.";

            int total = 0;
            for (Integer value : nextWords.values()) {
                total += value;
            }
            int randValue = random.nextInt(total);

            String selectedWord = "";
            for (Map.Entry<String, Integer> entry : nextWords.entrySet()) {
                randValue -= entry.getValue();
                if (randValue < 0) {
                    selectedWord = entry.getKey();
                    break;
                }
            }

            result.append(" ").append(selectedWord);

            startWords = startWords.split("\\+")[1] + "+" + selectedWord;
            System.out.println(startWords);
        }

        return result.toString();
    }
}
