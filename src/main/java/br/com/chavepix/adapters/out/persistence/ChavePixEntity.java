package br.com.chavepix.adapters.out.persistence;

import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chave_pix")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChavePixEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chave", nullable = false)
    private TipoChave tipoChave;

    @Column(name = "valor_chave", nullable = false, unique = true)
    private String valorChave;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    private TipoConta tipoConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    @Column(name = "numero_agencia", nullable = false)
    private Integer numeroAgencia;

    @Column(name = "numero_conta", nullable = false)
    private Integer numeroConta;

    @Column(name = "nome_correntista", nullable = false, length = 30)
    private String nomeCorrentista;

    @Column(name = "sobrenome_correntista", length = 45)
    private String sobrenomeCorrentista;

    @Column(name = "data_hora_inclusao", nullable = false)
    private LocalDateTime dataHoraInclusao;

    @Column(name = "data_hora_inativacao")
    private LocalDateTime dataHoraInativacao;
}
