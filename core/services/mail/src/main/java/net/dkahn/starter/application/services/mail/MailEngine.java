package net.dkahn.starter.application.services.mail;

import net.dkahn.starter.application.services.mail.dto.Attachement;
import net.dkahn.starter.application.services.mail.dto.Email;
import net.dkahn.starter.application.services.mail.exception.SendException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for sending e-mail messages based on Velocity templates
 * or with attachments.
 * 
 * @author Matt Raible
 */
public class MailEngine implements IMailEngine{
    private final Log log = LogFactory.getLog(MailEngine.class);
    private JavaMailSender mailSender;
    private String defaultFrom;
    private SpringTemplateEngine templateEngine;
    private String imagesResources;
    private boolean send;

    private static final String OUTPUT_ENCODING = "UTF-8";


    public MailEngine(JavaMailSender mailSender, String defaultFrom,SpringTemplateEngine templateEngine,String imagesResources,boolean send) throws SendException {
        this.mailSender = mailSender;
        this.defaultFrom = defaultFrom;
        this.templateEngine = templateEngine;
        this.imagesResources = imagesResources;
        this.send = send;
    }

    @Override
    public void sendMessage(Email email) throws SendException {
        sendMessage(email,Locale.FRANCE);
    }



        /**
         * Send a simple message based.
         * @param email
         */
    @Override
    public void sendMessage(Email email, Locale locale) throws SendException {

        final String subject;
        final String content;
        final String messageKey = email.getMessageKey();
        final List<String> recipients = email.getRecipients();
        final List<Attachement> attachements = email.getAttachements();

        final Context context = new Context(locale);
        //WebContext context = new WebContext(request, null,  request.getSession().getServletContext(), request.getLocale());

        for (Map.Entry<String, Object> entry : email.getModel().entrySet()) {
            context.setVariable(entry.getKey(),entry.getValue());
        }

        content = retrieveContent(email, context);
        subject = email.getSubject();

        if(send){
            try {
                mailSender.send(new MimeMessagePreparator() {
                    public void prepare(MimeMessage mimeMessage) throws MessagingException {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setFrom(defaultFrom);
                        message.setTo(recipients.toArray(new String[recipients.size()]));
                        message.setSubject(subject);
                        message.setText(content,true);

                        Collection<String> imgToSearch = findImgToSearch(content);

                        if (imgToSearch != null) {
                            for (String filename : imgToSearch) {
                                ClassPathResource resource = new ClassPathResource(imagesResources+File.separator+filename);
                                message.addInline(filename, resource);
                            }
                        }

                        if(!attachements.isEmpty()){
                            for (Attachement attachement : attachements) {
                                message.addAttachment(attachement.getAttachmentName(),attachement.getResource());
                            }
                        }
                    }
                });
            } catch (MailException e) {
                log.error("Problem while sending email message : " , e);
                throw new SendException("Problem while sending email message to '" + email.getRecipients() + "'", e);
            }
        }
    }



    private Collection<String> findImgToSearch(String htmlContent) {

        Map<String, String> map = new HashMap<String, String>();
        // search for img tag with src attribute begining with cid 'cid or "cid
        Matcher matcherImg = Pattern.compile(
                "<\\s*img[^>]*src=([\"']?)cid[^>]*>", Pattern.CASE_INSENSITIVE)
                .matcher(htmlContent);

        // search for the content ID cid value (after cid: )
        Pattern patternCID = Pattern
                .compile(".*src=([\"']?)cid:([^>\\s]*)[\\s>]?",
                        Pattern.CASE_INSENSITIVE);
        while (matcherImg.find()) {
            String img = matcherImg.group();
            Matcher matcherCID = patternCID.matcher(img);
            while (matcherCID.find()) {
                String cid = matcherCID.group(2).replaceAll("[\"']", "");
                map.put(cid, cid);
            }
        }

        Collection<String> values = null;
        if (map.size() != 0) {
            values = map.values();
        }

        return values;
    }

    private String retrieveContent(Email email, Context context){
        return this.templateEngine.process(email.getContent(), context);
    }


}
