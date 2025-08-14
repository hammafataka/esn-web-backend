package dev.mfataka.esnzlin.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:34
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    // html reporting
    @SneakyThrows
    public void sendMail(final String html, final String to, final String subject) {
        log.info("Sending html mail to {}", to);
        final var mimeMessage = mailSender.createMimeMessage();
        final var messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        messageHelper.setFrom(mailProperties.getUsername());
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
        log.info("Mail sent to {}", to);
    }

    @SneakyThrows
    public void sendMail(final File file, final String to, final String subject) {
        log.info("Sending file mail to {}", to);
        final var mimeMessage = mailSender.createMimeMessage();
        final var messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        messageHelper.setSubject(subject);
        messageHelper.setFrom(mailProperties.getUsername());
        messageHelper.setText("Please review the attached report");
        messageHelper.setTo(to);
        messageHelper.addAttachment(file.getName(), file);
        mailSender.send(mimeMessage);
    }

}
