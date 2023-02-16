package com.sanalaari;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    private Stack<String> tags;

    public Parser() {
        tags = new Stack<>();
    }

    public ArrayList<WordData> parse(List<String> wordListRaw) {
        ArrayList<WordData> data = new ArrayList<>();
        for (String row : wordListRaw) {
            try {
                WordData singleWordData = parseSingle(row);
                data.add(singleWordData);
            } catch (ParseException e) {
            }
        }
        return data;
    }

    private WordData parseSingle(String row) throws ParseException {
        ParseException e = checkExceptions(row);
        if (e != null) throw e;

        WordData wd = new WordData(getPlain(row));
        return wd;
    }

    private String getPlain(String row) {
        String tag = "";
        boolean tagOpen = false;

        String plain = "";
        boolean readPlain = false;

        for (char c : row.toCharArray()) {
            if (c == '<') {
                if (readPlain) break;
                tagOpen = true;
            } else if (c == '>') {
                if (tag.equals("s")) readPlain = true;
                tagOpen = false;
                tag = "";
            } else if (tagOpen) {
                tag += c;
            } else if (readPlain) {
                plain += c;
            }
        }
        return plain;
    }

    private ParseException checkExceptions(String row) {
        try {
            checkAngleBrackets(row);
            checkTags(row);
        } catch (ParseException e) {
            return e;
        }
        return null;
    }

    private void checkAngleBrackets(String row) throws ParseException {
        int n = 0;
        int index = 0;
        for (char c : row.toCharArray()) {
            if (c == '<') n++;
            if (c == '>') n--;
            if (n < 0 || n > 1) throw new ParseException("invalid angle bracket", index);
            index++;
        }
    }

    private void checkTags(String row) throws ParseException {
        boolean tagOpen = false;
        boolean readTag = false;
        String tag = "";
        int index = 0;

        for (char c : row.toCharArray()) {
            if (c == '<'){
                tagOpen = true;
                readTag = true;
            } else if (c == '>') {
                tagOpen = false;
                readTag = false;
                if (tag.length() == 0) throw new ParseException("empty tag", index);
                if (isClosing(tag)) {
                    if (tags.empty()) throw new ParseException("closing tag without opening", index);
                    tag = tag.substring(1);
                    String lastTag = tags.pop();
                    if (!tag.equals(lastTag)) throw new ParseException("invalid closing tag ( </" + lastTag + "> expected)", index);
                } else {
                    tags.push(tag);
                }
                tag = "";
            } else {
                if (c == ' ') readTag = false;
                if (tagOpen && readTag) tag += c;
            }
            index++;
        }
    }

    private boolean isClosing(String tag) {
        return tag.charAt(0) == '/';
    }
}
