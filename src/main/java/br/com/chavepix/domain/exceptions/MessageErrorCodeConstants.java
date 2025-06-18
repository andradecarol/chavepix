package br.com.chavepix.domain.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageErrorCodeConstants {

    //HTTP 400
    public static final String INVALID_REQUEST = "400.000";
    public static final String FIELD_NOT_BE_NULL = "400.001";
    public static final String FIELD_MUST_BE_MIN_MAX_CHARACTER = "400.002";
    public static final String FIELD_MUST_BE_LESS_THAN_VALUE = "400.003";
    public static final String FIELD_MUST_BE_GREATER_THAN_VALUE = "400.004";
    public static final String FIELD_MUST_BE_POSITIVE = "400.005";
    public static final String FIELD_MUST_BE_POSITIVE_OR_ZERO = "400.006";
    public static final String FIELD_MUST_BE_NEGATIVE = "400.007";
    public static final String FIELD_MUST_BE_NEGATIVE_OR_ZERO = "400.008";
    public static final String FIELD_MUST_BE_DATE_IN_PAST = "400.009";
    public static final String FIELD_MUST_BE_DATE_IN_PAST_OR_PRESENT = "400.010";
    public static final String FIELD_MUST_BE_DATE_IN_FUTURE = "400.011";
    public static final String FIELD_MUST_BE_DATE_IN_FUTURE_OR_PRESENT = "400.012";
    public static final String FIELD_MUST_BE_PATTERN = "400.013";
    public static final String FIELD_MUST_BE_VALID = "400.014";
    public static final String FIELD_NOT_ALLOWED = "400.015";
    public static final String HEADER_FIELD_REQUIRED = "400.016";
    public static final String HEADER_FIELD_INVALID = "400.017";
    public static final String CHAVE_INATIVA = "400.018";
    public static final String LIMITE_CHAVE_EXCEDIDO = "400.019";
    public static final String CHAVE_EXISTENTE= "400.020";
    public static final String ADDITIONAL_FIELDS_NOT_ALLOWED = "400.021";
    public static final String FILTRO_INVALIDO = "400.022";

    //HTTP 404
    public static final String NOT_FOUND = "404.000";
    public static final String CHAVE_NAO_ENCONTRADA = "404.001";


    //HTTP 422
    public static final String BUSINESS_ERROR = "422.000";
    public static final String EMAIL_INVALIDO = "422.001";
    public static final String CPF_INVALIDO =  "422.002";
    public static final String CNPJ_INVALIDO = "422.003";
    public static final String CELULAR_INVALIDO = "422.004";
    public static final String ALEATORIA_INVALIDA = "422.005";
    public static final String CONTA_INVALIDA = "422.006";
    public static final String NOME_INVALIDO= "422.007";
    public static final String SOBRENOME_INVALIDO= "422.008";
    public static final String AGENCIA_INVALIDA= "422.009";
    public static final String TIPO_CHAVE_NAO_CORRESPONDE= "422.010";
    public static final String FILTRO_ID_COMBINADO= "422.011";

    //HTTP 500
    public static final String INTERNAL_SERVER_ERROR = "500.000";
}
