package br.com.chavepix.domain.validation;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CELULAR_INVALIDO;

@Slf4j
@Component
@RequiredArgsConstructor
public class CelularValidator implements ValidadorChaveStrategy {

    private final MessageConfig messageConfig;

    @Override
    public void validar(String valor) {
        log.info("Validando celular: {}", valor);
        if (valor == null || !valor.matches("^\\+\\d{1,2}\\d{2}\\d{9}$")) {
            throw new UnprocessableEntityException(CELULAR_INVALIDO, messageConfig.getMessage(CELULAR_INVALIDO));
        }
    }

    @Override
    public TipoChave getTipoChave() {
        return TipoChave.CELULAR;
    }
}
