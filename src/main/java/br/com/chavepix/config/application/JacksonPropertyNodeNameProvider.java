package br.com.chavepix.config.application;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty;
import org.hibernate.validator.spi.nodenameprovider.Property;
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider;

public class JacksonPropertyNodeNameProvider implements PropertyNodeNameProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getName(Property property) {
        if (property instanceof JavaBeanProperty javaBeanProperty) {
            return getJavaBeanPropertyName(javaBeanProperty);
        }

        return getDefaultName(property);
    }

    private String getJavaBeanPropertyName(JavaBeanProperty property) {

        JavaType type = objectMapper.constructType(property.getDeclaringClass());
        BeanDescription description = objectMapper.getSerializationConfig().introspect(type);

        return description.findProperties()
                .stream()
                .filter(props -> props.getInternalName().equals(property.getName()))
                .map(BeanPropertyDefinition::getName)
                .findFirst()
                .orElseGet(property::getName);
    }

    private String getDefaultName(Property property) {
        return property.getName();
    }
}

