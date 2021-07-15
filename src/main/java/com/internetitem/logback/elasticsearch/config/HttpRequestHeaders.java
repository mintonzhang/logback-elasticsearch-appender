package com.internetitem.logback.elasticsearch.config;

import java.util.LinkedList;
import java.util.List;

/**
 * A container for the headers which will be sent to elasticsearch.
 */
public class HttpRequestHeaders implements Cloneable {

    private List<HttpRequestHeader> headers = new LinkedList<HttpRequestHeader>();

    public List<HttpRequestHeader> getHeaders() {
        return headers;
    }

    public HttpRequestHeaders addHeader(HttpRequestHeader header) {
        this.headers.add(header);
        return this;
    }

    public HttpRequestHeaders addHeader(String key, String value) {
        this.headers.add(new HttpRequestHeader(key, value));
        return this;
    }

    @Override
    public HttpRequestHeaders clone() {

        HttpRequestHeaders httpRequestHeaders = new HttpRequestHeaders();

        for (HttpRequestHeader header : headers) {
            httpRequestHeaders.addHeader(header.getName(), header.getValue());
        }
        return httpRequestHeaders;

    }
}
