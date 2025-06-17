package br.com.chavepix.domain.ports.in;

import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.domain.model.TipoConta;

import java.util.UUID;

public interface AlterarChavePixUseCase {

    AlterarChavePixResponse alterarChave(UUID id,
                                         TipoConta tipoConta,
                                         Integer numeroAgencia,
                                         Integer numeroConta,
                                         String nomeCorrentista,
                                         String sobrenomeCorrentista);
}
