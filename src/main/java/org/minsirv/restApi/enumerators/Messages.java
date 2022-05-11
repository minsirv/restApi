package org.minsirv.restApi.enumerators;

public enum Messages {
    ID_VALID("National Id is valid!"),
    SOME_INVALID("some invalid"),
    ALL_INVALID("all invalid"),
    ALL_VALID("all valid");

    private final String value;

    Messages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
