package com.BE.constants;

public enum InterviewStatus {
    ON_GOING,
    COMPLETED;

    public static InterviewStatus fromString(String status) {
        switch (status) {
            case "ON_GOING":
                return ON_GOING;
            case "COMPLETED":
                return COMPLETED;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
