package br.com.chavepix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"br.com.chavepix"})
@EnableJpaRepositories(basePackages = "br.com.chavepix.adapters.out.persistence")
public class ChavePixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChavePixApplication.class, args);
    }

}
