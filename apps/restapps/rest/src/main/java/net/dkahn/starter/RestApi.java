package net.dkahn.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Start spring boot
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"net.dkahn.starter"})
public class RestApi {
    public static void main(String[] args) {
        SpringApplication.run(RestApi.class, args);
    }
}
