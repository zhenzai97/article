package com.springbootinit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Spring MVC Json 配置
 *
 */
@JsonComponent
public class JsonConfig {

    /**
     * API 返回中的日期时间格式
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 添加 Long 转 json 精度丢失的配置，并统一 Date 序列化格式
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        // 全局 Date 格式：避免返回 ISO-8601（如 2026-06-26T03:38:07.000+00:00）
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_PATTERN));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return objectMapper;
    }
}