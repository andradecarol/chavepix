package br.com.chavepix.domain.validation;

import br.com.chavepix.domain.model.TipoChave;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ValidadorChaveFactory {

    private final Map<TipoChave, ValidadorChaveStrategy> strategyMap;

    public ValidadorChaveFactory(List<ValidadorChaveStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(ValidadorChaveStrategy::getTipoChave, Function.identity()));
    }

    public ValidadorChaveStrategy getStrategy(TipoChave tipoChave) {
        if (!strategyMap.containsKey(tipoChave)) {
            throw new IllegalArgumentException("Tipo de chave inválido: " + tipoChave);
        }
        return strategyMap.get(tipoChave);
    }
}
