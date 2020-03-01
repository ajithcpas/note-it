package com.noteit.mail;

import com.noteit.AppServerConfig;
import com.noteit.auth.authorization.PasswordResetToken;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Data
@Service
public class MailService
{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AppServerConfig appServerConfig;

    public void sendPasswordResetToken(PasswordResetToken token) throws Exception
    {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setToAddress(token.getUser().getUsername());
        mailMessage.setSubject("NoteIt - Reset Password");

        String resetUrl = appServerConfig.getServerUrl() + "/authn/verify-token?id=" + token.getUser().getId() + "&token=" + token.getToken();
        String text = "Dear User,\n\n" +
                "Click the below link and reset your NoteIt account password.\n\n" +
                resetUrl +
                "\n\n\n";

        mailMessage.setText(text);
        send(mailMessage);
    }

    public void send(MailMessage mailMessage)
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mailMessage.getToAddress());
        msg.setSubject(mailMessage.getSubject());
        msg.setText(mailMessage.getText());
        javaMailSender.send(msg);
    }
}
