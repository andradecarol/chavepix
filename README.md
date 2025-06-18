[![Spring](https://img.shields.io/badge/-Spring-%236DB33F?logo=Spring&logoColor=%23FFF)](https://spring.io/)
[![Docker](https://img.shields.io/badge/-Docker-%232496ED?logo=Docker&logoColor=%23FFF)](https://www.docker.com/)

# Microsserviço case-chave-pix

Código feito com Java 21, Springboot e Maven.
Microsserviço responsável por simular o cadastro, alteração e consulta de uma chave pix.

## 📐 Arquitetura

Esse microsserviço foi estruturado usando Arquitetura Hexagonal, seguindo a estrutura de pastas abaixo.

```
  /src
    /main
    /java
    /com.api
        /adapters
            /in
            /out
        /application
            /mapper
            /service
            /validator
        /config
            /application
        /domain            
            /exception
            /model
            /ports           
       
```

<br/>

## ☕ Executar

### Compilar o projeto

```
mvn clean install
```

### Subir o docker
```
docker-compose-up -d
```

### Subir a aplicação
- Localize a classe ChavePixApplication, clique em cima dela com o botão direito e selecione a opção 'Run ChavePixApplication.main()'.

# 📚 Operações


| METHOD | ENDPOINT           | DESCRIPTION                                     | ESCOPE |
| --- |--------------------|-------------------------------------------------| --- |
| **POST** | `/api/v1/chaves-pix` | Operação responsável por cadastrar a chave pix. | <kbd>REQUEST</kbd>

# ✔️ Exemplo de requisição local
```
curl --location 'http://localhost:8080/api/v1/chaves-pix' \
--header 'Content-Type: application/json' \
--data '{
       "tipoChave": "EMAIL",
       "valorChave": "teste@teste.com",
       "tipoConta": "CORRENTE",
       "numeroAgencia": 112,
       "numeroConta": 456789,
       "nomeCorrentista": "Carolina",
       "sobrenomeCorrentista": "Andrade",
       "tipoPessoa": "PESSOA_FISICA"
     }''
```
| METHOD | ENDPOINT           | DESCRIPTION                                     | ESCOPE |
| --- |--------------------|-------------------------------------------------| --- |
| **GET** | `/api/v1/chaves-pix` | Operação responsável por consultar a chave pix através de filtros. | <kbd>REQUEST</kbd>

# ✔️ Exemplo de requisição local
```
curl --location 'http://localhost:8080/api/v1/chaves-pix?id=4653de7c-a52f-4479-9786-af501b97a5e3&agencia=123&numeroConta=456789&nomeCorrentista=Carolina%20Andrade' \
--header 'Content-Type: application/json'
```

| METHOD | ENDPOINT           | DESCRIPTION                                     | ESCOPE |
| --- |--------------------|-------------------------------------------------| --- |
| **PUT** | `/api/v1/chaves-pix/{id}` | Operação responsável por alterar a chave pix. | <kbd>REQUEST</kbd>

# ✔️ Exemplo de requisição local
```
curl --location --request PUT 'http://localhost:8080/api/v1/chaves-pix/{id}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "tipoChave": "EMAIL",
    "valorChave": "carol@123",
    "tipoConta": "CORRENTE",
    "numeroAgencia": 123,
    "numeroConta": 456789,
    "nomeCorrentista": "Carolina Andrade",
    "sobrenomeCorrentista": "Silva"
}'
```

