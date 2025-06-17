package br.com.chavepix.domain.ports.in;

import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;

public interface CadastrarChavePixUseCase {

    CadastrarChavePixResponse cadastrarChave(
            TipoChave tipoChave,
            String valorChave,
            TipoConta tipoConta,
            TipoPessoa tipoPessoa,
            Integer numeroAgencia,
            Integer numeroConta,
            String nomeCorrentista,
            String sobrenomeCorrentista
    );
}
