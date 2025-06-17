package br.com.chavepix.adapters.in.rest.response;

import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarChavePixResponse {
    private UUID id;
    private TipoChave tipoChave;
    private String valorChave;
    private TipoConta tipoConta;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataInativacao;
}
