package br.com.chavepix.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public enum TipoChave {

    CPF {
        @Override
        public void validar(String valor) {
            if (valor == null || !valor.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
            }
            if (!isCpfValido(valor)) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        }

        private boolean isCpfValido(String cpf) {
            if (cpf.matches("(\\d)\\1{10}")) return false;

            int soma = 0, peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 > 9) digito1 = 0;

            soma = 0; peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 > 9) digito2 = 0;

            return cpf.charAt(9) == Character.forDigit(digito1, 10)
                    && cpf.charAt(10) == Character.forDigit(digito2, 10);
        }
    },

    CNPJ {
        @Override
        public void validar(String valor) {
            if (valor == null || !valor.matches("\\d{14}")) {
                throw new IllegalArgumentException("CNPJ deve conter exatamente 14 dígitos numéricos.");
            }
            if (!isCnpjValido(valor)) {
                throw new IllegalArgumentException("CNPJ inválido.");
            }
        }

        private boolean isCnpjValido(String cnpj) {
            if (cnpj.matches("(\\d)\\1{13}")) return false;

            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) soma += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
            int dig1 = soma % 11; dig1 = dig1 < 2 ? 0 : 11 - dig1;

            soma = 0;
            for (int i = 0; i < 13; i++) soma += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
            int dig2 = soma % 11; dig2 = dig2 < 2 ? 0 : 11 - dig2;

            return cnpj.charAt(12) == Character.forDigit(dig1, 10) &&
                    cnpj.charAt(13) == Character.forDigit(dig2, 10);
        }
    },

    EMAIL {
        @Override
        public void validar(String valor) {
            if (valor == null || valor.length() > 77) {
                throw new IllegalArgumentException("Email não pode ser nulo e deve ter até 77 caracteres.");
            }
            if (!valor.contains("@")) {
                throw new IllegalArgumentException("Email deve conter '@'.");
            }
        }
    },

    CELULAR {
        private final Pattern CELULAR_PATTERN = Pattern.compile("^\\+\\d{1,2}\\d{2}\\d{9}$");

        @Override
        public void validar(String valor) {
            if (valor == null) {
                throw new IllegalArgumentException("Número de celular não pode ser nulo.");
            }
            if (!CELULAR_PATTERN.matcher(valor).matches()) {
                throw new IllegalArgumentException("Celular deve estar no formato +[pais][DDD][9 dígitos]. Ex: +5511999999999");
            }
        }
    },

    ALEATORIA {
        @Override
        public void validar(String valor) {
            if (valor == null || !valor.matches("^[a-zA-Z0-9]{1,36}$")) {
                throw new IllegalArgumentException("Chave aleatória deve conter até 36 caracteres alfanuméricos.");
            }
        }
    };

    public abstract void validar(String valor);

}
