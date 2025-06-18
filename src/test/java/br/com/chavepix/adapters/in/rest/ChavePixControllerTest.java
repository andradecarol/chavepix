package br.com.chavepix.adapters.in.rest;

import br.com.chavepix.adapters.in.rest.request.AlterarChavePixRequest;
import br.com.chavepix.adapters.in.rest.request.CadastrarChavePixRequest;
import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.ChavePixException;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.in.AlterarChavePixUseCase;
import br.com.chavepix.domain.ports.in.CadastrarChavePixUseCase;
import br.com.chavepix.domain.ports.in.ConsultarChavePixUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChavePixController Tests")
public class ChavePixControllerTest {

    @Mock
    private CadastrarChavePixUseCase cadastrarService;

    @Mock
    private AlterarChavePixUseCase alterarService;

    @Mock
    private ConsultarChavePixUseCase consultarService;

    @Mock
    private MessageConfig messageConfig;

    @InjectMocks
    private ChavePixController chavePixController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chavePixController)
                .setControllerAdvice(new ControllerAdvice(messageConfig))
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX com sucesso")
    void deveCadastrarChavePixComSucesso() throws Exception {
        // Given
        CadastrarChavePixRequest request = createCadastrarRequest();
        CadastrarChavePixResponse expectedResponse = createCadastrarResponse();

        when(cadastrarService.cadastrarChave(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/chaves-pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId().toString()));

        verify(cadastrarService).cadastrarChave(
                request.getTipoChave(),
                request.getValorChave(),
                request.getTipoConta(),
                request.getTipoPessoa(),
                request.getNumeroAgencia(),
                request.getNumeroConta(),
                request.getNomeCorrentista(),
                request.getSobrenomeCorrentista()
        );
    }

    @Test
    @DisplayName("Deve alterar chave PIX com sucesso")
    void deveAlterarChavePixComSucesso() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        AlterarChavePixRequest request = createAlterarRequest();
        AlterarChavePixResponse expectedResponse = createAlterarResponse();

        when(alterarService.alterarChave(
                any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/chaves-pix/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId().toString()));

        verify(alterarService).alterarChave(
                eq(id),
                eq(request.getTipoChave()),
                eq(request.getValorChave()),
                eq(request.getTipoConta()),
                eq(request.getNumeroAgencia()),
                eq(request.getNumeroConta()),
                eq(request.getNomeCorrentista()),
                eq(request.getSobrenomeCorrentista())
        );
    }

    @Test
    @DisplayName("Deve consultar chave PIX por ID com sucesso")
    void deveConsultarChavePixPorIdComSucesso() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        ConsultarChavePixResponse expectedResponse = createConsultarResponse();

        when(consultarService.consultarPorId(id)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId().toString()));

        verify(consultarService).consultarPorId(id);
        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
        verify(consultarService, never()).consultarPorNome(anyString());
    }

    @Test
    @DisplayName("Deve consultar chave PIX por conta com sucesso")
    void deveConsultarChavePixPorContaComSucesso() throws Exception {
        // Given
        Integer agencia = 1234;
        Integer numeroConta = 567890;
        List<ConsultarChavePixResponse> expectedResponse = List.of(createConsultarResponse());

        when(consultarService.consultarPorConta(agencia, numeroConta)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("agencia", agencia.toString())
                        .param("numeroConta", numeroConta.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedResponse.get(0).getId().toString()));

        verify(consultarService).consultarPorConta(agencia, numeroConta);
        verify(consultarService, never()).consultarPorId(any());
        verify(consultarService, never()).consultarPorNome(anyString());
    }

    @Test
    @DisplayName("Deve consultar chave PIX por nome com sucesso")
    void deveConsultarChavePixPorNomeComSucesso() throws Exception {
        // Given
        String nomeCorrentista = "João Silva";
        List<ConsultarChavePixResponse> expectedResponse = List.of(createConsultarResponse());

        when(consultarService.consultarPorNome(nomeCorrentista)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("nomeCorrentista", nomeCorrentista))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedResponse.get(0).getId().toString()));

        verify(consultarService).consultarPorNome(nomeCorrentista);
        verify(consultarService, never()).consultarPorId(any());
        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Deve retornar erro quando ID é combinado com outros filtros")
    void deveRetornarErroQuandoIdECombinadoComOutrosFiltros() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        String mensagem = "Não é possível combinar filtro por ID com outros filtros";

        when(messageConfig.getMessage(anyString())).thenReturn(mensagem);

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("id", id.toString())
                        .param("agencia", "1234"))
                .andExpect(status().isUnprocessableEntity());

        verify(consultarService, never()).consultarPorId(any());
        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
        verify(consultarService, never()).consultarPorNome(anyString());
    }

    @Test
    @DisplayName("Deve retornar bad request quando nenhum filtro é informado")
    void deveRetornarBadRequestQuandoNenhumFiltroEInformado() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix"))
                .andExpect(status().isBadRequest());

        verify(consultarService, never()).consultarPorId(any());
        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
        verify(consultarService, never()).consultarPorNome(anyString());
    }

    @Test
    @DisplayName("Deve retornar bad request quando apenas agência é informada")
    void deveRetornarBadRequestQuandoApenasAgenciaEInformada() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("agencia", "1234"))
                .andExpect(status().isBadRequest());

        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Deve retornar bad request quando apenas número da conta é informado")
    void deveRetornarBadRequestQuandoApenasNumeroContaEInformado() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("numeroConta", "567890"))
                .andExpect(status().isBadRequest());

        verify(consultarService, never()).consultarPorConta(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Deve retornar not found quando chave não é encontrada por ID")
    void deveRetornarNotFoundQuandoChaveNaoEEncontradaPorId() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        String mensagemErro = "Chave PIX não encontrada";

        when(consultarService.consultarPorId(id))
                .thenThrow(new ChavePixException(mensagemErro));

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));

        verify(consultarService).consultarPorId(id);
    }

    @Test
    @DisplayName("Deve retornar not found quando chave não é encontrada por conta")
    void deveRetornarNotFoundQuandoChaveNaoEEncontradaPorConta() throws Exception {
        // Given
        Integer agencia = 1234;
        Integer numeroConta = 567890;
        String mensagemErro = "Nenhuma chave PIX encontrada para esta conta";

        when(consultarService.consultarPorConta(agencia, numeroConta))
                .thenThrow(new ChavePixException(mensagemErro));

        // When & Then
        mockMvc.perform(get("/api/v1/chaves-pix")
                        .param("agencia", agencia.toString())
                        .param("numeroConta", numeroConta.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));

        verify(consultarService).consultarPorConta(agencia, numeroConta);
    }


    private CadastrarChavePixRequest createCadastrarRequest() {
        CadastrarChavePixRequest request = new CadastrarChavePixRequest();
        request.setTipoChave(TipoChave.EMAIL);
        request.setValorChave("carol@teste.com");
        request.setTipoConta(TipoConta.CORRENTE);
        request.setTipoPessoa(TipoPessoa.PESSOA_FISICA);
        request.setNumeroAgencia(1234);
        request.setNumeroConta(567890);
        request.setNomeCorrentista("Carolina");
        request.setSobrenomeCorrentista("Andrade");
        return request;
    }

    private CadastrarChavePixResponse createCadastrarResponse() {
        CadastrarChavePixResponse response = new CadastrarChavePixResponse();
        response.setId(UUID.randomUUID());
        return response;
    }

    private AlterarChavePixRequest createAlterarRequest() {
        AlterarChavePixRequest request = new AlterarChavePixRequest();
        request.setTipoChave(TipoChave.EMAIL);
        request.setValorChave("carol2@teste.com");
        request.setTipoConta(TipoConta.CORRENTE);
        request.setNumeroAgencia(5678);
        request.setNumeroConta(123456);
        request.setNomeCorrentista("Carolina");
        request.setSobrenomeCorrentista("Andrade");
        return request;
    }

    private AlterarChavePixResponse createAlterarResponse() {
        AlterarChavePixResponse response = new AlterarChavePixResponse();
        response.setId(UUID.randomUUID());
        response.setTipoChave(TipoChave.EMAIL);
        response.setValorChave("carol2@teste.com");
        response.setTipoConta(TipoConta.CORRENTE);
        response.setNumeroAgencia(5678);
        response.setNumeroConta(123456);
        response.setNomeCorrentista("Carolina");
        response.setSobrenomeCorrentista("Andrade");
        return response;
    }

    private ConsultarChavePixResponse createConsultarResponse() {
        ConsultarChavePixResponse response = new ConsultarChavePixResponse();
        response.setId(UUID.randomUUID());
        response.setTipoChave(TipoChave.EMAIL);
        response.setValorChave("carol2@teste.com");
        response.setTipoConta(TipoConta.CORRENTE);
        response.setNumeroAgencia(1234);
        response.setNumeroConta(567890);
        response.setNomeCorrentista("João");
        response.setSobrenomeCorrentista("Silva");
        return response;
    }


}
