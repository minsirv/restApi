package org.minsirv.restApi.exceptions;

public class ProcessingNationalIdException extends RuntimeException {
    public ProcessingNationalIdException(String message) {
        super(message.isEmpty() ? "Failed to process national Id's!" : message);
    }
}
