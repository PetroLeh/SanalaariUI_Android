package com.sanalaari;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sanalaari {
    
    private Parser parser;
    private ArrayList<WordData> wordData;
    private HashMap<String, WordData> wordMap;

    public Sanalaari(List<String> rawData) {
        parser = new Parser();
        init(rawData);
    }

    private void init(List<String> rawData) {
        wordData = parser.parse(rawData);
        wordMap = new HashMap<>();
        for (WordData w : wordData) {
            wordMap.put(w.plain(), w);
        }
    }

    public List<WordData> get(String letters, int length) {
        ArrayList<WordData> result = new ArrayList<>();
        ArrayList<String> partial = new ArrayList<>();
        char[] letterSet = letters.toCharArray();

        for (WordData w : wordData) {
            if (w.plain().length() == length)
                partial.add(w.plain());
        }

        for (String word : partial) {
            int matchedLetters = 0;
            boolean[] usedLetter = new boolean[letters.length()];
            
            for (char charInWord : word.toCharArray()) {
                boolean letterFound = false;
                for (int i = 0; i < letters.length(); i++) {
                    if (usedLetter[i])
                        continue;
                    if (letterSet[i] == charInWord) {
                        usedLetter[i] = true;
                        letterFound = true;
                        break;
                    }
                }
                if (!letterFound)
                    break;
                matchedLetters++;            
            }
            if (matchedLetters == length)
                result.add(wordMap.get(word));
        }
        return result;
    }

    public ArrayList<String> getPlain(String letters, int length) {
        ArrayList<String> result = new ArrayList<>();
        for (WordData wd : get(letters, length)) {
            String plain = wd.plain();
            if (!result.contains((plain)))
                result.add(plain);
        }
        return result;
    }

    public int size() {
        return this.wordData.size();
    }

}
