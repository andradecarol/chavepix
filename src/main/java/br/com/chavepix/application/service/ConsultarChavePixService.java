package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.domain.exceptions.ChavePixException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.ports.in.ConsultarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultarChavePixService implements ConsultarChavePixUseCase {

    private final ChavePixRepository repository;

    @Override
    public ConsultarChavePixResponse consultarPorId(UUID id) {
        ChavePix chave = repository.buscarPorId(id)
                .orElseThrow(() -> new ChavePixException("Chave Pix não encontrada"));

        return ChavePixResponseMapper.toConsultarResponse(chave);
    }

    @Override
    public List<ConsultarChavePixResponse> consultarPorConta(Integer agencia, Integer numeroConta){
        List<ChavePix> chaves = repository.buscarPorConta(agencia, numeroConta);

        if (chaves.isEmpty()) {
            throw new ChavePixException("Nenhuma chave Pix encontrada para a conta informada.");
        }

        return ChavePixResponseMapper.toConsultarResponseList(chaves);
    }

    @Override
    public List<ConsultarChavePixResponse> consultarPorNome(String nomeCorrentista){
        List<ChavePix> chaves = repository.buscarPorNome(nomeCorrentista);

        return ChavePixResponseMapper.toConsultarResponseList(chaves);
    }
}
