package org.bugbusters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando programa...");
        SpringApplication.run(Main.class, args);
    }
}
