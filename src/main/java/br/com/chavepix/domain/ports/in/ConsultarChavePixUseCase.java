package br.com.chavepix.domain.ports.in;

import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;

import java.util.List;
import java.util.UUID;

public interface ConsultarChavePixUseCase {

    ConsultarChavePixResponse consultarPorId(UUID id);
    List<ConsultarChavePixResponse> consultarPorConta(Integer agencia, Integer numeroConta);
    List<ConsultarChavePixResponse> consultarPorNome(String nomeCorrentista);
}
