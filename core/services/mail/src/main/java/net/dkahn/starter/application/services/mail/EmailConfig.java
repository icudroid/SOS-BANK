package net.dkahn.starter.application.services.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 19/11/13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan("fr.k2i.adbeback.application.services.mail")
//@PropertySource(value = {"classpath:mail.properties"})
public class EmailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.transport.protocol}")
    private String protocol;

    @Value("${mail.defaultEncoding:UTF-8}")
    private String defaultEncoding;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.default.sender}")
    private String defaultFrom;

    @Value("${email.resources.uri}/images")
    private String imagesResources;

    @Value("${email.send:false}")
    private Boolean sendMail;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setProtocol(protocol);
        sender.setDefaultEncoding(defaultEncoding);
        sender.setUsername(username);
        sender.setPassword(password);
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth",auth);
        sender.setJavaMailProperties(props);
        return sender;
    }

    @Bean
    public IMailEngine mailEngine() throws Exception{
        IMailEngine mailEngine = new MailEngine(mailSender(),defaultFrom,templateEngine,imagesResources,sendMail);
        return mailEngine;
    }

}
