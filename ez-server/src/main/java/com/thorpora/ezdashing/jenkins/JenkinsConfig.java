/**
 * Created by Yannick Lacaute on 17/05/17.
 * Copyright 2015-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thorpora.ezdashing.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JenkinsConfig {

    private static final Logger logger = LoggerFactory.getLogger(JenkinsConfig.class);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM HH:MM");

    @Bean
    public JenkinsPluginsAPI JenkinsPluginsAPI(JenkinsProperties jenkinsProperties) {
        logger.debug("Creating Jenkins plugins client...");
        return Feign.builder()
                .requestInterceptor(new BasicAuthRequestInterceptor(
                        jenkinsProperties.getUserName(),
                        jenkinsProperties.getPassword()))
                .logger(new feign.Logger.ErrorLogger())
                .logLevel(feign.Logger.Level.BASIC)
                .target(JenkinsPluginsAPI.class, jenkinsProperties.getBaseUrl());
    }

    @Bean
    public JenkinsServer JenkinsServer(JenkinsProperties jenkinsProperties) {
        logger.debug("Creating Jenkins client...");
        return new JenkinsServer(new JenkinsHttpClient(
                jenkinsProperties.getJenkinsURI(),
                jenkinsProperties.getUserName(),
                jenkinsProperties.getPassword()));
    }

}
