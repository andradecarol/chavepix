package br.com.chavepix.application.validator;

import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.BadRequestException;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import br.com.chavepix.domain.model.ChavePix;
import br.com.chavepix.domain.model.TipoChave;
import br.com.chavepix.domain.model.TipoConta;
import br.com.chavepix.domain.model.TipoPessoa;
import br.com.chavepix.domain.ports.out.ChavePixRepository;
import br.com.chavepix.domain.validation.ValidadorChaveFactory;
import br.com.chavepix.domain.validation.ValidadorChaveStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private ValidadorChaveFactory validadorChaveFactory;

    @InjectMocks
    private ChavePixValidator validator;

    private final String mensagemErro = "Erro de validação";

    @BeforeEach
    void setUp() {
        lenient().when(messageConfig.getMessage(anyString())).thenReturn(mensagemErro);
        lenient().when(validadorChaveFactory.getStrategy(any())).thenReturn(mock(ValidadorChaveStrategy.class));
    }

    // ========== validarChaveExistente ==========

    @Nested
    @DisplayName("validarChaveExistente")
    class ValidarChaveExistenteTests {

        @Test
        @DisplayName("Deve validar com sucesso quando chave não existe")
        void deveValidarComSucessoQuandoChaveNaoExiste() {
            String valorChave = "test@email.com";
            when(repository.chaveJaExiste(valorChave)).thenReturn(false);

            assertDoesNotThrow(() -> validator.validarChaveExistente(valorChave));
            verify(repository).chaveJaExiste(valorChave);
            verifyNoInteractions(messageConfig);
        }

        @Test
        @DisplayName("Deve lançar BadRequestException quando chave já existe")
        void deveLancarBadRequestExceptionQuandoChaveJaExiste() {
            String valorChave = "test@email.com";
            when(repository.chaveJaExiste(valorChave)).thenReturn(true);

            BadRequestException exception = assertThrows(BadRequestException.class,
                    () -> validator.validarChaveExistente(valorChave));

            assertEquals(mensagemErro, exception.getMessage());
            verify(repository).chaveJaExiste(valorChave);
            verify(messageConfig).getMessage(anyString());
        }
    }

    // =================== validarLimitePorConta ===================
    @Nested
    @DisplayName("validarLimitePorConta")
    class ValidarLimitePorContaTests {

        @Test
        @DisplayName("Deve validar com sucesso para PF com menos de 5 chaves")
        void deveValidarComSucessoParaPFComMenosDe5Chaves() {
            Integer agencia = 1234;
            Integer conta = 567890;
            List<ChavePix> chaves = createMockChavesList(4);
            when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

            assertDoesNotThrow(() -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_FISICA));
            verify(repository).buscarPorConta(agencia, conta);
            verifyNoInteractions(messageConfig);
        }

        @Test
        @DisplayName("Deve lançar exceção para PF com 5 ou mais chaves")
        void deveLancarExcecaoParaPFCom5OuMaisChaves() {
            Integer agencia = 1234;
            Integer conta = 567890;
            List<ChavePix> chaves = createMockChavesList(5);
            when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

            BadRequestException exception = assertThrows(BadRequestException.class,
                    () -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_FISICA));

            assertEquals(mensagemErro, exception.getMessage());
            verify(repository).buscarPorConta(agencia, conta);
            verify(messageConfig).getMessage(anyString());
        }

        @Test
        @DisplayName("Deve validar com sucesso para PJ com menos de 20 chaves")
        void deveValidarComSucessoParaPJComMenosDe20Chaves() {
            Integer agencia = 1234;
            Integer conta = 567890;
            List<ChavePix> chaves = createMockChavesList(19);
            when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

            assertDoesNotThrow(() -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_JURIDICA));
            verify(repository).buscarPorConta(agencia, conta);
            verifyNoInteractions(messageConfig);
        }

        @Test
        @DisplayName("Deve lançar exceção para PJ com 20 ou mais chaves")
        void deveLancarExcecaoParaPJCom20OuMaisChaves() {
            Integer agencia = 1234;
            Integer conta = 567890;
            List<ChavePix> chaves = createMockChavesList(20);
            when(repository.buscarPorConta(agencia, conta)).thenReturn(chaves);

            BadRequestException exception = assertThrows(BadRequestException.class,
                    () -> validator.validarLimitePorConta(agencia, conta, TipoPessoa.PESSOA_JURIDICA));

            assertEquals(mensagemErro, exception.getMessage());
            verify(repository).buscarPorConta(agencia, conta);
            verify(messageConfig).getMessage(anyString());
        }
    }

    // =================== validarCampos ===================
    @Nested
    @DisplayName("validarCampos")
    class ValidarCamposTests {

        @Test
        @DisplayName("Deve validar com sucesso com todos os campos válidos")
        void deveValidarComSucessoComTodosOsCamposValidos() {
            assertDoesNotThrow(() -> validator.validarCampos(
                    TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                    1234, 567890, "João", "Silva"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome é null")
        void deveLancarExcecaoQuandoNomeENull() {
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            1234, 567890, null, "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome tem mais de 30 caracteres")
        void deveLancarExcecaoQuandoNomeTemMaisDe30Caracteres() {
            String nomeLongo = "a".repeat(31);
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            1234, 567890, nomeLongo, "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve validar com sucesso quando sobrenome é null")
        void deveValidarComSucessoQuandoSobrenomeENull() {
            assertDoesNotThrow(() -> validator.validarCampos(
                    TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                    1234, 567890, "João", null));
        }

        @Test
        @DisplayName("Deve lançar exceção quando sobrenome tem mais de 45 caracteres")
        void deveLancarExcecaoQuandoSobrenomeTemMaisDe45Caracteres() {
            String sobrenomeLongo = "a".repeat(46);
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            1234, 567890, "João", sobrenomeLongo));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando agência é null")
        void deveLancarExcecaoQuandoAgenciaENull() {
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            null, 567890, "João", "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando agência tem mais de 4 dígitos")
        void deveLancarExcecaoQuandoAgenciaTemMaisDe4Digitos() {
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            12345, 567890, "João", "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando conta é null")
        void deveLancarExcecaoQuandoContaENull() {
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            1234, null, "João", "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando conta tem mais de 8 dígitos")
        void deveLancarExcecaoQuandoContaTemMaisDe8Digitos() {
            UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                    () -> validator.validarCampos(TipoChave.EMAIL, "test@email.com", TipoConta.CORRENTE,
                            1234, 123456789, "João", "Silva"));
            assertEquals(mensagemErro, exception.getMessage());
        }

        // Você pode também separar ainda mais por tipo de chave para testar os validadores específicos,
        // mas para não deixar muito longo, deixo assim por enquanto.
    }

    // =================== MÉTODOS AUXILIARES ===================
    private List<ChavePix> createMockChavesList(int size) {
        if (size == 0) {
            return Collections.emptyList();
        }
        return IntStream.range(0, size)
                .mapToObj(i -> mock(ChavePix.class))
                .toList();
    }
}


