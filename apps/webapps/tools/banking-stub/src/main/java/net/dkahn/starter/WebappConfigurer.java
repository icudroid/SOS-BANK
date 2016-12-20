package net.dkahn.starter;


import net.dkahn.starter.apps.webapps.bankingstub.MvcConfig;
import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan({"net.dkahn.starter"})
@Import({MvcConfig.class})
public class WebappConfigurer extends WebMvcConfigurerAdapter {


    @Log
    private Logger logger;


    @Bean
    public LocalValidatorFactoryBean validator(){
        return new LocalValidatorFactoryBean();
    }



    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(WebappConfigurer.class).run(args);
    }


}
