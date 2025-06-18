package br.com.chavepix.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChavePix {

    private UUID id;
    private TipoChave tipoChave;
    private String valorChave;
    private TipoConta tipoConta;
    private TipoPessoa tipoPessoa;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraInativacao;

    public ChavePix(
            TipoChave tipoChave,
            String valorChave,
            TipoConta tipoConta,
            TipoPessoa tipoPessoa,
            Integer numeroAgencia,
            Integer numeroConta,
            String nomeCorrentista,
            String sobrenomeCorrentista
    ) {
        this.id = UUID.randomUUID();
        this.tipoChave = tipoChave;
        this.valorChave = valorChave;
        this.tipoConta = tipoConta;
        this.tipoPessoa = tipoPessoa;
        this.numeroAgencia = numeroAgencia;
        this.numeroConta = numeroConta;
        this.nomeCorrentista = nomeCorrentista;
        this.sobrenomeCorrentista = sobrenomeCorrentista;
        this.dataHoraInclusao = LocalDateTime.now();
    }

    public void atualizarDadosPermitidos(TipoConta tipoConta,
                                         String valorChave,
                                         Integer numeroAgencia,
                                         Integer numeroConta,
                                         String nomeCorrentista,
                                         String sobrenomeCorrentista) {
        if (estaInativa()) {
            throw new IllegalStateException("Não é possível alterar uma chave inativa.");
        }

        this.tipoConta = tipoConta;
        this.valorChave = valorChave;
        this.numeroAgencia = numeroAgencia;
        this.numeroConta = numeroConta;
        this.nomeCorrentista = nomeCorrentista;
        this.sobrenomeCorrentista = sobrenomeCorrentista;
    }

    public boolean estaInativa() {
        return dataHoraInativacao != null;
    }

}
