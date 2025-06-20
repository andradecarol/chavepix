package br.com.chavepix.domain.validation;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CPF_INVALIDO;

@Slf4j
@Component
@RequiredArgsConstructor
public class CpfValidator implements ValidadorChaveStrategy {

    private final MessageConfig messageConfig;

    @Override
    public void validar(String valor) {
        log.info("Validando CPF: {}", valor);
        if (valor == null || !valor.matches("\\d{11}")) {
            throw new UnprocessableEntityException(CPF_INVALIDO, messageConfig.getMessage(CPF_INVALIDO));
        }
        if (valor.matches("(\\d)\\1{10}")) {
            throw new UnprocessableEntityException(CPF_INVALIDO, messageConfig.getMessage(CPF_INVALIDO));
        }

        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(valor.charAt(i)) * peso--;
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 > 9) digito1 = 0;

        soma = 0; peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(valor.charAt(i)) * peso--;
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 > 9) digito2 = 0;

        if (valor.charAt(9) != Character.forDigit(digito1, 10) ||
                valor.charAt(10) != Character.forDigit(digito2, 10)) {
            throw new UnprocessableEntityException(CPF_INVALIDO, messageConfig.getMessage(CPF_INVALIDO));
        }
    }

    @Override
    public TipoChave getTipoChave() {
        return TipoChave.CPF;
    }
}
