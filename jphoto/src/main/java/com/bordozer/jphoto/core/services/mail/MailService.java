package com.bordozer.jphoto.core.services.mail;

import javax.mail.MessagingException;

public interface MailService {

    void send(final MailBean mailBean) throws MessagingException;

    void sendNoException(final MailBean mailBean);
}
