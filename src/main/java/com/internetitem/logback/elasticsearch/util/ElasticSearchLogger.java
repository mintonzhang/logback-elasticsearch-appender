package com.internetitem.logback.elasticsearch.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.internetitem.logback.elasticsearch.ElasticsearchAppender;
import com.internetitem.logback.elasticsearch.config.ElasticsearchProperties;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Property;
import com.internetitem.logback.elasticsearch.config.Settings;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author minton.zhang
 * @since 2021/7/15 10:12
 */
public class ElasticSearchLogger {

    public static final Set<Property> DEFAULT_COMMON_PROPERTY = new HashSet<>(10);
    private static final LoggerContext LOGGER_CONTEXT = new LoggerContext();
    public static String INDEX_FORMAT = "logs-%date{yyyy-MM-dd}";
    public static String SYSTEM_TYPE = "default";
    public static HttpRequestHeaders HEADERS = new HttpRequestHeaders().addHeader("Content-Type", "application/json");
    public static Settings SETTINGS;
    public static ParamsMapSupply DEFAULT_PARAMS_MAP_SUPPLY = Collections::emptyMap;


    static {
        Property p1 = new Property("level", "%level", Property.Type.STRING, false);
        Property p2 = new Property("logger", "%logger", Property.Type.STRING, false);
        Property p3 = new Property("thread", "%thread", Property.Type.STRING, false);
        Property p4 = new Property("stacktrace", "%ex", Property.Type.STRING, true);
        Property p5 = new Property("timestamp", String.valueOf(System.currentTimeMillis()), Property.Type.INT, false);

        DEFAULT_COMMON_PROPERTY.add(p1);
        DEFAULT_COMMON_PROPERTY.add(p2);
        DEFAULT_COMMON_PROPERTY.add(p3);
        DEFAULT_COMMON_PROPERTY.add(p4);
        DEFAULT_COMMON_PROPERTY.add(p5);

        SETTINGS = new Settings();

        SETTINGS.setType("_doc");
        SETTINGS.setLoggerName("ELASTIC_SEARCH_LOGGER");
        SETTINGS.setErrorLoggerName("ELASTIC_SEARCH_ERROR_LOGGER");
        SETTINGS.setMaxMessageSize(100);
    }


    public static void setElasticSearchConnUrl(String ip, int port, String systemType) {
        setElasticSearchConnUrl(String.format("http://%s:%d/_bulk", ip, port), systemType);
    }

    public static void setElasticSearchConnUrl(String url, String systemType) {
        try {
            SETTINGS.setUrl(new URL(url.endsWith("_bulk") ? url : url.concat("_bulk")));
            SYSTEM_TYPE = systemType;
            Property p6 = new Property("systemType", SYSTEM_TYPE, Property.Type.STRING, false);
            DEFAULT_COMMON_PROPERTY.add(p6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ElasticsearchAppender initAppender(String indexName, ParamsMapSupply paramsMapSupply) {
        try {
            Settings newSetting = SETTINGS.clone();
            newSetting.setIndex(SYSTEM_TYPE.toLowerCase() + "-" + indexName);
            HttpRequestHeaders clone = HEADERS.clone();
            //日志追加配置
            ElasticsearchAppender appender = new ElasticsearchAppender(newSetting, clone, paramsMapSupply);
            appender.setContext(LOGGER_CONTEXT);
            appender.setName(SYSTEM_TYPE);
            ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();
            elasticsearchProperties.getProperties().addAll(DEFAULT_COMMON_PROPERTY);
            appender.setProperties(elasticsearchProperties);
            appender.start();
            return appender;
        } catch (Exception e) {
            throw new RuntimeException("无法初始化elastic log appender");
        }
    }


    public static Logger getLogger(String logName, String indexName, ParamsMapSupply supply) {
        Logger log = LOGGER_CONTEXT.getLogger(logName);
        log.setAdditive(false);
        log.setLevel(Level.ALL);
        log.addAppender(initAppender(indexName, supply));
        return log;
    }

    public static Logger getLogger(String indexName, ParamsMapSupply supply) {
        return getLogger(SYSTEM_TYPE, indexName, supply);
    }

    public static Logger getLogger(String indexName) {
        return getLogger(SYSTEM_TYPE, indexName, DEFAULT_PARAMS_MAP_SUPPLY);
    }

    public static Logger getLogger(ParamsMapSupply supply) {
        return getLogger(SYSTEM_TYPE, INDEX_FORMAT, supply);
    }

    public static Logger getLogger() {
        return getLogger(SYSTEM_TYPE, INDEX_FORMAT, DEFAULT_PARAMS_MAP_SUPPLY);
    }
}
