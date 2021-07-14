package com.internetitem.logback.elasticsearch.config;

/**
 * A key value pair for the http header.
 */
public class HttpRequestHeader {

    private String name;
    private String value;

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

    public HttpRequestHeader(String name, String value) {

        this.name = name;
        this.value = value;
    }

    public HttpRequestHeader() {
    }
}
