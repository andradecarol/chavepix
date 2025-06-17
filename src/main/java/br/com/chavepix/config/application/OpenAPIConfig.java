package br.com.chavepix.config.application;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Case Chave Pix")
                        .description("Microsserviço responsável pelas operações relacionadas a Chave Pix")
                        .version("v1.0.0"));
    }

}