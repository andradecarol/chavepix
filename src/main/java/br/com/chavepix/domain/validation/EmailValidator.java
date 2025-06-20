package br.com.chavepix.domain.validation;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.EMAIL_INVALIDO;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailValidator implements ValidadorChaveStrategy {

    private final MessageConfig messageConfig;

    @Override
    public void validar(String valor) {
        log.info("Validando email: {}", valor);
        if (valor == null || valor.length() > 77 || !valor.contains("@")) {
            throw new UnprocessableEntityException(EMAIL_INVALIDO, messageConfig.getMessage(EMAIL_INVALIDO));
        }
    }

    @Override
    public TipoChave getTipoChave() {
        return TipoChave.EMAIL;
    }
}
