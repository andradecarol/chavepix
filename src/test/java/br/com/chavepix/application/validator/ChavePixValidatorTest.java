package br.com.chavepix.application.validator;

import br.com.chavepix.config.application.MessageConfig;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChavePixValidator - Testes Unitários")
public class ChavePixValidatorTest {

    @Mock
    private ChavePixRepository repository;

    @Mock
    private MessageConfig messageConfig;

    @InjectMocks
    private ChavePixValidator validator;

    private final String mensagemErro = "Erro de validação";

    @BeforeEach
    void setUp() {
        lenient().when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);
    }

    // ========== TESTES PARA validarChaveExistente ==========

    @Test
    @DisplayName("validarChaveExistente - Deve validar com sucesso quando chave não existe")
    void validarChaveExistente_DeveValidarComSucessoQuandoChaveNaoExiste() {
        // Given
        String valorChave = "test@email.com";
        when(repository.chaveJaExiste(valorChave)).thenReturn(false);

        // When & Then
        assertDoesNotThrow(() -> validator.validarChaveExistente(valorChave));
        verify(repository).chaveJaExiste(valorChave);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("validarChaveExistente - Deve lançar BadRequestException quando chave já existe")
    void validarChaveExistente_DeveLancarBadRequestExceptionQuandoChaveJaExiste() {
        // Given
        String valorChave = "test@email.com";
        when(repository.chaveJaExiste(valorChave)).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validator.validarChaveExistente(valorChave));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).chaveJaExiste(valorChave);
        verify(messageConfig).getMessage(anyString());
    }

    // ========== TESTES PARA validarLimitePorConta ==========

    @Test
    @DisplayName("validarLimitePorConta - Deve validar com sucesso para PF com menos de 5 chaves")
    void validarLimitePorConta_DeveValidarComSucessoParaPFComMenosDe5Chaves() {
        // Given
        Integer agencia = 1234;
        Integer conta = 567890;
        List<ChavePix> chaves = createMockChavesList(4);
        when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

        // When & Then
        assertDoesNotThrow(() -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_FISICA));
        verify(repository).buscarPorConta(agencia, conta);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("validarLimitePorConta - Deve lançar exceção para PF com 5 ou mais chaves")
    void validarLimitePorConta_DeveLancarExcecaoParaPFCom5OuMaisChaves() {
        // Given
        Integer agencia = 1234;
        Integer conta = 567890;
        List<ChavePix> chaves = createMockChavesList(5);
        when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_FISICA));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorConta(agencia, conta);
        verify(messageConfig).getMessage(anyString());
    }

    @Test
    @DisplayName("validarLimitePorConta - Deve validar com sucesso para PJ com menos de 20 chaves")
    void validarLimitePorConta_DeveValidarComSucessoParaPJComMenosDe20Chaves() {
        // Given
        Integer agencia = 1234;
        Integer conta = 567890;
        List<ChavePix> chaves = createMockChavesList(19);
        when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

        // When & Then
        assertDoesNotThrow(() -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_JURIDICA));
        verify(repository).buscarPorConta(agencia, conta);
        verifyNoInteractions(messageConfig);
    }

    @Test
    @DisplayName("validarLimitePorConta - Deve lançar exceção para PJ com 20 ou mais chaves")
    void validarLimitePorConta_DeveLancarExcecaoParaPJCom20OuMaisChaves() {
        // Given
        Integer agencia = 1234;
        Integer conta = 567890;
        List<ChavePix> chaves = createMockChavesList(20);
        when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_JURIDICA));

        assertEquals(mensagemErro, exception.getMessage());
        verify(repository).buscarPorConta(agencia, conta);
        verify(messageConfig).getMessage(anyString());
    }

    // ========== TESTES PARA validarCampos ==========

    @Test
    @DisplayName("validarCampos - Deve validar com sucesso com todos os campos válidos")
    void validarCampos_DeveValidarComSucessoComTodosOsCamposValidos() {
        // Given
        TipoChave tipoChave = TipoChave.EMAIL;
        String valorChave = "test@email.com";
        TipoConta tipoConta = TipoConta.CORRENTE;
        Integer numeroAgencia = 1234;
        Integer numeroConta = 567890;
        String nomeCorrentista = "João";
        String sobrenomeCorrentista = "Silva";

        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                tipoChave, valorChave, tipoConta, numeroAgencia,
                numeroConta, nomeCorrentista, sobrenomeCorrentista));
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando nome é null")
    void validarCampos_DeveLancarExcecaoQuandoNomeENull() {
        // Given
        TipoChave tipoChave = TipoChave.EMAIL;
        String valorChave = "test@email.com";
        TipoConta tipoConta = TipoConta.CORRENTE;

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(tipoChave, valorChave, tipoConta, 1234, 567890, null, "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando nome tem mais de 30 caracteres")
    void validarCampos_DeveLancarExcecaoQuandoNomeTemMaisDe30Caracteres() {
        // Given
        String nomeLongo = "a".repeat(31);

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        1234, 567890, nomeLongo, "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve validar com sucesso quando sobrenome é null")
    void validarCampos_DeveValidarComSucessoQuandoSobrenomeENull() {
        // Given & When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                1234, 567890, "João", null));
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando sobrenome tem mais de 45 caracteres")
    void validarCampos_DeveLancarExcecaoQuandoSobrenomeTemMaisDe45Caracteres() {
        // Given
        String sobrenomeLongo = "a".repeat(46);

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        1234, 567890, "João", sobrenomeLongo));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando agência é null")
    void validarCampos_DeveLancarExcecaoQuandoAgenciaENull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        null, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando agência tem mais de 4 dígitos")
    void validarCampos_DeveLancarExcecaoQuandoAgenciaTemMaisDe4Digitos() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        12345, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando conta é null")
    void validarCampos_DeveLancarExcecaoQuandoContaENull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        1234, null, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCampos - Deve lançar exceção quando conta tem mais de 8 dígitos")
    void validarCampos_DeveLancarExcecaoQuandoContaTemMaisDe8Digitos() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                        1234, 123456789, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarTipoConta ==========

    @Test
    @DisplayName("validarTipoConta - Deve validar CORRENTE com sucesso")
    void validarTipoConta_DeveValidarCorrenteComSucesso() {
        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @Test
    @DisplayName("validarTipoConta - Deve validar POUPANCA com sucesso")
    void validarTipoConta_DeveValidarPoupancaComSucesso() {
        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.EMAIL, "test@email.com", TipoConta.POUPANCA,
                1234, 567890, "João", "Silva"));
    }

    @Test
    @DisplayName("validarTipoConta - Deve lançar exceção quando tipo conta é null")
    void validarTipoConta_DeveLancarExcecaoQuandoTipoContaENull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", null,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarCpf ==========

    @Test
    @DisplayName("validarCpf - Deve validar CPF válido com sucesso")
    void validarCpf_DeveValidarCpfValidoComSucesso() {
        // Given - CPF válido: 11144477735
        String cpfValido = "11144477735";

        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.CPF, cpfValido, TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "1234567890", "abcdefghijk", "111.444.777-35"})
    @DisplayName("validarCpf - Deve lançar exceção para CPF com formato inválido")
    void validarCpf_DeveLancarExcecaoParaCpfComFormatoInvalido(String cpfInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CPF, cpfInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000000000", "11111111111", "22222222222", "99999999999"})
    @DisplayName("validarCpf - Deve lançar exceção para CPF com todos os dígitos iguais")
    void validarCpf_DeveLancarExcecaoParaCpfComTodosOsDigitosIguais(String cpfInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CPF, cpfInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCpf - Deve lançar exceção para CPF com dígitos verificadores inválidos")
    void validarCpf_DeveLancarExcecaoParaCpfComDigitosVerificadoresInvalidos() {
        // Given
        String cpfInvalido = "11144477700";

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CPF, cpfInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCpf - Deve lançar exceção para CPF null")
    void validarCpf_DeveLancarExcecaoParaCpfNull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CPF, null, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarCnpj ==========

    @Test
    @DisplayName("validarCnpj - Deve validar CNPJ válido com sucesso")
    void validarCnpj_DeveValidarCnpjValidoComSucesso() {
        // Given
        String cnpjValido = "11222333000181";

        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.CNPJ, cnpjValido, TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "1234567890123", "abcdefghijklmn", "11.222.333/0001-81"})
    @DisplayName("validarCnpj - Deve lançar exceção para CNPJ com formato inválido")
    void validarCnpj_DeveLancarExcecaoParaCnpjComFormatoInvalido(String cnpjInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CNPJ, cnpjInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000000000000", "11111111111111", "22222222222222"})
    @DisplayName("validarCnpj - Deve lançar exceção para CNPJ com todos os dígitos iguais")
    void validarCnpj_DeveLancarExcecaoParaCnpjComTodosOsDigitosIguais(String cnpjInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CNPJ, cnpjInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCnpj - Deve lançar exceção para CNPJ null")
    void validarCnpj_DeveLancarExcecaoParaCnpjNull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CNPJ, null, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarEmail ==========

    @ParameterizedTest
    @ValueSource(strings = {"test@email.com", "user@domain.com.br", "a@b.co"})
    @DisplayName("validarEmail - Deve validar emails válidos com sucesso")
    void validarEmail_DeveValidarEmailsValidosComSucesso(String emailValido) {
        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.EMAIL, emailValido, TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "email", "email.domain.com"})
    @DisplayName("validarEmail - Deve lançar exceção para emails inválidos")
    void deveLancarExcecaoParaEmailsInvalidos(String emailInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, emailInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarEmail - Deve lançar exceção para email com mais de 77 caracteres")
    void deveLancarExcecaoParaEmailComMaisDe77Caracteres() {
        // Given
        String emailLongo = "a".repeat(70) + "@test.com"; // 78 caracteres

        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, emailLongo, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarEmail - Deve lançar exceção para email null")
    void deveLancarExcecaoParaEmailNull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.EMAIL, null, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarCelular ==========

    @ParameterizedTest
    @ValueSource(strings = {"+5511987654321", "+5521987654321", "+1234567890123"})
    @DisplayName("validarCelular - Deve validar celulares válidos com sucesso")
    void deveValidarCelularesValidosComSucesso(String celularValido) {
        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.CELULAR, celularValido, TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "11987654321", "+55119876543210", "+55aa987654321", "5511987654321"})
    @DisplayName("validarCelular - Deve lançar exceção para celulares inválidos")
    void deveLancarExcecaoParaCelularesInvalidos(String celularInvalido) {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CELULAR, celularInvalido, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    @DisplayName("validarCelular - Deve lançar exceção para celular null")
    void deveLancarExcecaoParaCelularNull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.CELULAR, null, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== TESTES PARA validarAleatoria ==========

    @ParameterizedTest
    @ValueSource(strings = {"a1B2c3D4e5F6g7H8i9J0k1L2m3N4o5P6q7R8", "123456789012345678901234567890123456", "1234567890abcdefghijklmnopqrstuvwxyz"})
    @DisplayName("validarAleatoria - Deve validar chaves aleatórias válidas com sucesso")
    void deveValidarChavesAleatoriasValidasComSucesso(String chaveValida) {
        // When & Then
        assertDoesNotThrow(() -> validator.validarCampos(
                TipoChave.ALEATORIA, chaveValida, TipoConta.CORRENTE,
                1234, 567890, "João", "Silva"));
    }

    @Test
    @DisplayName("validarAleatoria - Deve lançar exceção para chave aleatória null")
    void deveLancarExcecaoParaChaveAleatoriaNull() {
        // When & Then
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> validator.validarCampos(TipoChave.ALEATORIA, null, TipoConta.CORRENTE,
                        1234, 567890, "João", "Silva"));

        assertEquals(mensagemErro, exception.getMessage());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<ChavePix> createMockChavesList(int size) {
        if (size == 0) {
            return Collections.emptyList();
        }
        return IntStream.range(0, size)
                .mapToObj(i -> mock(ChavePix.class))
                .toList();
    }
}

