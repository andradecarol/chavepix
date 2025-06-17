package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.request.CadastrarChavePixRequest;
import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.domain.exceptions.ChavePixException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.in.CadastrarChavePixUseCase;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarChavePixService implements CadastrarChavePixUseCase {

    private final ChavePixRepository repository;

    @Override
    public CadastrarChavePixResponse cadastrarChave(TipoChave tipoChave, String valorChave, TipoConta tipoConta, TipoPessoa tipoPessoa, Integer numeroAgencia, Integer numeroConta, String nomeCorrentista, String sobrenomeCorrentista) {
        if (repository.chaveJaExiste(valorChave)) {
            throw new ChavePixException("Chave Pix já cadastrada.");
        }

        List<ChavePix> chavesExistentes = repository.buscarPorConta(numeroAgencia, numeroConta);
        var limite = (tipoPessoa == TipoPessoa.PESSOA_FISICA) ? 5 :20;

        if (chavesExistentes.size() >= limite) {
            throw new ChavePixException("Limite de chaves excedido para a conta.");
        }

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

    public CadastrarChavePixResponse cadastrar(CadastrarChavePixRequest request) {
        return this.cadastrarChave(
                request.getTipoChave(),
                request.getValorChave(),
                request.getTipoConta(),
                request.getTipoPessoa(),
                request.getNumeroAgencia(),
                request.getNumeroConta(),
                request.getNomeCorrentista(),
                request.getSobrenomeCorrentista()
        );
    }
}
