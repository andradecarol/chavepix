package br.com.chavepix.domain.validation;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CNPJ_INVALIDO;

@Slf4j
@Component
@RequiredArgsConstructor
public class CnpjValidator implements ValidadorChaveStrategy {

    private final MessageConfig messageConfig;

    @Override
    public void validar(String valor) {
        log.info("Validando CNPJ: {}", valor);
        if (valor == null || !valor.matches("\\d{14}")) {
            throw new UnprocessableEntityException(CNPJ_INVALIDO, messageConfig.getMessage(CNPJ_INVALIDO));
        }
        if (valor.matches("(\\d)\\1{13}")) {
            throw new UnprocessableEntityException(CNPJ_INVALIDO, messageConfig.getMessage(CNPJ_INVALIDO));
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) soma += Character.getNumericValue(valor.charAt(i)) * pesos1[i];
        int dig1 = soma % 11; dig1 = dig1 < 2 ? 0 : 11 - dig1;

        soma = 0;
        for (int i = 0; i < 13; i++) soma += Character.getNumericValue(valor.charAt(i)) * pesos2[i];
        int dig2 = soma % 11; dig2 = dig2 < 2 ? 0 : 11 - dig2;

        if (valor.charAt(12) != Character.forDigit(dig1, 10) ||
                valor.charAt(13) != Character.forDigit(dig2, 10)) {
            throw new UnprocessableEntityException(CNPJ_INVALIDO, messageConfig.getMessage(CNPJ_INVALIDO));
        }
    }

    @Override
    public TipoChave getTipoChave() {
        return TipoChave.CNPJ;
    }
}
