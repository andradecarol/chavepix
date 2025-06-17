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

        validar();
    }

    public void atualizarDadosPermitidos(TipoConta tipoConta,
                                         Integer numeroAgencia,
                                         Integer numeroConta,
                                         String nomeCorrentista,
                                         String sobrenomeCorrentista) {
        if (estaInativa()) {
            throw new IllegalStateException("Não é possível alterar uma chave inativa.");
        }

        this.tipoConta = tipoConta;
        this.numeroAgencia = numeroAgencia;
        this.numeroConta = numeroConta;
        this.nomeCorrentista = nomeCorrentista;
        this.sobrenomeCorrentista = sobrenomeCorrentista;

        validar();
    }

    private void validar() {
        tipoChave.validar(valorChave);
        tipoConta.validar();

        if (nomeCorrentista == null || nomeCorrentista.length() > 30) {
            throw new IllegalArgumentException("Nome do correntista inválido.");
        }

        if (sobrenomeCorrentista != null && sobrenomeCorrentista.length() > 45) {
            throw new IllegalArgumentException("Sobrenome do correntista inválido.");
        }

        if (numeroAgencia == null || numeroAgencia.toString().length() > 4) {
            throw new IllegalArgumentException("Número da agência inválido.");
        }

        if (numeroConta == null || numeroConta.toString().length() > 8) {
            throw new IllegalArgumentException("Número da conta inválido.");
        }
    }

    public boolean estaInativa() {
        return dataHoraInativacao != null;
    }

}
