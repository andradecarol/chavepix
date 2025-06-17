package br.com.chavepix.adapters.in.rest;

import br.com.chavepix.adapters.in.rest.request.AlterarChavePixRequest;
import br.com.chavepix.adapters.in.rest.request.CadastrarChavePixRequest;
import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.domain.exceptions.ChavePixException;
import br.com.chavepix.domain.ports.in.AlterarChavePixUseCase;
import br.com.chavepix.domain.ports.in.CadastrarChavePixUseCase;
import br.com.chavepix.domain.ports.in.ConsultarChavePixUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chaves-pix")
@RequiredArgsConstructor
public class ChavePixController {

    private final CadastrarChavePixUseCase cadastrarService;
    private final AlterarChavePixUseCase alterarService;
    private final ConsultarChavePixUseCase consultarService;


    // Cadastrar nova chave Pix
    @PostMapping
    public ResponseEntity<CadastrarChavePixResponse> cadastrar(@RequestBody @Valid CadastrarChavePixRequest request) {
        CadastrarChavePixResponse response = cadastrarService.cadastrarChave(
                request.getTipoChave(),
                request.getValorChave(),
                request.getTipoConta(),
                request.getTipoPessoa(),
                request.getNumeroAgencia(),
                request.getNumeroConta(),
                request.getNomeCorrentista(),
                request.getSobrenomeCorrentista()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlterarChavePixResponse> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid AlterarChavePixRequest request
    ) {
        AlterarChavePixResponse response = alterarService.alterarChave(
                id,
                request.getTipoConta(),
                request.getNumeroAgencia(),
                request.getNumeroConta(),
                request.getNomeCorrentista(),
                request.getSobrenomeCorrentista()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> consultarChavePix(
            @RequestParam(value = "id", required = false) UUID id,
            @RequestParam(value = "agencia", required = false) Integer agencia,
            @RequestParam(value = "numeroConta", required = false) Integer numeroConta,
            @RequestParam(value = "nomeCorrentista", required = false) String nomeCorrentista
    ) {
        boolean outrosFiltros = agencia != null || numeroConta != null || nomeCorrentista != null;
        if (id != null && outrosFiltros) {
            return ResponseEntity.unprocessableEntity().body("Consulta por ID não pode combinar com outros filtros.");
        }

        try {
            if (id != null) {
                var resposta = consultarService.consultarPorId(id);
                return ResponseEntity.ok(resposta);
            }

            if (agencia != null && numeroConta != null) {
                var resposta = consultarService.consultarPorConta(agencia, numeroConta);
                return ResponseEntity.ok(resposta);
            }

            if (nomeCorrentista != null) {
                var resposta = consultarService.consultarPorNome(nomeCorrentista);
                return ResponseEntity.ok(resposta);
            }

            return ResponseEntity.badRequest().body("É necessário informar pelo menos um filtro válido para consulta.");

        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
