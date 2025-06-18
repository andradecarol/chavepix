package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.application.validator.ChavePixValidator;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.in.CadastrarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarChavePixService implements CadastrarChavePixUseCase {

    private final ChavePixRepository repository;
    private final ChavePixValidator validator;

    @Override
    public CadastrarChavePixResponse cadastrarChave(TipoChave tipoChave, String valorChave, TipoConta tipoConta, TipoPessoa tipoPessoa, Integer numeroAgencia, Integer numeroConta, String nomeCorrentista, String sobrenomeCorrentista) {

        validator.validarChaveExistente(valorChave);
        validator.validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        validator.validarCampos(tipoChave, valorChave, tipoConta, numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista);

        ChavePix novaChave = new ChavePix(
                UUID.randomUUID(),
                tipoChave,
                valorChave,
                tipoConta,
                tipoPessoa,
                numeroAgencia,
                numeroConta,
                nomeCorrentista,
                sobrenomeCorrentista,
                LocalDateTime.now(),
                null
        );

        repository.salvar(novaChave);

        return ChavePixResponseMapper.toCadastrarResponse(novaChave);
    }
}
