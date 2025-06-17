package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.domain.exceptions.ChavePixException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.ports.in.AlterarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarChavePixService implements AlterarChavePixUseCase {

    private final ChavePixRepository repository;


    @Override
    public AlterarChavePixResponse alterarChave(UUID id, TipoConta tipoConta, Integer numeroAgencia, Integer numeroConta, String nomeCorrentista, String sobrenomeCorrentista) {

        ChavePix chave = repository.buscarPorId(id)
                .orElseThrow(() -> new ChavePixException("Chave não encontrada."));

        if (chave.estaInativa()) {
            throw new ChavePixException("Não é possível alterar uma chave inativa.");
        }

        chave.atualizarDadosPermitidos(tipoConta, numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista);

        repository.salvar(chave);

        return ChavePixResponseMapper.toAlterarResponse(chave);
    }
}
