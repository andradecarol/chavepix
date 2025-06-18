package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.AlterarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.application.validator.ChavePixValidator;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.BadRequestException;
import br.com.chavepix.domain.exceptions.NotFoundException;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlterarChavePixService - Testes Unitários")
public class AlterarChavePixServiceTest {

    @Mock
    private ChavePixRepository repository;

    @Mock
    private ChavePixValidator validator;

    @Mock
    private MessageConfig messageConfig;

    @Mock
    private ChavePixResponseMapper mapper;

    @InjectMocks
    private AlterarChavePixService service;

    private UUID id;
    private TipoChave tipoChave;
    private String valorChave;
    private TipoConta tipoConta;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private ChavePix chavePixMock;
    private AlterarChavePixResponse responseMock;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        tipoChave = TipoChave.EMAIL;
        valorChave = "teste@email.com";
        tipoConta = TipoConta.CORRENTE;
        numeroAgencia = 1234;
        numeroConta = 567890;
        nomeCorrentista = "João";
        sobrenomeCorrentista = "Silva";

        chavePixMock = mock(ChavePix.class);
        responseMock = mock(AlterarChavePixResponse.class);
    }

    @Test
    @DisplayName("Deve alterar chave Pix com sucesso")
    void deveAlterarChavePixComSucesso() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        when(mapper.toAlterarResponse(chavePixMock)).thenReturn(responseMock);

        // When
        AlterarChavePixResponse result = service.alterarChave(
                id, tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(responseMock, result);

        verify(repository).buscarPorId(id);
        verify(chavePixMock).estaInativa();
        verify(chavePixMock).getTipoChave();
        verify(validator).validarCampos(tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);
        verify(chavePixMock).atualizarDadosPermitidos(tipoConta, valorChave, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);
        verify(repository).salvar(chavePixMock);
        verify(mapper).toAlterarResponse(chavePixMock);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando chave não for encontrada")
    void deveLancarNotFoundExceptionQuandoChaveNaoForEncontrada() {
        // Given
        String mensagemErro = "Chave Pix não encontrada";
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());
        when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.alterarChave(id, tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(messageConfig).getMessage(anyString());
        verifyNoInteractions(validator, mapper);
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando chave estiver inativa")
    void deveLancarBadRequestExceptionQuandoChaveEstiverInativa() {
        // Given
        String mensagemErro = "Chave Pix inativa";
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(true);
        when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.alterarChave(id, tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(chavePixMock).estaInativa();
        verify(messageConfig).getMessage(anyString());
        verify(chavePixMock, never()).getTipoChave();
        verifyNoInteractions(validator, mapper);
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar UnprocessableEntityException quando tipo de chave não corresponder")
    void deveLancarUnprocessableEntityExceptionQuandoTipoChaveNaoCorresponder() {
        // Given
        String mensagemErro = "Tipo de chave não corresponde";
        TipoChave tipoChaveDiferente = TipoChave.CPF;
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChaveDiferente);
        when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> service.alterarChave(id, tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(chavePixMock).estaInativa();
        verify(chavePixMock).getTipoChave();
        verify(messageConfig).getMessage(anyString());
        verifyNoInteractions(validator, mapper);
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve propagar exceção do validator quando validação falhar")
    void devePropagarExcecaoDoValidatorQuandoValidacaoFalhar() {
        // Given
        RuntimeException validationException = new RuntimeException("Erro de validação");
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        doThrow(validationException).when(validator).validarCampos(
                tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.alterarChave(id, tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista));

        assertEquals("Erro de validação", exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(chavePixMock).estaInativa();
        verify(chavePixMock).getTipoChave();
        verify(validator).validarCampos(tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);
        verify(chavePixMock, never()).atualizarDadosPermitidos(any(), any(), any(), any(), any(), any());
        verify(repository, never()).salvar(any());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deve verificar interação com métodos específicos do ChavePix")
    void deveVerificarInteracaoComMetodosEspecificosDoChavePix() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        when(mapper.toAlterarResponse(chavePixMock)).thenReturn(responseMock);

        // When
        service.alterarChave(id, tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);

        // Then
        verify(chavePixMock).atualizarDadosPermitidos(
                eq(tipoConta),
                eq(valorChave),
                eq(numeroAgencia),
                eq(numeroConta),
                eq(nomeCorrentista),
                eq(sobrenomeCorrentista)
        );
    }

    @Test
    @DisplayName("Deve funcionar com parâmetros nulos opcionais")
    void deveFuncionarComParametrosNulosOpcionais() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        when(mapper.toAlterarResponse(chavePixMock)).thenReturn(responseMock);

        // When
        AlterarChavePixResponse result = service.alterarChave(
                id, tipoChave, valorChave, tipoConta, null,
                null, null, null
        );

        // Then
        assertNotNull(result);
        verify(validator).validarCampos(tipoChave, valorChave, tipoConta, null,
                null, null, null);
        verify(chavePixMock).atualizarDadosPermitidos(tipoConta, valorChave, null,
                null, null, null);
    }

    @Test
    @DisplayName("Deve verificar ordem de execução das operações")
    void deveVerificarOrdemDeExecucaoDasOperacoes() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        when(mapper.toAlterarResponse(chavePixMock)).thenReturn(responseMock);

        var inOrder = inOrder(repository, chavePixMock, validator, mapper);

        // When
        service.alterarChave(id, tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista);

        // Then
        inOrder.verify(repository).buscarPorId(id);
        inOrder.verify(chavePixMock).estaInativa();
        inOrder.verify(chavePixMock).getTipoChave();
        inOrder.verify(validator).validarCampos(any(), any(), any(), any(), any(), any(), any());
        inOrder.verify(chavePixMock).atualizarDadosPermitidos(any(), any(), any(), any(), any(), any());
        inOrder.verify(repository).salvar(chavePixMock);
        inOrder.verify(mapper).toAlterarResponse(chavePixMock);
    }

    @Test
    @DisplayName("Deve verificar que não salva quando há erro na validação")
    void deveVerificarQueNaoSalvaQuandoHaErroNaValidacao() {
        // Given
        when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
        when(chavePixMock.estaInativa()).thenReturn(false);
        when(chavePixMock.getTipoChave()).thenReturn(tipoChave);
        doThrow(new IllegalArgumentException("Validação falhou"))
                .when(validator).validarCampos(any(), any(), any(), any(), any(), any(), any());

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.alterarChave(id, tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista));

        verify(repository, never()).salvar(any());
        verify(chavePixMock, never()).atualizarDadosPermitidos(any(), any(), any(), any(), any(), any());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de chave")
    void deveTestarDiferentesTiposDeChave() {
        // Given
        TipoChave[] tiposChave = {TipoChave.EMAIL, TipoChave.CPF, TipoChave.CNPJ, TipoChave.CELULAR};

        for (TipoChave tipo : tiposChave) {
            // Reset mocks
            reset(repository, chavePixMock, validator, mapper);

            when(repository.buscarPorId(id)).thenReturn(Optional.of(chavePixMock));
            when(chavePixMock.estaInativa()).thenReturn(false);
            when(chavePixMock.getTipoChave()).thenReturn(tipo);
            when(mapper.toAlterarResponse(chavePixMock)).thenReturn(responseMock);

            // When
            AlterarChavePixResponse result = service.alterarChave(
                    id, tipo, valorChave, tipoConta, numeroAgencia,
                    numeroConta, nomeCorrentista, sobrenomeCorrentista);

            // Then
            assertNotNull(result);
            verify(chavePixMock).getTipoChave();
        }
    }
}
