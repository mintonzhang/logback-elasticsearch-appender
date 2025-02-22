package com.internetitem.logback.elasticsearch.config;

public class Property {
    private String name;
    private String value;
    private boolean allowEmpty;
    private Type type = Type.STRING;

    public Property() {
    }

    public Property(String name, String value, boolean allowEmpty) {
        this.name = name;
        this.value = value;
        this.allowEmpty = allowEmpty;
    }

    public Property(String name, String value, Type type, boolean allowEmpty) {
        this.name = name;
        this.value = value;
        this.allowEmpty = allowEmpty;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type) {
        try {
            this.type = Enum.valueOf(Type.class, type.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.type = Type.STRING;
        }
    }

    public enum Type {
        STRING, INT, FLOAT, BOOLEAN
    }
}
