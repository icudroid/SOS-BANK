package net.dkahn.starter.apps.webapps.bankingstub;

import net.dkahn.starter.apps.webapps.common.config.CustomReloadableResourceMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static String I18N_BASE = "classpath:i18n/";

    @Autowired
    private ApplicationContext context;

    @Bean
    public MessageSource messageSource(){
        CustomReloadableResourceMessageSource messageSource = new CustomReloadableResourceMessageSource();
        messageSource.setBasenames(I18N_BASE+"resources",I18N_BASE+"errors",I18N_BASE+"references");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

}
