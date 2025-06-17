package br.com.chavepix.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChavePixJpaRepository extends JpaRepository<ChavePixEntity, UUID> {

    boolean existsByValorChave(String valorChave);

    List<ChavePixEntity> findByNumeroAgenciaAndNumeroConta(Integer numeroAgencia, Integer numeroConta);

    List<ChavePixEntity> findByNomeCorrentista(String nomeCorrentista);
}
