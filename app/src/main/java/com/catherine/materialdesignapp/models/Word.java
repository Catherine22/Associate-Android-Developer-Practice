package com.catherine.materialdesignapp.models;

public class Word {
    public String word;
    public int frequency;
    public String shortcut;
    public String locale;

    public Word() {

    }

    public Word(String word, int frequency, String shortcut, String locale) {
        this.word = word;
        this.frequency = frequency;
        this.shortcut = shortcut;
        this.locale = locale;
    }
}
