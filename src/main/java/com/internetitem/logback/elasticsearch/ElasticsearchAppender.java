package com.internetitem.logback.elasticsearch;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.util.ParamsMapSupply;

import java.io.IOException;

public class ElasticsearchAppender extends AbstractElasticsearchAppender<ILoggingEvent> {

    public ElasticsearchAppender() {
    }

    public ElasticsearchAppender(Settings settings, HttpRequestHeaders headers, ParamsMapSupply supplier) {
        super(settings, headers, supplier);
    }

    @Override
    protected void appendInternal(ILoggingEvent eventObject) {

        String targetLogger = eventObject.getLoggerName();

        String loggerName = settings.getLoggerName();
        if (loggerName != null && loggerName.equals(targetLogger)) {
            return;
        }

        String errorLoggerName = settings.getErrorLoggerName();
        if (errorLoggerName != null && errorLoggerName.equals(targetLogger)) {
            return;
        }

        eventObject.prepareForDeferredProcessing();
        if (settings.isIncludeCallerData()) {
            eventObject.getCallerData();
        }

        publishEvent(eventObject);
    }

    protected ClassicElasticsearchPublisher buildElasticsearchPublisher() throws IOException {
        return new ClassicElasticsearchPublisher(
                super.supplier,
                super.getContext(),
                super.errorReporter,
                super.settings,
                super.elasticsearchProperties,
                super.headers
        );
    }


}
