package dev.mfataka.esnzlin.config;

import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:39
 */
@Configuration
public class MailSenderConfig {


    @Bean
    public JavaMailSender mailSender(final MailProperties mailProperties) {
        final var javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        if (Objects.nonNull(mailProperties.getUsername())) {
            javaMailSender.setUsername(mailProperties.getUsername());
        }
        if (Objects.nonNull(mailProperties.getPassword())) {
            javaMailSender.setPassword(mailProperties.getPassword());
        }


        final var properties = MapUtils.toProperties(mailProperties.getProperties());
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

    @Bean
    public MailProperties mailProperties() {
        return new MailProperties();
    }


}