package ru.javawebinar.topjava.util.exception;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private final String url;
    private final HttpStatus status;
    private final String[] details;

    public ErrorInfo(CharSequence url, HttpStatus status, String[] details) {
        this.url = url.toString();
        this.status = status;
        this.details = details;
    }
}
