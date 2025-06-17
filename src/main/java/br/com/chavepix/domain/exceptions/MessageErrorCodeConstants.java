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
    public static final String ENUM_INVALIDO = "400.018";
    public static final String DOCUMENTO_INVALIDO = "400.019";
    public static final String DATA_FORMATO_INVALIDO = "400.021";
    public static final String APOLICE_INEXISTENTE = "400.022";
    public static final String ITEM_APOLICE_INEXISTENTE = "400.023";
    public static final String NUMERO_APOLICE_INVALIDO = "400.024";
    public static final String NUMERO_CHASSI_INVALIDO = "400.025";

    //HTTP 404
    public static final String NOT_FOUND = "404.000";

    //HTTP 409
    public static final String CONFLITO = "409.000";
    public static final String APOLICE_EXISTENTE = "409.001";

    //HTTP 422
    public static final String BUSINESS_ERROR = "422.000";
    public static final String DATA_ORDEM_INICIO_FIM = "422.001";
    public static final String DATA_ORDEM_EMISSAO_INICIO = "422.002";
    public static final String ERRO_SEGMENTO_INDIVIDUAL = "422.003";
    public static final String ERRO_SEGMENTO_FROTA = "422.004";
    public static final String QUANTIDADE_ITEM_INVALIDO = "422.005";
    public static final String SEGMENTO_INVALIDO = "422.006";

    //HTTP 500
    public static final String INTERNAL_SERVER_ERROR = "500.000";
}
