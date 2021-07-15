package com.internetitem.logback.elasticsearch;

import ch.qos.logback.classic.Logger;
import com.internetitem.logback.elasticsearch.util.ElasticSearchLogger;
import com.internetitem.logback.elasticsearch.util.ParamsMapSupply;

import java.util.HashMap;
import java.util.Map;

/**
 * @author minton.zhang
 * @since 2021/7/14 17:37
 */
public class Test {

    static {
        ElasticSearchLogger.setElasticSearchConnUrl("47.111.8.143", 35088, "TEST");

        ElasticSearchLogger.DEFAULT_PARAMS_MAP_SUPPLY = new ParamsMapSupply() {

            @Override
            public Map<String, Object> get() {
                Thread thread = Thread.currentThread();
                HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("1", "2");
                objectObjectHashMap.put("3", "3");
                return objectObjectHashMap;
            }
        };
    }


    public static void main(String[] args) {
        Logger logger = ElasticSearchLogger.getLogger("test_log", () -> {
            Thread thread = Thread.currentThread();
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("1", "2");
            objectObjectHashMap.put("3", "3");
            return objectObjectHashMap;
        });

        logger.error("卧槽2");
        logger.error("卧槽2");
    }
}
