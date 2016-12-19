package net.dkahn.starter.application.services.mail.exception;

public class SendException extends Exception {

    private static final long serialVersionUID = 1L;

    public SendException() {
        super();
    }

    public SendException(String message, Exception ex) {
        super(message, ex);
    }
}
