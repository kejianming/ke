package com.nfdw.config;

import org.activiti.spring.SpringProcessEngineConfiguration;

import java.io.InputStream;

public class GpdiProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    @Override
    protected InputStream getMyBatisXmlConfigurationSteam() {
        return getResourceAsStream("activiti/mappings.xml");
    }
}
