package org.bugbusters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando programa...");
        SpringApplication.run(Main.class, args);
    }
}
