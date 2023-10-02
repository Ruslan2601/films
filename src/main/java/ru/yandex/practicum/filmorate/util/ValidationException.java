package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    int code;

    public ValidationException(String message) {
        super(message);
        log.error(message);
    }

    public ValidationException(String message, int i) {
        super(message);
        code = i;
        log.error(message);
    }

    public int getCode() {
        return code;
    }
}
