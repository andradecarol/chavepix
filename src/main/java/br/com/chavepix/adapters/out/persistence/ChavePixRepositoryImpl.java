package br.com.chavepix.adapters.out.persistence;

import br.com.chavepix.application.mapper.ChavePixMapper;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChavePixRepositoryImpl implements ChavePixRepository {

    private final ChavePixJpaRepository jpaRepository;

    @Override
    public boolean chaveJaExiste(String valorChave) {
        return jpaRepository.existsByValorChave(valorChave);
    }

    @Override
    public List<ChavePix> buscarPorConta(Integer agencia, Integer numeroConta) {
        List<ChavePixEntity> entities = jpaRepository.findByNumeroAgenciaAndNumeroConta(agencia, numeroConta);
        return entities.stream()
                .map(ChavePixMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChavePix> buscarPorNome(String nomeCorrentista) {
        List<ChavePixEntity> entities = jpaRepository.findByNomeCorrentista(nomeCorrentista);
        return entities.stream()
                .map(ChavePixMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChavePix> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(ChavePixMapper::toDomain);
    }

    @Override
    public void salvar(ChavePix chavePix) {
        ChavePixEntity entity = ChavePixMapper.toEntity(chavePix);
        jpaRepository.save(entity);
    }
}
