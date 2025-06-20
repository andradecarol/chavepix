package br.com.chavepix.config.application;

import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.validation.ValidadorChaveFactory;
import br.com.chavepix.domain.validation.ValidadorChaveStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ValidadorChaveConfig {

    @Bean
    public ValidadorChaveFactory validadorChaveFactory(
            List<ValidadorChaveStrategy> estrategias) {
        Map<TipoChave, ValidadorChaveStrategy> mapa = estrategias.stream()
                .collect(Collectors.toMap(ValidadorChaveStrategy::getTipoChave, e -> e));
        return new ValidadorChaveFactory(mapa);
    }
}
