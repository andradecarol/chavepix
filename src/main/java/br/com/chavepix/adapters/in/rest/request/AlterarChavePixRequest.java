package br.com.chavepix.adapters.in.rest.request;

import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterarChavePixRequest {
    @NotNull
    private TipoChave tipoChave;

    @NotBlank
    private String valorChave;

    @NotNull
    private TipoConta tipoConta;

    @NotNull
    private Integer numeroAgencia;

    @NotNull
    private Integer numeroConta;

    @NotBlank
    private String nomeCorrentista;

    private String sobrenomeCorrentista;
}
