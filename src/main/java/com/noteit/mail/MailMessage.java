package com.noteit.mail;

import lombok.Data;

@Data
public class MailMessage {
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String text;
}
