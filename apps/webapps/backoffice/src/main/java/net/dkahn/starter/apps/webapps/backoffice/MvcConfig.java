package net.dkahn.starter.apps.webapps.backoffice;

import net.dkahn.starter.apps.webapps.common.config.CustomReloadableResourceMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static String I18N_BASE = "classpath:i18n/";
    private static String I18N_SECURITY_BASE = "classpath:i18n/security/";

    @Bean
    public MessageSource messageSource(){
        CustomReloadableResourceMessageSource messageSource = new CustomReloadableResourceMessageSource();
        messageSource.setBasenames(
                I18N_BASE+"resources",I18N_BASE+"errors",I18N_BASE+"references",
                //module security i18n
                I18N_SECURITY_BASE+"resources",I18N_SECURITY_BASE+"errors"
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
