package com.bordozer.jphoto.core.services.mail;

public class MailBean {

    private String fromAddress;
    private String[] toAddresses;
    private String[] ccAddresses;
    private String[] bccAddresses;

    private String subject;
    private String body;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(final String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String[] getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(final String[] toAddresses) {
        this.toAddresses = toAddresses;
    }

    public void setToAddress(final String toAddress) {
        final String[] strings = new String[1];
        strings[0] = toAddress;

        this.toAddresses = strings;
    }

    public String[] getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(final String[] ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public String[] getBccAddresses() {
        return bccAddresses;
    }

    public void setBccAddresses(final String[] bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }
}
