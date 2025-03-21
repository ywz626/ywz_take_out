package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 于汶泽
 */
@Configuration
public class OSSConfiguration {

    @Bean
    public AliOssUtil getAliOssUtil(AliOssProperties properties) {
        AliOssUtil aliOssUtil = new AliOssUtil(properties.getEndpoint(),properties.getAccessKeyId(), properties.getAccessKeySecret(), properties.getBucketName());
        return aliOssUtil;
    }
}
