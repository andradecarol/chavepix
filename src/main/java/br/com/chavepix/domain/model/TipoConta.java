package br.com.chavepix.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoConta {
    CORRENTE,
    POUPANCA;
}
