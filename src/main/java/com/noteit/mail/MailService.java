package com.noteit.mail;

import com.noteit.AppServerConfig;
import com.noteit.auth.Users;
import com.noteit.auth.authorization.PasswordResetToken;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

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
        mailMessage.setSubject("Reset Password | NoteIt");

        final String resetUrl = appServerConfig.getServerUrl() + "/authn/verify-token?id=" + token.getUser().getId() + "&token=" + token.getToken();
        String text = "Dear User,<br/>" +
                "<p>If you have requested to reset your NoteIt account password click the link below, otherwise ignore this email. <b>This link is only valid for the next one hour.</b></p>" +
                "<a href=\"" + resetUrl + "\" target=\"_blank\">Reset Password</a>" +
                "<br/><br/><br/>" +
                "Thanks,<br/>" +
                "The NoteIt Team";

        mailMessage.setText(text);
        send(mailMessage, false);
    }

    public void sendWelcomeAboard(Users user) throws MessagingException
    {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setToAddress(user.getUsername());
        mailMessage.setSubject("Welcome to NoteIt");

        final String supportMailId = appServerConfig.getValue("spring.mail.username");
        String text = "Welcome aboard,<br/>" +
                "<p>Thanks for choosing NoteIt as your notes taking app. Create and organize anything from thoughts and ideas to simple To-Do lists. We hope you enjoy the NoteIt. Keep taking notes...</p>" +
                "<p>If you have any query or feedback, please <a href=\"mailto:" + supportMailId + "\">write</a> to us.</p>" +
                "<br/><br/>" +
                "Thanks,<br/>" +
                "The NoteIt Team";

        mailMessage.setText(text);
        send(mailMessage);
    }

    public void send(MailMessage mailMessage) throws MessagingException
    {
        send(mailMessage, true);
    }

    public void send(MailMessage mailMessage, boolean async) throws MessagingException
    {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(mailMessage.getToAddress());
        helper.setSubject(mailMessage.getSubject());

        final String logoImage = "<div style=\"border-bottom:solid 1px #e6e6e6; text-align:center;\">" +
                "<a href=\"" + appServerConfig.getServerUrl() + "\" target=\"_blank\"><img src=\"cid:logo.png\" width=\"130px\" alt=\"NoteIt\"></img></a></div><br/>";
        helper.setText(logoImage + mailMessage.getText(), true);
        helper.addInline("logo.png", new ClassPathResource("static/assets/images/logo.png"));

        if (async)
        {
            Thread thred = new Thread(() -> javaMailSender.send(message));
            thred.start();
        }
        else
        {
            javaMailSender.send(message);
        }
    }
}
