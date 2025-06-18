package br.com.chavepix.config.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        DefaultSerializerProvider.Impl serializerProvider = new DefaultSerializerProvider.Impl();
        serializerProvider.setNullValueSerializer(new NullToEmptyStringSerializer());
        objectMapper.setSerializerProvider(serializerProvider);
    }
}
