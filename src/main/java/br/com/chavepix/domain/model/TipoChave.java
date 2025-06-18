package br.com.chavepix.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoChave {
    CPF,
    CNPJ,
    EMAIL,
    CELULAR,
    ALEATORIA
}
