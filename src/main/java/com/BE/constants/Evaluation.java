package com.BE.constants;

public enum Evaluation {
    AI,
    HUMAN;

    public static Evaluation fromText(String text) {
        return valueOf(text.toUpperCase());
    }
}
