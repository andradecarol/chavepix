[![Spring](https://img.shields.io/badge/-Spring-%236DB33F?logo=Spring&logoColor=%23FFF)](https://spring.io/)

# Microsserviço rnpa-auto-core-v2-ms

### Documentação

[Link para Documentação aqui](https://sensedia.atlassian.net/wiki/spaces/C/pages/4264296475/rnpa-auto-core-v2-ms)

Código feito com Java 21, Springboot e Maven.


## ⚡ Getting started
## 📐 Arquitetura

Esse microsserviço foi estruturado
usando [arquitetura hexagonal](<https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)#:~:text=The%20hexagonal%20architecture%2C%20or%20ports,means%20of%20ports%20and%20adapters.>)
seguindo a estrutura de pastas abaixo

```
  /src
    /main
    /java
    /com.api
        /application
          /adapters
        /config
          /application
        /domain
            /entities
            /enums
            /exception
            /ports
            /services
            /validations
            /valueobjects
        /infrasctructure
        /mapper
        /rnpa-auto-core-v2-ms
```

<br/>

## ☕ Executar

### Compilar o projeto

```
mvn clean install
```
### Executando os **testes**

```sh
mvn test
```