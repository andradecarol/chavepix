package br.com.chavepix.application.service;

import br.com.chavepix.adapters.in.rest.response.CadastrarChavePixResponse;
import br.com.chavepix.application.mapper.ChavePixResponseMapper;
import br.com.chavepix.application.validator.ChavePixValidator;
import br.com.chavepix.domain.exceptions.BadRequestException;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarChavePixService Tests")
public class CadastrarChavePixServiceTest {

    @Mock
    private ChavePixRepository repository;

    @Mock
    private ChavePixValidator validator;

    @Mock
    private ChavePixResponseMapper mapper;

    @InjectMocks
    private CadastrarChavePixService service;

    private TipoChave tipoChave;
    private String valorChave;
    private TipoConta tipoConta;
    private TipoPessoa tipoPessoa;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;

    @BeforeEach
    void setUp() {
        tipoChave = TipoChave.CPF;
        valorChave = "12345678901";
        tipoConta = TipoConta.CORRENTE;
        tipoPessoa = TipoPessoa.PESSOA_FISICA;
        numeroAgencia = 1234;
        numeroConta = 567890;
        nomeCorrentista = "João";
        sobrenomeCorrentista = "Silva";
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX com sucesso")
    void deveCadastrarChavePixComSucesso() {
        // Given
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );

        ArgumentCaptor<ChavePix> chaveCaptor = ArgumentCaptor.forClass(ChavePix.class);
        verify(repository).salvar(chaveCaptor.capture());

        ChavePix savedChave = chaveCaptor.getValue();
        assertEquals(tipoChave, savedChave.getTipoChave());
        assertEquals(valorChave, savedChave.getValorChave());
        assertEquals(tipoConta, savedChave.getTipoConta());
        assertEquals(tipoPessoa, savedChave.getTipoPessoa());
        assertEquals(numeroAgencia, savedChave.getNumeroAgencia());
        assertEquals(numeroConta, savedChave.getNumeroConta());
        assertEquals(nomeCorrentista, savedChave.getNomeCorrentista());
        assertEquals(sobrenomeCorrentista, savedChave.getSobrenomeCorrentista());
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX do tipo EMAIL com sucesso")
    void deveCadastrarChavePixEmailComSucesso() {
        // Given
        tipoChave = TipoChave.EMAIL;
        valorChave = "joao@example.com";
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );
        verify(repository).salvar(any(ChavePix.class));
        verify(mapper).toCadastrarResponse(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX do tipo CELULAR com sucesso")
    void deveCadastrarChavePixCelularComSucesso() {
        // Given
        tipoChave = TipoChave.CELULAR;
        valorChave = "+5511987654321";
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );
        verify(repository).salvar(any(ChavePix.class));
        verify(mapper).toCadastrarResponse(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX do tipo CNPJ para pessoa jurídica com sucesso")
    void deveCadastrarChavePixCnpjPessoaJuridicaComSucesso() {
        // Given
        tipoChave = TipoChave.CNPJ;
        valorChave = "12345678000195";
        tipoPessoa = TipoPessoa.PESSOA_JURIDICA;
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );
        verify(repository).salvar(any(ChavePix.class));
        verify(mapper).toCadastrarResponse(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX do tipo ALEATORIA com sucesso")
    void deveCadastrarChavePixAleatoriaComSucesso() {
        // Given
        tipoChave = TipoChave.ALEATORIA;
        valorChave = "123e4567-e89b-12d3-a456-426614174000";
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );
        verify(repository).salvar(any(ChavePix.class));
        verify(mapper).toCadastrarResponse(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve cadastrar chave PIX em conta poupança com sucesso")
    void deveCadastrarChavePixContaPoupancaComSucesso() {
        // Given
        tipoConta = TipoConta.POUPANCA;
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();

        when(mapper.toCadastrarResponse(any(ChavePix.class)))
                .thenReturn(expectedResponse);

        // When
        CadastrarChavePixResponse result = service.cadastrarChave(
                tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta,
                numeroAgencia, numeroConta,
                nomeCorrentista, sobrenomeCorrentista
        );
        verify(repository).salvar(any(ChavePix.class));
        verify(mapper).toCadastrarResponse(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando chave já existe")
    void deveLancarExcecaoQuandoChaveJaExiste() {
        // Given
        String mensagem = "Chave PIX já existe";
        doThrow(new BadRequestException("CHAVE_EXISTENTE", mensagem))
                .when(validator).validarChaveExistente(valorChave);

        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                service.cadastrarChave(
                        tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
                )
        );

        // Then
        assertEquals(mensagem, exception.getMessage());
        verify(validator).validarChaveExistente(valorChave);
        verify(validator, never()).validarLimitePorConta(anyInt(), anyInt(), any());
        verify(validator, never()).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando limite de chaves por conta é excedido")
    void deveLancarExcecaoQuandoLimiteChavesExcedido() {
        // Given
        String mensagem = "Limite de chaves por conta excedido";
        doThrow(new BadRequestException("LIMITE_CHAVE_EXCEDIDO", mensagem))
                .when(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);

        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                service.cadastrarChave(
                        tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
                )
        );

        // Then
        assertEquals(mensagem, exception.getMessage());
        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator, never()).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando campos são inválidos")
    void deveLancarExcecaoQuandoCamposSaoInvalidos() {
        // Given
        String mensagem = "Campos inválidos";
        doThrow(new UnprocessableEntityException("CAMPOS_INVALIDOS", mensagem))
                .when(validator).validarCampos(
                        tipoChave, valorChave, tipoConta,
                        numeroAgencia, numeroConta,
                        nomeCorrentista, sobrenomeCorrentista
                );

        // When
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(
                        tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
                )
        );

        // Then
        assertEquals(mensagem, exception.getMessage());
        verify(validator).validarChaveExistente(valorChave);
        verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        verify(validator).validarCampos(tipoChave, valorChave, tipoConta, numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista);
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF é inválido")
    void deveLancarExcecaoQuandoCpfInvalido() {
        // Given
        String mensagem = "CPF inválido";
        doThrow(new UnprocessableEntityException("CPF_INVALIDO", mensagem))
                .when(validator).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista)
        );

        assertEquals(mensagem, exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é inválido")
    void deveLancarExcecaoQuandoEmailInvalido() {
        // Given
        tipoChave = TipoChave.EMAIL;
        valorChave = "email-invalido";
        String mensagem = "Email inválido";
        doThrow(new UnprocessableEntityException("EMAIL_INVALIDO", mensagem))
                .when(validator).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista)
        );

        assertEquals(mensagem, exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando celular é inválido")
    void deveLancarExcecaoQuandoCelularInvalido() {
        // Given
        tipoChave = TipoChave.CELULAR;
        valorChave = "celular-invalido";
        String mensagem = "Celular inválido";
        doThrow(new UnprocessableEntityException("CELULAR_INVALIDO", mensagem))
                .when(validator).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista)
        );

        assertEquals(mensagem, exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CNPJ é inválido")
    void deveLancarExcecaoQuandoCnpjInvalido() {
        // Given
        tipoChave = TipoChave.CNPJ;
        valorChave = "cnpj-invalido";
        String mensagem = "CNPJ inválido";
        doThrow(new UnprocessableEntityException("CNPJ_INVALIDO", mensagem))
                .when(validator).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista)
        );

        assertEquals(mensagem, exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando chave aleatória é inválida")
    void deveLancarExcecaoQuandoChaveAleatoriaInvalida() {
        // Given
        tipoChave = TipoChave.ALEATORIA;
        valorChave = "chave-aleatoria-muito-longa-e-invalida-com-caracteres-especiais-@#$%";
        String mensagem = "Chave aleatória inválida";
        doThrow(new UnprocessableEntityException("ALEATORIA_INVALIDA", mensagem))
                .when(validator).validarCampos(any(), anyString(), any(), anyInt(), anyInt(), anyString(), anyString());

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () ->
                service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                        numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista)
        );

        assertEquals(mensagem, exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve validar ordem de execução das validações")
    void deveValidarOrdemExecucaoValidacoes() {
        // Given
        CadastrarChavePixResponse expectedResponse = createExpectedResponse();
        when(mapper.toCadastrarResponse(any(ChavePix.class))).thenReturn(expectedResponse);

        // When
        service.cadastrarChave(tipoChave, valorChave, tipoConta, tipoPessoa,
                numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista);

        // Then
        InOrder inOrder = inOrder(validator, repository);
        inOrder.verify(validator).validarChaveExistente(valorChave);
        inOrder.verify(validator).validarLimitePorConta(numeroAgencia, numeroConta, tipoPessoa);
        inOrder.verify(validator).validarCampos(
                tipoChave, valorChave, tipoConta, numeroAgencia, numeroConta, nomeCorrentista, sobrenomeCorrentista
        );
        inOrder.verify(repository).salvar(any(ChavePix.class));
    }









    private CadastrarChavePixResponse createExpectedResponse() {
        CadastrarChavePixResponse response = new CadastrarChavePixResponse();
        response.setId(UUID.randomUUID());
        return response;
    }
}
