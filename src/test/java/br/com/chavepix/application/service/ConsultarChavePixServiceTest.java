package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.ConsultarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.NotFoundException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultarChavePixService - Testes Unitários")
public class ConsultarChavePixServiceTest {

    @Mock
    private ChavePixRepository repository;

    @Mock
    private MessageConfig messageConfig;

    @Mock
    private ChavePixResponseMapper mapper;

    @InjectMocks
    private ConsultarChavePixService service;

    private UUID id;
    private Integer agencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private ChavePix chavePixMock;
    private ConsultarChavePixResponse responseMock;
    private List<ChavePix> listaChavesMock;
    private List<ConsultarChavePixResponse> listaResponseMock;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        agencia = 1234;
        numeroConta = 567890;
        nomeCorrentista = "João Silva";

        chavePixMock = mock(ChavePix.class);
        responseMock = mock(ConsultarChavePixResponse.class);
        listaChavesMock = Arrays.asList(chavePixMock, mock(ChavePix.class));
        listaResponseMock = Arrays.asList(responseMock, mock(ConsultarChavePixResponse.class));
    }

    // ========== TESTES PARA consultarPorId ==========

    @Test
    @DisplayName("consultarPorId - Deve consultar chave Pix por ID com sucesso")
    void consultarPorId_DeveConsultarChavePixPorIdComSucesso() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(mapper.toConsultarResponse(chavePixMock)).thenReturn(responseMock);

        // When
        ConsultarChavePixResponse result = service.consultarPorId(id);

        // Then
        assertNotNull(result);
        assertEquals(responseMock, result);

        verify(repository).buscarPorId(id);
        verify(mapper).toConsultarResponse(chavePixMock);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("consultarPorId - Deve lançar NotFoundException quando chave não for encontrada")
    void consultarPorId_DeveLancarNotFoundExceptionQuandoChaveNaoForEncontrada() {
        // Given
        String mensagemErro = "Chave Pix não encontrada";
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());
        when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.consultarPorId(id));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(messageConfig).getMessage(anyString());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("consultarPorId - Deve verificar se mapper é chamado com chave correta")
    void consultarPorId_DeveVerificarSeMapperEChamadoComChaveCorreta() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(mapper.toConsultarResponse(chavePixMock)).thenReturn(responseMock);

        // When
        service.consultarPorId(id);

        // Then
        verify(mapper).toConsultarResponse(eq(chavePixMock));
    }

    // ========== TESTES PARA consultarPorConta ==========

    @Test
    @DisplayName("consultarPorConta - Deve consultar chaves Pix por conta com sucesso")
    void consultarPorConta_DeveConsultarChavesPixPorContaComSucesso() {
        // Given
        when(repository.buscarPorConta(agencia, numeroConta)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorConta(agencia, numeroConta);

        // Then
        assertNotNull(result);
        assertEquals(listaResponseMock, result);
        assertFalse(result.isEmpty());

        verify(repository).buscarPorConta(agencia, numeroConta);
        verify(mapper).toConsultarResponseList(listaChavesMock);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("consultarPorConta - Deve lançar NotFoundException quando lista de chaves estiver vazia")
    void consultarPorConta_DeveLancarNotFoundExceptionQuandoListaDeChavesEstiverVazia() {
        // Given
        String mensagemErro = "Chave Pix não encontrada";
        when(repository.buscarPorConta(agencia, numeroConta)).thenReturn(Collections.emptyList());
        when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.consultarPorConta(agencia, numeroConta));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorConta(agencia, numeroConta);
        verify(messageConfig).getMessage(anyString());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("consultarPorConta - Deve consultar com uma única chave na lista")
    void consultarPorConta_DeveConsultarComUmaUnicaChaveNaLista() {
        // Given
        List<ChavePix> listaComUmaChave = Collections.singletonList(chavePixMock);
        List<ConsultarChavePixResponse> responseComUmaChave = Collections.singletonList(responseMock);

        when(repository.buscarPorConta(agencia, numeroConta)).thenReturn(listaComUmaChave);
        when(mapper.toConsultarResponseList(listaComUmaChave)).thenReturn(responseComUmaChave);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorConta(agencia, numeroConta);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseComUmaChave, result);

        verify(repository).buscarPorConta(agencia, numeroConta);
        verify(mapper).toConsultarResponseList(listaComUmaChave);
    }

    @Test
    @DisplayName("consultarPorConta - Deve consultar com múltiplas chaves na lista")
    void consultarPorConta_DeveConsultarComMultiplasChavesNaLista() {
        // Given
        when(repository.buscarPorConta(agencia, numeroConta)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorConta(agencia, numeroConta);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(listaResponseMock, result);

        verify(repository).buscarPorConta(agencia, numeroConta);
        verify(mapper).toConsultarResponseList(listaChavesMock);
    }

    @Test
    @DisplayName("consultarPorConta - Deve passar parâmetros corretos para repository")
    void consultarPorConta_DevePassarParametrosCorretosParaRepository() {
        // Given
        when(repository.buscarPorConta(agencia, numeroConta)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        service.consultarPorConta(agencia, numeroConta);

        // Then
        verify(repository).buscarPorConta(eq(agencia), eq(numeroConta));
    }

    // ========== TESTES PARA consultarPorNome ==========

    @Test
    @DisplayName("consultarPorNome - Deve consultar chaves Pix por nome com sucesso")
    void consultarPorNome_DeveConsultarChavesPixPorNomeComSucesso() {
        // Given
        when(repository.buscarPorNome(nomeCorrentista)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorNome(nomeCorrentista);

        // Then
        assertNotNull(result);
        assertEquals(listaResponseMock, result);

        verify(repository).buscarPorNome(nomeCorrentista);
        verify(mapper).toConsultarResponseList(listaChavesMock);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("consultarPorNome - Deve retornar lista vazia quando não encontrar chaves")
    void consultarPorNome_DeveRetornarListaVaziaQuandoNaoEncontrarChaves() {
        // Given
        List<ChavePix> listaVazia = Collections.emptyList();
        List<ConsultarChavePixResponse> responseVazia = Collections.emptyList();

        when(repository.buscarPorNome(nomeCorrentista)).thenReturn(listaVazia);
        when(mapper.toConsultarResponseList(listaVazia)).thenReturn(responseVazia);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorNome(nomeCorrentista);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(responseVazia, result);

        verify(repository).buscarPorNome(nomeCorrentista);
        verify(mapper).toConsultarResponseList(listaVazia);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("consultarPorNome - Deve consultar com nome contendo espaços")
    void consultarPorNome_DeveConsultarComNomeContendoEspacos() {
        // Given
        String nomeComEspacos = "João da Silva Santos";
        when(repository.buscarPorNome(nomeComEspacos)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorNome(nomeComEspacos);

        // Then
        assertNotNull(result);
        assertEquals(listaResponseMock, result);
        verify(repository).buscarPorNome(eq(nomeComEspacos));
    }

    @Test
    @DisplayName("consultarPorNome - Deve consultar com nome em maiúsculas")
    void consultarPorNome_DeveConsultarComNomeEmMaiusculas() {
        // Given
        String nomeEmMaiusculas = "JOÃO SILVA";
        when(repository.buscarPorNome(nomeEmMaiusculas)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        List<ConsultarChavePixResponse> result = service.consultarPorNome(nomeEmMaiusculas);

        // Then
        assertNotNull(result);
        assertEquals(listaResponseMock, result);
        verify(repository).buscarPorNome(eq(nomeEmMaiusculas));
    }

    @Test
    @DisplayName("consultarPorNome - Deve passar parâmetro correto para repository")
    void consultarPorNome_DevePassarParametroCorretoParaRepository() {
        // Given
        when(repository.buscarPorNome(nomeCorrentista)).thenReturn(listaChavesMock);
        when(mapper.toConsultarResponseList(listaChavesMock)).thenReturn(listaResponseMock);

        // When
        service.consultarPorNome(nomeCorrentista);

        // Then
        verify(repository).buscarPorNome(eq(nomeCorrentista));
        verify(mapper).toConsultarResponseList(eq(listaChavesMock));
    }
}
