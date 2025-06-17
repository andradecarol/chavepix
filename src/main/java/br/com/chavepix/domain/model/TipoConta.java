package br.com.chavepix.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoConta {

    CORRENTE,
    POUPANCA;

    public void validar() {
        String nome = this.name();
        if (nome.length() > 10) {
            throw new IllegalArgumentException("Tipo de conta inválido: excede 10 caracteres.");
        }

        if (!nome.equals("CORRENTE") && !nome.equals("POUPANCA")) {
            throw new IllegalArgumentException("Tipo de conta deve ser CORRENTE ou POUPANCA.");
        }
    }

    public static TipoConta fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Tipo de conta não pode ser nulo.");
        }

        try {
            return TipoConta.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de conta inválido: " + valor);
        }
    }
}
