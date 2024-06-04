package com.example.tasklist.service.impl;

import com.example.tasklist.model.entity.User;
import com.example.tasklist.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final Configuration configuration;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(
            final User user,
            final Properties properties
    ) {
        log.info("Отправка письма пользователю {} с параметрами {}",
                user.getUsername(), properties);

        try {
            String emailContent = getReminderEmailContent(user, properties);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    false,
                    "UTF-8"
            );
            helper.setSubject(
                    "Срок выполнения задачи истекает менее чем через 1 час");
            helper.setTo(user.getUsername());
            helper.setText(emailContent, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception exception) {
            log.error("Ошибка при отправке письма" + exception);
        }
    }

    private String getReminderEmailContent(
            final User user,
            final Properties properties
    ) throws IOException, TemplateException {
        log.info("Заполнение письма пользователю {} с параметрами {}",
                user.getUsername(), properties);

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));

        StringWriter writer = new StringWriter();
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);

        return writer.getBuffer()
                .toString();
    }
}
