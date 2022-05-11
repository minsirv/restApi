package org.minsirv.restApi.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidNationalIdException extends RuntimeException {
    public InvalidNationalIdException(List<String> errors) {
        super(errors.stream().collect(Collectors.joining("; ")));
    }
}
