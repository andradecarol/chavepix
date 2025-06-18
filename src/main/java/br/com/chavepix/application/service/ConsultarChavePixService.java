package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.NotFoundException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.ports.in.ConsultarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CHAVE_NAO_ENCONTRADA;

@Service
@RequiredArgsConstructor
public class ConsultarChavePixService implements ConsultarChavePixUseCase {

    private final ChavePixRepository repository;
    private final MessageConfig messageConfig;

    @Override
    public ConsultarChavePixResponse consultarPorId(UUID id) {
        ChavePix chave = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException(CHAVE_NAO_ENCONTRADA, messageConfig.getMessage(CHAVE_NAO_ENCONTRADA) ));

        return ChavePixResponseMapper.toConsultarResponse(chave);
    }

    @Override
    public List<ConsultarChavePixResponse> consultarPorConta(Integer agencia, Integer numeroConta){
        List<ChavePix> chaves = repository.buscarPorConta(agencia, numeroConta);

        if (chaves.isEmpty()) {
            throw new NotFoundException(CHAVE_NAO_ENCONTRADA, messageConfig.getMessage(CHAVE_NAO_ENCONTRADA));
        }

        return ChavePixResponseMapper.toConsultarResponseList(chaves);
    }

    @Override
    public List<ConsultarChavePixResponse> consultarPorNome(String nomeCorrentista){
        List<ChavePix> chaves = repository.buscarPorNome(nomeCorrentista);

        return ChavePixResponseMapper.toConsultarResponseList(chaves);
    }
}
