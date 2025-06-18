package br.com.chavepix.application.validator;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.BadRequestException;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.*;

@Service
@RequiredArgsConstructor
public class ChavePixValidator {

    private final ChavePixRepository repository;
    private final MessageConfig messageConfig;

    public void validarChaveExistente(String valorChave) {
        if (repository.chaveJaExiste(valorChave)) {
            throw new BadRequestException(CHAVE_EXISTENTE, messageConfig.getMessage(CHAVE_EXISTENTE));
        }
    }

    public void validarLimitePorConta(Integer numeroAgencia, Integer numeroConta, TipoPessoa tipoPessoa) {
        var chaves = repository.buscarPorConta(numeroAgencia, numeroConta);
        int limite = tipoPessoa == TipoPessoa.PESSOA_FISICA ? 5 : 20;

        if (chaves.size() >= limite) {
            throw new BadRequestException(LIMITE_CHAVE_EXCEDIDO, messageConfig.getMessage(LIMITE_CHAVE_EXCEDIDO));
        }
    }

    public void validarCampos(
            TipoChave tipoChave,
            String valorChave,
            TipoConta tipoConta,
            Integer numeroAgencia,
            Integer numeroConta,
            String nomeCorrentista,
            String sobrenomeCorrentista
    ) {
        validarTipoChave(tipoChave, valorChave);
        validarTipoConta(tipoConta);

        if (nomeCorrentista == null || nomeCorrentista.length() > 30) {
            throw new UnprocessableEntityException(NOME_INVALIDO, messageConfig.getMessage(NOME_INVALIDO));
        }

        if (sobrenomeCorrentista != null && sobrenomeCorrentista.length() > 45) {
            throw new UnprocessableEntityException(SOBRENOME_INVALIDO, messageConfig.getMessage(SOBRENOME_INVALIDO));
        }

        if (numeroAgencia == null || numeroAgencia.toString().length() > 4) {
            throw new UnprocessableEntityException(AGENCIA_INVALIDA, messageConfig.getMessage(AGENCIA_INVALIDA));
        }

        if (numeroConta == null || numeroConta.toString().length() > 8) {
            throw new UnprocessableEntityException(CONTA_INVALIDA, messageConfig.getMessage(CONTA_INVALIDA));
        }
    }

    private void validarTipoChave(TipoChave tipo, String valor) {
        switch (tipo) {
            case CPF -> validarCpf(valor);
            case CNPJ -> validarCnpj(valor);
            case EMAIL -> validarEmail(valor);
            case CELULAR -> validarCelular(valor);
            case ALEATORIA -> validarAleatoria(valor);
        }
    }

    private void validarCpf(String valor) {
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

    private void validarCnpj(String valor) {
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

    private void validarEmail(String valor) {
        if (valor == null || valor.length() > 77 || !valor.contains("@")) {
            throw new UnprocessableEntityException(EMAIL_INVALIDO, messageConfig.getMessage(EMAIL_INVALIDO));
        }
    }

    private void validarCelular(String valor) {
        if (valor == null || !valor.matches("^\\+\\d{1,2}\\d{2}\\d{9}$")) {
            throw new UnprocessableEntityException(CELULAR_INVALIDO, messageConfig.getMessage(CELULAR_INVALIDO));
        }
    }

    private void validarAleatoria(String valor) {
        if (valor == null || !valor.matches("^[a-zA-Z0-9]{1,36}$")) {
            throw new UnprocessableEntityException(ALEATORIA_INVALIDA, messageConfig.getMessage(ALEATORIA_INVALIDA));
        }
    }

    private void validarTipoConta(TipoConta tipoConta) {
        if (tipoConta == null || (!tipoConta.equals(TipoConta.CORRENTE) && !tipoConta.equals(TipoConta.POUPANCA))) {
            throw new UnprocessableEntityException(CONTA_INVALIDA, messageConfig.getMessage(CONTA_INVALIDA));
        }
    }
}
