package br.com.chavepix.application.mapper;

import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;
import br.com.chavepix.domain.model.ChavePix;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChavePixResponseMapper {

    public AlterarChavePixResponse toAlterarResponse(ChavePix chave) {
        return AlterarChavePixResponse.builder()
                .id(chave.getId())
                .tipoChave(chave.getTipoChave())
                .valorChave(chave.getValorChave())
                .tipoConta(chave.getTipoConta())
                .numeroAgencia(chave.getNumeroAgencia())
                .numeroConta(chave.getNumeroConta())
                .nomeCorrentista(chave.getNomeCorrentista())
                .sobrenomeCorrentista(chave.getSobrenomeCorrentista())
                .dataInclusao(chave.getDataHoraInclusao())
                .build();
    }

    public CadastrarChavePixResponse toCadastrarResponse(ChavePix chave) {
        return CadastrarChavePixResponse.builder()
                .id(chave.getId())
                .build();
    }

    public ConsultarChavePixResponse toConsultarResponse(ChavePix chave) {
        return ConsultarChavePixResponse.builder()
                .id(chave.getId())
                .tipoChave(chave.getTipoChave())
                .valorChave(chave.getValorChave())
                .tipoConta(chave.getTipoConta())
                .numeroAgencia(chave.getNumeroAgencia())
                .numeroConta(chave.getNumeroConta())
                .nomeCorrentista(chave.getNomeCorrentista())
                .sobrenomeCorrentista(
                        chave.getSobrenomeCorrentista() == null ? "" : chave.getSobrenomeCorrentista()
                )
                .dataInclusao(chave.getDataHoraInclusao())
                .dataInativacao(chave.getDataHoraInativacao())
                .build();
    }

    public List<ConsultarChavePixResponse> toConsultarResponseList(List<ChavePix> chaves) {
        return chaves.stream()
                .map(this::toConsultarResponse)
                .collect(Collectors.toList());
    }
}
