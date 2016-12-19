package net.dkahn.starter.tools;

import net.dkahn.starter.tools.cipher.PasswordBasedEncoderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("net.dkahn.starter")
public class Application {


	@Bean
	public PasswordBasedEncoderConfig passwordBasedEncoderConfig(){
		PasswordBasedEncoderConfig config = new PasswordBasedEncoderConfig();
		return config;
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}


}

