package com.kanbanchallenge.kanbanchallengeservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This component allows us to use our custom properties anywhere we should choose to inject it.
 */
@Component
@ConfigurationProperties(prefix = "kanban-challenge")
public class Configuration {
    String loads;
    Integer shift;

    public String getLoads() {
        return loads;
    }

    public void setLoads(String loads) {
        this.loads = loads;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }
}
