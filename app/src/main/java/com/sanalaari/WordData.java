package com.sanalaari;

public class WordData {

    private String plain;

    public WordData() {
    }

    public WordData(String word) {
        this.plain = word;
    }

    public String plain() {
        return this.plain;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WordData))
            return false;

        WordData o = (WordData) other;
        return this.plain.equalsIgnoreCase(o.plain);
    }
    
}
