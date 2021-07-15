package com.internetitem.logback.elasticsearch;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.internetitem.logback.elasticsearch.config.ElasticsearchProperties;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.util.ErrorReporter;
import com.internetitem.logback.elasticsearch.util.ParamsMapSupply;

import java.io.IOException;
import java.util.Collections;

public abstract class AbstractElasticsearchAppender<T> extends UnsynchronizedAppenderBase<T> {

    protected Settings settings;
    protected ElasticsearchProperties elasticsearchProperties;
    protected AbstractElasticsearchPublisher<T> publisher;
    protected ErrorReporter errorReporter;
    protected HttpRequestHeaders headers;
    protected ParamsMapSupply supplier;


    public AbstractElasticsearchAppender(Settings settings, HttpRequestHeaders headers, ParamsMapSupply supplier) {
        this.settings = settings;
        this.headers = headers;
        this.supplier = supplier;
    }

    public AbstractElasticsearchAppender() {
        this.settings = new Settings();
        this.headers = new HttpRequestHeaders();
        this.supplier = Collections::emptyMap;
    }

    @Override
    public void start() {
        super.start();
        this.errorReporter = getErrorReporter();
        try {
            this.publisher = buildElasticsearchPublisher();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void publishEvent(T eventObject) {
        publisher.addEvent(eventObject);
    }

    //VisibleForTesting
    protected ErrorReporter getErrorReporter() {
        return new ErrorReporter(settings, getContext());
    }

    //VisibleForTesting
    protected abstract AbstractElasticsearchPublisher<T> buildElasticsearchPublisher() throws IOException;

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(T eventObject) {
        appendInternal(eventObject);
    }

    protected abstract void appendInternal(T eventObject);

    public void setProperties(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }
}
