package net.dkahn.starter.services;

import net.dkahn.starter.tools.cipher.PasswordBasedEncoderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@SpringBootApplication
@ComponentScan("net.dkahn.starter")
@Import(PasswordBasedEncoderConfig.class)
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder(){
		return new StandardPasswordEncoder();
	}


}

