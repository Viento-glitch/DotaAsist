package ru.sa.dotaassist.domain;

public class LogResponse {
    final int code;
    final String message;

    public LogResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
