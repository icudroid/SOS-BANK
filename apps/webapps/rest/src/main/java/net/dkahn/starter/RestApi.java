package net.dkahn.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by dev on 29/10/16.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"net.dkahn.starter"})
public class RestApi {
    public static void main(String[] args) {
        SpringApplication.run(RestApi.class, args);
    }
}
