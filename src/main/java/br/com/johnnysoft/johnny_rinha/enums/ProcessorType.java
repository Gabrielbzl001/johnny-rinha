package br.com.johnnysoft.johnny_rinha.enums;

public enum ProcessorType {
    DEFAULT("default"),
    FALLBACK("fallback");

    private final String value;

    ProcessorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
