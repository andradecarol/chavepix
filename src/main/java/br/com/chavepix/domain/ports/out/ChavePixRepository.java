package br.com.chavepix.domain.ports.out;

import br.com.chavepix.domain.model.ChavePix;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChavePixRepository {

    boolean chaveJaExiste(String valorChave);
    List<ChavePix> buscarPorConta(Integer agencia, Integer numeroConta);
    List<ChavePix> buscarPorNome(String nomeCorrentista);
    Optional<ChavePix> buscarPorId(UUID id);
    void salvar(ChavePix chavePix);
}
