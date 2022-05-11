package org.minsirv.restApi.exceptions;

public class ReadFileException extends RuntimeException {
    public ReadFileException(String message) {
        super(message.isEmpty() ? "Could not read file" : message);
    }
}
