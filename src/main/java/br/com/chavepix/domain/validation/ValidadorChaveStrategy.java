package br.com.chavepix.domain.validation;

import br.com.chavepix.domain.model.TipoChave;

public interface ValidadorChaveStrategy {

    void validar(String valor);
    TipoChave getTipoChave();
}
