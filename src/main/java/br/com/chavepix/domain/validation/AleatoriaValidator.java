package br.com.chavepix.domain.validation;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.ALEATORIA_INVALIDA;

@Slf4j
@Component
@RequiredArgsConstructor
public class AleatoriaValidator implements ValidadorChaveStrategy {

    private final MessageConfig messageConfig;

    @Override
    public void validar(String valor) {
        log.info("Validando chave aleatória: {}", valor);
        if (valor == null || !valor.matches("^[a-zA-Z0-9]{36}$")) {
            throw new UnprocessableEntityException(ALEATORIA_INVALIDA, messageConfig.getMessage(ALEATORIA_INVALIDA));
        }
    }

    @Override
    public TipoChave getTipoChave() {
        return TipoChave.ALEATORIA;
    }
}
