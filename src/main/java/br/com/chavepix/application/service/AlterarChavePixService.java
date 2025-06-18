package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.application.validator.ChavePixValidator;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.BadRequestException;
import br.com.chavepix.domain.exceptions.NotFoundException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.ports.in.AlterarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CHAVE_INATIVA;
import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.CHAVE_NAO_ENCONTRADA;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlterarChavePixService implements AlterarChavePixUseCase {

    private final ChavePixRepository repository;
    private final ChavePixValidator validator;
    private final MessageConfig messageConfig;


    @Override
    public AlterarChavePixResponse alterarChave(UUID id, TipoConta tipoConta, Integer numeroAgencia, Integer numeroConta, String nomeCorrentista, String sobrenomeCorrentista) {
        log.info("Iniciando alteração da chave Pix com ID {}", id);

        ChavePix chave = repository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Chave Pix não encontrada: id={}", id);
                    return new NotFoundException(CHAVE_NAO_ENCONTRADA, messageConfig.getMessage(CHAVE_NAO_ENCONTRADA));
                });

        if (chave.estaInativa()) {
            log.warn("Chave Pix inativa: id={}", id);
            throw new BadRequestException(CHAVE_INATIVA, messageConfig.getMessage(CHAVE_INATIVA));
        }

        validator.validarCampos(
                chave.getTipoChave(),
                chave.getValorChave(),
                tipoConta,
                numeroAgencia,
                numeroConta,
                nomeCorrentista,
                sobrenomeCorrentista
        );

        chave.atualizarDadosPermitidos(tipoConta, numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista);
        repository.salvar(chave);

        log.info("Chave Pix atualizada com sucesso: id={}", id);
        return ChavePixResponseMapper.toAlterarResponse(chave);
    }
}
