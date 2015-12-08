package net.dkahn.starter.application.services.mail.dto;

import org.springframework.core.io.ClassPathResource;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 14/12/12
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class Attachement {
    private ClassPathResource resource;
    private String attachmentName;

    public Attachement(ClassPathResource resource, String attachmentName) {
        this.resource = resource;
        this.attachmentName = attachmentName;
    }

    public ClassPathResource getResource() {

        return resource;
    }

    public void setResource(ClassPathResource resource) {
        this.resource = resource;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
