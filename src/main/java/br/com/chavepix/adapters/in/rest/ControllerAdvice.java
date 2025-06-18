package br.com.chavepix.adapters.in.rest;


import br.com.chavepix.adapters.in.rest.response.ErrorResponse;
import br.com.chavepix.config.application.MessageConfig;
import br.com.chavepix.domain.exceptions.NotFoundException;
import br.com.chavepix.domain.exceptions.RestException;
import br.com.chavepix.domain.exceptions.UnprocessableEntityException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.chavepix.domain.exceptions.MessageErrorCodeConstants.*;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    public static final String EMAIL = "Email";
    public static final String SIZE = "Size";
    public static final String DIGITS = "Digits";
    public static final String PATTERN = "Pattern";
    private static final Integer JVM_MAX_STRING_LEN = Integer.MAX_VALUE;

    private final MessageConfig messageConfig;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<ErrorResponse>> methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ErrorResponse.builder()
                .codigo(FIELD_MUST_BE_VALID)
                .mensagem(getMessage(FIELD_MUST_BE_VALID, e.getName()))
                .build()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ErrorResponse>> bindException(final BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ErrorResponse.builder()
                .codigo(FIELD_MUST_BE_VALID)
                .mensagem(getMessage(FIELD_MUST_BE_VALID, Objects.requireNonNull(e.getBindingResult().getFieldError()).getField()))
                .build()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<List<ErrorResponse>> methodMissingPathVariableException(final MissingPathVariableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ErrorResponse.builder()
                .codigo(FIELD_NOT_BE_NULL)
                .mensagem(getMessage(FIELD_NOT_BE_NULL, e.getVariableName()))
                .build()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<List<ErrorResponse>> methodMissingRequestHeaderException(final MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ErrorResponse.builder()
                .codigo(FIELD_NOT_BE_NULL)
                .mensagem(getMessage(FIELD_NOT_BE_NULL, e.getHeaderName()))
                .build()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Error> mediaTypeNotFoundException(final HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<ErrorResponse>> assertionException(final HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException cause) {
            String field = cause.getPath().stream().map(reference -> {
                if (StringUtils.isNotBlank(reference.getFieldName())) {
                    return reference.getFieldName();
                }
                if (reference.getDescription().contains("java.util.ArrayList")) {
                    return "[" + reference.getIndex() + "]";
                }
                return "";
            }).collect(Collectors.joining("."));

            field = field.replace(".[", "[");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(ErrorResponse.builder()
                    .codigo(FIELD_MUST_BE_VALID)
                    .mensagem(getMessage(FIELD_MUST_BE_VALID, field))
                    .build()));
        }
        if (e.getCause() instanceof UnrecognizedPropertyException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonList(ErrorResponse.builder()
                            .codigo(ADDITIONAL_FIELDS_NOT_ALLOWED)
                            .mensagem(getMessage(ADDITIONAL_FIELDS_NOT_ALLOWED))
                            .build()));
        }
        if (e.getCause() instanceof JsonMappingException cause) {
            String field = cause.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("."));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonList(ErrorResponse.builder()
                            .codigo(INVALID_REQUEST)
                            .mensagem(getMessage(INVALID_REQUEST, field))
                            .build()));
        }
        return defaultBadRequestError();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<List<ErrorResponse>> missingServletRequestParameterException(
            final MissingServletRequestParameterException e) {
        return defaultBadRequestError();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        List<ErrorResponse> messageErrors = Optional.ofNullable(methodArgumentNotValidException)
                .filter(argumentNotValidException -> !ObjectUtils.isEmpty(argumentNotValidException.getBindingResult()))
                .map(MethodArgumentNotValidException::getBindingResult)
                .filter(bindingResult -> !ObjectUtils.isEmpty(bindingResult.getAllErrors()))
                .map(BindingResult::getAllErrors)
                .stream()
                .flatMap(Collection::stream)
                .filter(objectError -> !ObjectUtils.isEmpty(objectError))
                .map(this::createError)
                .toList();
        return new ResponseEntity<>(messageErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<ErrorResponse> errors = e.getConstraintViolations().stream()
                .map(constraint -> {
                    Annotation annotation = constraint.getConstraintDescriptor().getAnnotation();
                    if (annotation instanceof Size) {
                        return new ErrorResponse(constraint.getMessageTemplate(),
                                constraint.getPropertyPath(),
                                constraint.getConstraintDescriptor().getAttributes().get("min"),
                                constraint.getConstraintDescriptor().getAttributes().get("max")
                        );
                    }

                    return new ErrorResponse(constraint.getMessageTemplate(),
                            constraint.getPropertyPath(),
                            constraint.getConstraintDescriptor().getAttributes().get("value"));
                })
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> handleRestException(RestException restException) {
        log.error(restException.getMessage(), restException);

        if (restException.getResponseBodyCode() != null) {
            return ResponseEntity
                    .status(restException.getStatus())
                    .body(new ErrorResponse(restException.getResponseBodyCode(), getMessage(restException.getResponseBodyCode())));
        }

        if (restException.getResponseBody() != null) {
            String message = StringUtils.isBlank(restException.getResponseBody().getMensagem())
                    ? getMessage(restException.getResponseBody().getCodigo())
                    : restException.getResponseBody().getMensagem();

            return ResponseEntity
                    .status(restException.getStatus())
                    .body(new ErrorResponse(restException.getResponseBody().getCodigo(), message));
        }

        return ResponseEntity.status(restException.getStatus()).build();
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<Object> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorResponse(exception.getResponseBody().getCodigo(), exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorResponse(exception.getResponseBody().getCodigo(), exception.getMessage()));
    }


    private static boolean isMinOrMax(ObjectError error) {
        return Objects.equals(error.getCode(), "Max") || Objects.equals(error.getCode(), "Min");
    }

    private static ErrorResponse getSizeErrorResponse(ObjectError error, String field) {
        Integer min = null;
        Integer max = null;
        if (Objects.requireNonNull(error.getArguments()).length > 2) {
            List<Object> argumentsList = Arrays.stream(error.getArguments()).toList();

            Integer rawMax = (Integer) argumentsList.get(1);
            max = Objects.equals(rawMax, JVM_MAX_STRING_LEN) ? null : rawMax;

            Integer rawMin = (Integer) argumentsList.get(2);
            min = rawMin == 0 ? null : rawMin;
        }

        if (min != null && max != null) {
            return new ErrorResponse(error.getDefaultMessage(), field, min, max);
        } else if (min != null) {
            return new ErrorResponse(error.getDefaultMessage(), field, min);
        }

        return new ErrorResponse(error.getDefaultMessage(), error.getCode(), max);
    }

    private static ErrorResponse getPatternErrorResponse(ObjectError error, String field) {
        String regex = null;

        if (Objects.requireNonNull(error.getArguments()).length > 2) {
            List<Object> argumentsList = Arrays.stream(error.getArguments()).toList();
            regex = argumentsList.get(2).toString();
        }

        return new ErrorResponse(FIELD_MUST_BE_PATTERN, field, regex);
    }

    private ResponseEntity<List<ErrorResponse>> defaultBadRequestError() {
        return new ResponseEntity<>(
                Collections.singletonList(ErrorResponse.builder()
                        .codigo(INVALID_REQUEST)
                        .mensagem(getMessage(INVALID_REQUEST))
                        .build()),
                HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse createError(ObjectError error) {
        String field = "";
        if (error instanceof FieldError fieldError) {
            field = fieldError.getField();
        }

        if (Objects.equals(error.getCode(), EMAIL)) {
            return new ErrorResponse(FIELD_MUST_BE_VALID, getMessage(FIELD_MUST_BE_VALID, field));
        }

        if (Objects.equals(error.getCode(), DIGITS)) {
            return new ErrorResponse(FIELD_MUST_BE_VALID, getMessage(FIELD_MUST_BE_VALID, field));
        }

        if (Objects.equals(error.getCode(), PATTERN)) {
            return getPatternErrorResponse(error, field);
        }

        if (isMinOrMax(error)) {
            List<Object> argumentsList = Arrays.stream(Objects.requireNonNull(error.getArguments())).toList();
            return new ErrorResponse(error.getDefaultMessage(), field, argumentsList.get(1));
        }

        if (Objects.equals(error.getCode(), SIZE)) {
            return getSizeErrorResponse(error, field);
        }

        return ErrorResponse.builder()
                .codigo(error.getDefaultMessage())
                .mensagem(getMessage(error.getDefaultMessage(), field))
                .build();
    }

    private String getMessage(String codigo, Object... args) {
        return this.messageConfig.getMessage(codigo, args);
    }
}