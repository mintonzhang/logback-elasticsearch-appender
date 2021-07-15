package com.internetitem.logback.elasticsearch;

import ch.qos.logback.access.spi.IAccessEvent;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.util.ParamsMapSupply;

import java.io.IOException;

public class ElasticsearchAccessAppender extends AbstractElasticsearchAppender<IAccessEvent> {


    public ElasticsearchAccessAppender(Settings settings, HttpRequestHeaders headers, ParamsMapSupply supply) {
        super(settings, headers, supply);
    }

    public ElasticsearchAccessAppender() {
    }

    @Override
    protected void appendInternal(IAccessEvent eventObject) {
        eventObject.prepareForDeferredProcessing();
        publishEvent(eventObject);
    }

    protected AccessElasticsearchPublisher buildElasticsearchPublisher() throws IOException {
        return new AccessElasticsearchPublisher(super.supplier, getContext(), errorReporter, settings, elasticsearchProperties, headers);
    }


}
