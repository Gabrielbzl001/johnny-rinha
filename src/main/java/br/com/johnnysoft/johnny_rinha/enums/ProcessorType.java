package br.com.johnnysoft.johnny_rinha.enums;

public record ProcessorType(String value) {
    public static final ProcessorType DEFAULT = new ProcessorType("default");
    public static final ProcessorType FALLBACK = new ProcessorType("fallback");
}
