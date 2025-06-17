package br.com.chavepix.application.mapper;

import br.com.chavepix.adapters.out.persistence.ChavePixEntity;
import br.com.chavepix.domain.model.ChavePix;

public class ChavePixMapper {

    public static ChavePixEntity toEntity(ChavePix domain) {
        ChavePixEntity entity = new ChavePixEntity();
        entity.setId(domain.getId());
        entity.setTipoChave(domain.getTipoChave());
        entity.setValorChave(domain.getValorChave());
        entity.setTipoConta(domain.getTipoConta());
        entity.setTipoPessoa(domain.getTipoPessoa());
        entity.setNumeroAgencia(domain.getNumeroAgencia());
        entity.setNumeroConta(domain.getNumeroConta());
        entity.setNomeCorrentista(domain.getNomeCorrentista());
        entity.setSobrenomeCorrentista(domain.getSobrenomeCorrentista());
        entity.setDataHoraInclusao(domain.getDataHoraInclusao());
        entity.setDataHoraInativacao(domain.getDataHoraInativacao());
        return entity;
    }

    public static ChavePix toDomain(ChavePixEntity entity) {
        return new ChavePix(
                entity.getId(),
                entity.getTipoChave(),
                entity.getValorChave(),
                entity.getTipoConta(),
                entity.getTipoPessoa(),
                entity.getNumeroAgencia(),
                entity.getNumeroConta(),
                entity.getNomeCorrentista(),
                entity.getSobrenomeCorrentista(),
                entity.getDataHoraInclusao(),
                entity.getDataHoraInativacao()
        );
    }
}
