package net.dkahn.starter.application.services.mail.dto;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Email {
    private String subject;
    private Map<String, Object> model;
    private String content;
    private List<String> recipients = Lists.newArrayList();
    private String messageKey;
    private List<Attachement>attachements = Lists.newArrayList();

    private Email() {

    }

    public interface Subjectable {
        Modelable subject(String subject);
    }

    public interface Modelable {
        Contentable model(Map<String, Object> model);
    }

    public interface Contentable {
        Recipientable content(String templateContent);
    }

    public interface Recipientable {
        Attachementable recipients(String... recipients);

        Attachementable recipients(List<String> recipients);
    }



    public interface Attachementable{
        Producer noAttachements();
        Producer attachements(Attachement... attachements);
        Producer attachements(List<Attachement> attachements);
    }


    public interface Producer {
        Email build();
    }

    public String getSubject() {
        return subject;
    }

    public Map<String, Object> getModel() {
        return model;
    }


    public String getContent() {
        return content;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }


    private class Builder implements Modelable, Subjectable, Contentable, Recipientable, Attachementable, Producer {


        @Override
        public Modelable subject(String subject) {
            Email.this.subject = subject;
            return this;
        }

        @Override
        public Contentable model(Map<String, Object> model) {
            Email.this.model = model;
            return this;
        }


        @Override
        public Recipientable content(String templateContentName) {
            Email.this.content = templateContentName;
            return this;
        }

        @Override
        public Attachementable recipients(String... recipients) {
            Email.this.recipients.addAll(Arrays.asList(recipients));
            return this;
        }

        @Override
        public Attachementable recipients(List<String> recipients) {
            Email.this.recipients.addAll(recipients);
            return this;
        }

/*        @Override
        public Attachementable messageKey(String messageKey) {
            Email.this.messageKey = messageKey;
            return this;
        }*/

        @Override
        public Producer noAttachements() {
            return this;
        }

        @Override
        public Producer attachements(Attachement... attachements) {
            Email.this.attachements.addAll(Arrays.asList(attachements));
            return this;
        }

        @Override
        public Producer attachements(List<Attachement> attachements) {
            Email.this.attachements.addAll(attachements);
            return this;
        }

        public Email build() {
            if (Email.this.subject != null && Email.this.model != null && Email.this.content != null
                   && Email.this.recipients != null) {
                return Email.this;
            } else {
                throw new IllegalArgumentException("some of the properties were not set.");
            }
        }

    }

    public static Subjectable builder() {
        return new Email().new Builder();
    }

}
